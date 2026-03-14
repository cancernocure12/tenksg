
package userdashboard;
 

import config.config;
import config.Session;
import config.guestinfo;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class activeguest extends javax.swing.JFrame {
    
    DefaultTableModel model;

    public activeguest() {
        initComponents();
        setLocationRelativeTo(null);
        loadActiveGuests();
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblActiveGuests = new javax.swing.JTable();
        btnCheckout = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Active Guests - Forest Staycation");

        jPanel1.setBackground(new java.awt.Color(45, 80, 45));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("ACTIVE GUESTS");

        tblActiveGuests.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"ID", "Guest Name", "Contact", "Cabin", "Check-in Date", "Total Amount", "Status"}
        ) {
            boolean[] canEdit = new boolean [] {false, false, false, false, false, false, false};
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblActiveGuests);

        btnCheckout.setBackground(new java.awt.Color(220, 53, 69));
        btnCheckout.setFont(new java.awt.Font("Segoe UI", 1, 12));
        btnCheckout.setForeground(new java.awt.Color(255, 255, 255));
        btnCheckout.setText("CHECK OUT");
        btnCheckout.addActionListener(evt -> btnCheckoutActionPerformed(evt));

        btnRefresh.setBackground(new java.awt.Color(40, 167, 69));
        btnRefresh.setFont(new java.awt.Font("Segoe UI", 1, 12));
        btnRefresh.setForeground(new java.awt.Color(255, 255, 255));
        btnRefresh.setText("REFRESH");
        btnRefresh.addActionListener(evt -> loadActiveGuests());

        btnBack.setBackground(new java.awt.Color(108, 117, 125));
        btnBack.setFont(new java.awt.Font("Segoe UI", 1, 12));
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setText("BACK");
        btnBack.addActionListener(evt -> {
            new userdashboard().setVisible(true);
            dispose();
        });

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Search:");

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchGuests(txtSearch.getText());
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnCheckout)
                        .addGap(18, 18, 18)
                        .addComponent(btnRefresh)
                        .addGap(18, 18, 18)
                        .addComponent(btnBack)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCheckout)
                    .addComponent(btnRefresh)
                    .addComponent(btnBack))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    private void loadActiveGuests() {
        model = (DefaultTableModel) tblActiveGuests.getModel();
        model.setRowCount(0);
        
        try {
            Connection conn = config.connectDB();
            String sql = "SELECT g.g_id, g.g_fname || ' ' || g.g_lname as name, g.g_contact, " +
                        "c.c_name, g.g_checkin_date, g.g_total_amount, g.g_status " +
                        "FROM tbl_guests g " +
                        "LEFT JOIN tbl_cabins c ON g.g_cabin_id = c.c_id " +
                        "WHERE g.g_status = 'Active' " +
                        "ORDER BY g.g_checkin_date DESC";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("g_id"),
                    rs.getString("name"),
                    rs.getString("g_contact"),
                    rs.getString("c_name"),
                    rs.getString("g_checkin_date"),
                    "₱" + String.format("%.2f", rs.getDouble("g_total_amount")),
                    rs.getString("g_status")
                });
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading guests: " + e.getMessage());
        }
    }

    private void searchGuests(String keyword) {
        model = (DefaultTableModel) tblActiveGuests.getModel();
        model.setRowCount(0);
        
        try {
            Connection conn = config.connectDB();
            String sql = "SELECT g.g_id, g.g_fname || ' ' || g.g_lname as name, g.g_contact, " +
                        "c.c_name, g.g_checkin_date, g.g_total_amount, g.g_status " +
                        "FROM tbl_guests g " +
                        "LEFT JOIN tbl_cabins c ON g.g_cabin_id = c.c_id " +
                        "WHERE g.g_status = 'Active' AND " +
                        "(g.g_fname LIKE ? OR g.g_lname LIKE ? OR c.c_name LIKE ?) " +
                        "ORDER BY g.g_checkin_date DESC";
            PreparedStatement pst = conn.prepareStatement(sql);
            String search = "%" + keyword + "%";
            pst.setString(1, search);
            pst.setString(2, search);
            pst.setString(3, search);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("g_id"),
                    rs.getString("name"),
                    rs.getString("g_contact"),
                    rs.getString("c_name"),
                    rs.getString("g_checkin_date"),
                    "₱" + String.format("%.2f", rs.getDouble("g_total_amount")),
                    rs.getString("g_status")
                });
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void btnCheckoutActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedRow = tblActiveGuests.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a guest to check out.");
            return;
        }

        int guestId = (int) tblActiveGuests.getValueAt(selectedRow, 0);
        String guestName = (String) tblActiveGuests.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to check out " + guestName + "?",
            "Confirm Checkout", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = config.connectDB();
                
                // Get guest details for history
                String getSql = "SELECT g_cabin_id, g_checkin_date, g_total_amount FROM tbl_guests WHERE g_id = ?";
                PreparedStatement getPst = conn.prepareStatement(getSql);
                getPst.setInt(1, guestId);
                ResultSet rs = getPst.executeQuery();
                
                int cabinId = 0;
                String checkinDate = "";
                double totalAmount = 0;
                
                if (rs.next()) {
                    cabinId = rs.getInt("g_cabin_id");
                    checkinDate = rs.getString("g_checkin_date");
                    totalAmount = rs.getDouble("g_total_amount");
                }

                // Update guest status
                String updateGuest = "UPDATE tbl_guests SET g_status = 'Checked Out', g_checkout_date = date('now') WHERE g_id = ?";
                PreparedStatement updatePst = conn.prepareStatement(updateGuest);
                updatePst.setInt(1, guestId);
                updatePst.executeUpdate();

                // Update cabin status to Available
                String updateCabin = "UPDATE tbl_cabins SET c_status = 'Available' WHERE c_id = ?";
                PreparedStatement cabinPst = conn.prepareStatement(updateCabin);
                cabinPst.setInt(1, cabinId);
                cabinPst.executeUpdate();

                // Add to check-in history
                String historySql = "INSERT INTO tbl_checkin_history (ch_guest_id, ch_cabin_id, ch_checkin_date, ch_checkout_date, ch_total_amount, ch_processed_by) " +
                                   "VALUES (?, ?, ?, date('now'), ?, ?)";
                PreparedStatement historyPst = conn.prepareStatement(historySql);
                historyPst.setInt(1, guestId);
                historyPst.setInt(2, cabinId);
                historyPst.setString(3, checkinDate);
                historyPst.setDouble(4, totalAmount);
                historyPst.setInt(5, Session.getUserId());
                historyPst.executeUpdate();

                conn.close();
                
                JOptionPane.showMessageDialog(this, "Guest checked out successfully!");
                loadActiveGuests();
                
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    // Variables declaration
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCheckout;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblActiveGuests;
    private javax.swing.JTextField txtSearch;
    
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(204, 255, 102));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 314, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 368, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 519, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(242, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(activeguest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(activeguest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(activeguest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(activeguest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new activeguest().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
