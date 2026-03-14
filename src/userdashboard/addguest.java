/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userdashboard;
import config.config;
import config.Session;
import java.sql.*;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *
 * @author Archie
 */
public class addguest extends javax.swing.JFrame {

    public addguest() {
        initComponents();
        setLocationRelativeTo(null);
        loadAvailableCabins();
        txtCheckinDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtFirstName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtLastName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtContact = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cmbCabin = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtCheckinDate = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtCheckoutDate = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtTotalAmount = new javax.swing.JTextField();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        lblCabinPrice = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add Guest - Forest Staycation");

        jPanel1.setBackground(new java.awt.Color(45, 80, 45));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("ADD NEW GUEST");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("First Name:");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Last Name:");

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Contact Number:");

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Email:");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Select Cabin:");

        cmbCabin.addActionListener(evt -> updateCabinPrice());

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Check-in Date (YYYY-MM-DD):");

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Check-out Date (YYYY-MM-DD):");

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Total Amount:");

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Cabin Price/Night:");

        lblCabinPrice.setForeground(new java.awt.Color(255, 255, 0));
        lblCabinPrice.setText("₱0.00");

        btnSave.setBackground(new java.awt.Color(40, 167, 69));
        btnSave.setFont(new java.awt.Font("Segoe UI", 1, 12));
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("SAVE GUEST");
        btnSave.addActionListener(evt -> btnSaveActionPerformed(evt));

        btnCancel.setBackground(new java.awt.Color(108, 117, 125));
        btnCancel.setFont(new java.awt.Font("Segoe UI", 1, 12));
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setText("CANCEL");
        btnCancel.addActionListener(evt -> dispose());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(btnSave)
                            .addGap(18, 18, 18)
                            .addComponent(btnCancel))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(jLabel4)
                                .addComponent(jLabel6)
                                .addComponent(jLabel7)
                                .addComponent(jLabel9))
                            .addGap(30, 30, 30)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtFirstName)
                                .addComponent(txtContact)
                                .addComponent(cmbCabin, 0, 200, Short.MAX_VALUE)
                                .addComponent(txtCheckinDate)
                                .addComponent(txtTotalAmount)))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3)
                                .addComponent(jLabel5)
                                .addComponent(jLabel10)
                                .addComponent(jLabel8))
                            .addGap(30, 30, 30)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtLastName)
                                .addComponent(txtEmail)
                                .addComponent(lblCabinPrice)
                                .addComponent(txtCheckoutDate, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)))))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtContact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cmbCabin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(lblCabinPrice))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtCheckinDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtCheckoutDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtTotalAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnCancel))
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

    private void loadAvailableCabins() {
        cmbCabin.removeAllItems();
        try {
            Connection conn = config.connectDB();
            String sql = "SELECT c_id, c_name, c_price FROM tbl_cabins WHERE c_status = 'Available'";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                cmbCabin.addItem(rs.getInt("c_id") + " - " + rs.getString("c_name") + " (₱" + rs.getDouble("c_price") + "/night)");
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateCabinPrice() {
        if (cmbCabin.getSelectedItem() == null) return;
        
        String selected = cmbCabin.getSelectedItem().toString();
        int cabinId = Integer.parseInt(selected.split(" - ")[0]);
        
        try {
            Connection conn = config.connectDB();
            String sql = "SELECT c_price FROM tbl_cabins WHERE c_id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, cabinId);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                lblCabinPrice.setText("₱" + String.format("%.2f", rs.getDouble("c_price")));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {
        // Validation
        if (txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty() || 
            txtContact.getText().isEmpty() || cmbCabin.getSelectedItem() == null ||
            txtCheckinDate.getText().isEmpty() || txtCheckoutDate.getText().isEmpty() ||
            txtTotalAmount.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
            return;
        }

        String selected = cmbCabin.getSelectedItem().toString();
        int cabinId = Integer.parseInt(selected.split(" - ")[0]);

        try {
            Connection conn = config.connectDB();
            
            // Insert guest
            String sql = "INSERT INTO tbl_guests (g_fname, g_lname, g_contact, g_email, g_cabin_id, g_checkin_date, g_checkout_date, g_total_amount, g_status, g_created_by) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'Active', ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtFirstName.getText());
            pst.setString(2, txtLastName.getText());
            pst.setString(3, txtContact.getText());
            pst.setString(4, txtEmail.getText());
            pst.setInt(5, cabinId);
            pst.setString(6, txtCheckinDate.getText());
            pst.setString(7, txtCheckoutDate.getText());
            pst.setDouble(8, Double.parseDouble(txtTotalAmount.getText()));
            pst.setInt(9, Session.getUserId());
            pst.executeUpdate();

            // Update cabin status to Occupied
            String updateCabin = "UPDATE tbl_cabins SET c_status = 'Occupied' WHERE c_id = ?";
            PreparedStatement cabinPst = conn.prepareStatement(updateCabin);
            cabinPst.setInt(1, cabinId);
            cabinPst.executeUpdate();

            conn.close();
            
            JOptionPane.showMessageDialog(this, "Guest added successfully!");
            dispose();
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.");
        }
    }

    // Variables declaration
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cmbCabin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblCabinPrice;
    private javax.swing.JTextField txtCheckinDate;
    private javax.swing.JTextField txtCheckoutDate;
    private javax.swing.JTextField txtContact;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtTotalAmount;
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 506, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 438, Short.MAX_VALUE)
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
            java.util.logging.Logger.getLogger(addguest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(addguest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(addguest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(addguest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new addguest().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
