package userdashboard;

import config.Session;
import config.SessionManager;
import config.config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class booking extends javax.swing.JFrame {

    private void selectCabinInTable(int cabinId) {
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            int id = Integer.parseInt(jTable1.getValueAt(i, 0).toString());

            if (id == cabinId) {
                jTable1.setRowSelectionInterval(i, i);
                break;
            }
        }
    }

    public booking() {
        initComponents();
        initializeView();
    }

    public booking(int cabinId) {
        initComponents();
        initializeView();
        selectCabinInTable(cabinId);
    }

    private void initializeView() {
        if (!SessionManager.checkUser(this)) {
            return;
        }

        setLocationRelativeTo(null);
        setSize(980, 650);
        setResizable(false);
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"Cash", "GCash", "Card"}
        ));
        loadCabinsTable();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(860, 700));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(26, 46, 26));
        jPanel1.setPreferredSize(new java.awt.Dimension(860, 600));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("BOOK A CABIN");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(248, 30, -1, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 100, 180, 38));

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("PAYMENT METHOD");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 109, -1, -1));

        jButton1.setText("CONFIRM");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 520, 98, 34));

        jButton2.setText("BACK");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 520, 118, 34));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "CABIN ID", "CABIN NAME", "DESCRIPTION", "PRICE", "CAPACITY", "STATUS"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 156, 800, 320));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 930, 650));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a cabin first!");
            return;
        }

        String status = jTable1.getValueAt(selectedRow, 5).toString();

        if (!status.equalsIgnoreCase("Available")) {
            JOptionPane.showMessageDialog(this, "Cabin is not available.");
            return;
        }

        int cabinId = Integer.parseInt(jTable1.getValueAt(selectedRow, 0).toString());
        double amount = Double.parseDouble(jTable1.getValueAt(selectedRow, 3).toString());
        int userId = Session.getUserId();
        String paymentMethod = jComboBox1.getSelectedItem().toString();
        String bookingDate = java.time.LocalDateTime.now().withNano(0).toString().replace('T', ' ');

        String existingSql = "SELECT COUNT(*) FROM tbl_transactions "
                + "WHERE t_uid=? AND t_cid=? AND t_status IN ('Paid','Booked')";
        String insertSql = "INSERT INTO tbl_transactions "
                + "(t_uid, t_cid, t_date, t_status, t_amount, t_payment_method, t_notes) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        String updateCabinSql = "UPDATE tbl_cabins SET c_status='Occupied' WHERE c_id=?";

        try (Connection conn = config.connectDB()) {
            conn.setAutoCommit(false);

            try (PreparedStatement checkPst = conn.prepareStatement(existingSql)) {
                checkPst.setInt(1, userId);
                checkPst.setInt(2, cabinId);

                try (ResultSet rs = checkPst.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        JOptionPane.showMessageDialog(this, "You already have an active booking for this cabin.");
                        conn.rollback();
                        return;
                    }
                }
            }

            try (PreparedStatement insertPst = conn.prepareStatement(insertSql);
                 PreparedStatement updatePst = conn.prepareStatement(updateCabinSql)) {

                insertPst.setInt(1, userId);
                insertPst.setInt(2, cabinId);
                insertPst.setString(3, bookingDate);
                insertPst.setString(4, "Paid");
                insertPst.setDouble(5, amount);
                insertPst.setString(6, paymentMethod);
                insertPst.setString(7, "Booked and paid by guest");
                insertPst.executeUpdate();

                updatePst.setInt(1, cabinId);
                updatePst.executeUpdate();
            }

            conn.commit();
            JOptionPane.showMessageDialog(this, "Booking and payment recorded successfully.");
            loadCabinsTable();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Booking failed: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
        new userdashboard().setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
    }//GEN-LAST:event_jComboBox1ActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(booking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(booking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(booking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(booking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new booking().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

    private void loadCabinsTable() {
        String sql = "SELECT c_id, c_name, c_description, c_price, c_capacity, c_status "
                + "FROM tbl_cabins ORDER BY c_id";

        try (Connection conn = config.connectDB();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("c_id"),
                    rs.getString("c_name"),
                    rs.getString("c_description"),
                    rs.getDouble("c_price"),
                    rs.getInt("c_capacity"),
                    rs.getString("c_status")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading cabins: " + e.getMessage());
        }
    }
}
