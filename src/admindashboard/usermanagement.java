
package admindashboard;
import config.config;
import javax.swing.*;
import java.awt.event.*;
import config.Session; 
public class usermanagement extends javax.swing.JFrame {
    config cfg = new config();
    
    public usermanagement() {
       initComponents();
       txtsearch.addKeyListener(new java.awt.event.KeyAdapter() {
    public void keyReleased(java.awt.event.KeyEvent evt) {
        searchUsers();
    }
});
       cmbfilter.setModel(new javax.swing.DefaultComboBoxModel<>(
    new String[] { "All", "ADMIN", "USER", "Approved", "Pending" }
));

    if(Session.getUserId() == 0){
        JOptionPane.showMessageDialog(this,"Please login first!");
        new Main.login().setVisible(true);
        dispose();
        return;
    }
    if(!Session.isAdmin()){
    JOptionPane.showMessageDialog(this,"Admin access only!");
    new Main.login().setVisible(true);
    dispose();
    return;
}
    setLayout(null); 
    loadUsers();

    
   
    }
private void searchUsers() {

    String keyword = txtsearch.getText().trim();
    String filter  = cmbfilter.getSelectedItem().toString();

    String sql = "SELECT u_id, u_fname, u_lname, u_status, u_role "
               + "FROM tbl_accounts WHERE 1=1 ";

    // 🔎 Search by name or email
    if (!keyword.isEmpty()) {
        sql += "AND (u_fname LIKE '%" + keyword + "%' "
            + "OR u_lname LIKE '%" + keyword + "%' "
            + "OR u_email LIKE '%" + keyword + "%') ";
    }

    // 🎯 Filter
    if (!filter.equals("All")) {

        if (filter.equalsIgnoreCase("ADMIN") || filter.equalsIgnoreCase("USER")) {
            sql += "AND u_role = '" + filter + "' ";
        } else {
            sql += "AND u_status = '" + filter + "' ";
        }
    }

    cfg.displayData(sql, jTable1);
}
    private void loadUsers() {
        String sql = "SELECT u_id, u_fname, u_lname, u_status FROM tbl_accounts";
        cfg.displayData(sql, jTable1); // use your NetBeans table
    }
    
    
    private void addUser() {
        String fname = JOptionPane.showInputDialog(this, "Enter First Name:");
    String lname = JOptionPane.showInputDialog(this, "Enter Last Name:");
    String email = JOptionPane.showInputDialog(this, "Enter Email:");
    String password = JOptionPane.showInputDialog(this, "Enter Password:");

    if(fname == null || lname == null || email == null || password == null){
        return;
    }

    if(fname.isEmpty() || lname.isEmpty() || email.isEmpty() || password.isEmpty()){
        JOptionPane.showMessageDialog(this, "All fields are required!");
        return;
    }

    String sql = "INSERT INTO tbl_accounts (u_fname, u_lname, u_email, u_password, u_role, u_status) "
               + "VALUES (?, ?, ?, ?, ?, ?)";

    try (java.sql.Connection conn = config.connectDB();
         java.sql.PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setString(1, fname);
        pst.setString(2, lname);
        pst.setString(3, email);
        pst.setString(4, password);
        pst.setString(5, "USER");      // default role
        pst.setString(6, "Pending");  // default status

        pst.executeUpdate();

        JOptionPane.showMessageDialog(this, "User Added Successfully!");
        loadUsers();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        e.printStackTrace();
    }
    }

    private void updateUser() {
       int selectedRow = jTable1.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Select user first!");
        return;
    }

    int userId = Integer.parseInt(jTable1.getValueAt(selectedRow, 0).toString());

    String fname = JOptionPane.showInputDialog(this, "Enter New First Name:");
    String lname = JOptionPane.showInputDialog(this, "Enter New Last Name:");
    String email = JOptionPane.showInputDialog(this, "Enter New Email:");
    String role  = JOptionPane.showInputDialog(this, "Enter Role (ADMIN/USER):");
    String status = JOptionPane.showInputDialog(this, "Enter Status (Approved/Pending):");

    if(fname == null || lname == null || email == null || role == null || status == null){
        return;
    }

    String sql = "UPDATE tbl_accounts SET u_fname=?, u_lname=?, u_email=?, u_role=?, u_status=? WHERE u_id=?";

    try (java.sql.Connection conn = config.connectDB();
         java.sql.PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setString(1, fname);
        pst.setString(2, lname);
        pst.setString(3, email);
        pst.setString(4, role.trim());
        pst.setString(5, status.trim());
        pst.setInt(6, userId);

        pst.executeUpdate();

        JOptionPane.showMessageDialog(this, "User Updated Successfully!");
        loadUsers();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        e.printStackTrace();
    }
    }
    
    private void deleteUser() {
          int selectedRow = jTable1.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Select user first!");
        return;
    }

    int userId = Integer.parseInt(jTable1.getValueAt(selectedRow, 0).toString());

    int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this user?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);

    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    String sql = "DELETE FROM tbl_accounts WHERE u_id=?";

    try (java.sql.Connection conn = config.connectDB();
         java.sql.PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setInt(1, userId);
        pst.executeUpdate();

        JOptionPane.showMessageDialog(this, "User Deleted Successfully!");
        loadUsers();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        e.printStackTrace();
    }
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        txtsearch = new javax.swing.JTextField();
        cmbfilter = new javax.swing.JComboBox<>();
        btnsearch = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1020, 790));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(26, 46, 26));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 500));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(26, 46, 26));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(26, 46, 26));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        jPanel4.setPreferredSize(new java.awt.Dimension(152, 82));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-bonsai-62.png"))); // NOI18N
        jPanel4.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, -1, -1));

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 90));

        jButton4.setFont(new java.awt.Font("Segoe UI Black", 0, 22)); // NOI18N
        jButton4.setText("DASHBOARD");
        jButton4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, 160, 60));

        jButton10.setFont(new java.awt.Font("Segoe UI Black", 0, 20)); // NOI18N
        jButton10.setText("Guest");
        jButton10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(255, 255, 255), new java.awt.Color(204, 204, 204), new java.awt.Color(255, 255, 255), new java.awt.Color(204, 204, 204)));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 230, 160, 50));

        jButton11.setFont(new java.awt.Font("Segoe UI Black", 0, 20)); // NOI18N
        jButton11.setText("Cabins");
        jButton11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 310, 160, 50));

        jButton7.setFont(new java.awt.Font("Segoe UI Black", 0, 20)); // NOI18N
        jButton7.setText("Booking");
        jPanel2.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 390, 160, 50));

        jButton5.setFont(new java.awt.Font("Segoe UI Black", 0, 20)); // NOI18N
        jButton5.setText("My Profile");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 470, 160, 50));

        jPanel5.setBackground(new java.awt.Color(26, 46, 26));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton6.setFont(new java.awt.Font("Segoe UI Black", 0, 20)); // NOI18N
        jButton6.setText("Logout");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, 160, 40));

        jPanel2.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 590, 250, 160));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-dashboard-layout-50 (1).png"))); // NOI18N
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, 70));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-user-account-48.png"))); // NOI18N
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, -1, 70));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-house-50.png"))); // NOI18N
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, -1, 50));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-booking-50.png"))); // NOI18N
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, -1, -1));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-profile-50.png"))); // NOI18N
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 470, -1, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 850));

        jPanel3.setBackground(new java.awt.Color(26, 46, 26));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        jPanel3.setPreferredSize(new java.awt.Dimension(290, 60));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("User Table");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 260, -1));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 0, 860, 70));

        jButton2.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton2.setText("Update User");
        jButton2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 110, 140, 40));

        jButton3.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton3.setText("Delete User");
        jButton3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 110, 140, 40));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 220, 650, 500));

        txtsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsearchActionPerformed(evt);
            }
        });
        jPanel1.add(txtsearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 170, 130, -1));

        cmbfilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbfilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbfilterActionPerformed(evt);
            }
        });
        jPanel1.add(cmbfilter, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 170, 100, -1));

        btnsearch.setText("search");
        btnsearch.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsearchActionPerformed(evt);
            }
        });
        jPanel1.add(btnsearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 170, 80, 30));

        jButton1.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton1.setText("Add User");
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 110, 150, 40));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1160, 840));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        addUser();// TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        updateUser();// TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        deleteUser();// TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void cmbfilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbfilterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbfilterActionPerformed

    private void btnsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsearchActionPerformed
        searchUsers();
    }//GEN-LAST:event_btnsearchActionPerformed

    private void txtsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtsearchActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        usermanagement um = new usermanagement();
        um.setVisible(true);          // show the JFrame
        um.setLocationRelativeTo(null); // optional: center on screen
        this.dispose();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        cabinmanagement cm = new cabinmanagement();
        cm.setVisible(true);
        this.dispose(); //// TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        new adminprofile().setVisible(true); // open profile
        dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
       
    }//GEN-LAST:event_jButton6ActionPerformed

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
            java.util.logging.Logger.getLogger(usermanagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(usermanagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(usermanagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(usermanagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new usermanagement().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnsearch;
    private javax.swing.JComboBox<String> cmbfilter;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtsearch;
    // End of variables declaration//GEN-END:variables
}
