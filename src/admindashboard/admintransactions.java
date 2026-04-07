package admindashboard;

import Main.landing;
import config.Session;
import config.SessionManager;
import config.config;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class admintransactions extends JFrame {

    private JTable transactionTable;

    public admintransactions() {
        if (!SessionManager.checkAdmin(this)) {
            return;
        }

        initComponents();
        loadTransactions();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Admin Transaction Management");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(1100, 650);
        setLayout(new BorderLayout(10, 10));

        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Transaction ID", "Guest", "Cabin", "Date", "Amount", "Payment", "Status", "Notes"}
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        transactionTable = new JTable(model);
        add(new JScrollPane(transactionTable), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(evt -> loadTransactions());
        actions.add(refreshButton);

        JButton paidButton = new JButton("Mark Paid");
        paidButton.addActionListener(evt -> updateTransactionStatus("Paid", false));
        actions.add(paidButton);

        JButton completedButton = new JButton("Complete");
        completedButton.addActionListener(evt -> updateTransactionStatus("Completed", true));
        actions.add(completedButton);

        JButton cancelledButton = new JButton("Cancel Booking");
        cancelledButton.addActionListener(evt -> updateTransactionStatus("Cancelled", true));
        actions.add(cancelledButton);

        JButton exportButton = new JButton("Export Report");
        exportButton.addActionListener(evt -> exportReport());
        actions.add(exportButton);

        JButton dashboardButton = new JButton("Dashboard");
        dashboardButton.addActionListener(evt -> {
            new admindashboard().setVisible(true);
            dispose();
        });
        actions.add(dashboardButton);

        JButton cabinsButton = new JButton("Cabins");
        cabinsButton.addActionListener(evt -> {
            new cabinmanagement().setVisible(true);
            dispose();
        });
        actions.add(cabinsButton);

        JButton usersButton = new JButton("Users");
        usersButton.addActionListener(evt -> {
            new usermanagement().setVisible(true);
            dispose();
        });
        actions.add(usersButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(evt -> {
            Session.logout();
            new landing().setVisible(true);
            dispose();
        });
        actions.add(logoutButton);

        add(actions, BorderLayout.NORTH);
    }

    private void loadTransactions() {
        String sql = "SELECT t.t_id, "
                + "(a.u_fname || ' ' || a.u_lname) AS guest_name, "
                + "c.c_name, t.t_date, t.t_amount, t.t_payment_method, t.t_status, t.t_notes "
                + "FROM tbl_transactions t "
                + "JOIN tbl_accounts a ON t.t_uid = a.u_id "
                + "JOIN tbl_cabins c ON t.t_cid = c.c_id "
                + "ORDER BY t.t_id DESC";

        DefaultTableModel model = (DefaultTableModel) transactionTable.getModel();
        model.setRowCount(0);

        try (Connection conn = config.connectDB();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("t_id"),
                    rs.getString("guest_name"),
                    rs.getString("c_name"),
                    rs.getString("t_date"),
                    rs.getDouble("t_amount"),
                    rs.getString("t_payment_method"),
                    rs.getString("t_status"),
                    rs.getString("t_notes")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Unable to load transactions: " + e.getMessage());
        }
    }

    private void updateTransactionStatus(String status, boolean releaseCabin) {
        int selectedRow = transactionTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a transaction first.");
            return;
        }

        int transactionId = Integer.parseInt(transactionTable.getValueAt(selectedRow, 0).toString());
        String notes = JOptionPane.showInputDialog(this, "Notes for this update:", "");

        String txSql = "UPDATE tbl_transactions SET t_status=?, t_notes=? WHERE t_id=?";
        String cabinSql = "UPDATE tbl_cabins SET c_status='Available' "
                + "WHERE c_id=(SELECT t_cid FROM tbl_transactions WHERE t_id=?)";

        try (Connection conn = config.connectDB()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pst = conn.prepareStatement(txSql)) {
                pst.setString(1, status);
                pst.setString(2, notes == null ? "" : notes.trim());
                pst.setInt(3, transactionId);
                pst.executeUpdate();
            }

            if (releaseCabin) {
                try (PreparedStatement pst = conn.prepareStatement(cabinSql)) {
                    pst.setInt(1, transactionId);
                    pst.executeUpdate();
                }
            }

            conn.commit();
            JOptionPane.showMessageDialog(this, "Transaction updated.");
            loadTransactions();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Unable to update transaction: " + e.getMessage());
        }
    }

    private void exportReport() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("staycation-transactions-report.csv"));

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("transaction_id,guest,cabin,date,amount,payment_method,status,notes");

            for (int row = 0; row < transactionTable.getRowCount(); row++) {
                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s%n",
                        csvValue(transactionTable.getValueAt(row, 0)),
                        csvValue(transactionTable.getValueAt(row, 1)),
                        csvValue(transactionTable.getValueAt(row, 2)),
                        csvValue(transactionTable.getValueAt(row, 3)),
                        csvValue(transactionTable.getValueAt(row, 4)),
                        csvValue(transactionTable.getValueAt(row, 5)),
                        csvValue(transactionTable.getValueAt(row, 6)),
                        csvValue(transactionTable.getValueAt(row, 7)));
            }

            JOptionPane.showMessageDialog(this, "Report exported to " + file.getAbsolutePath());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Unable to export report: " + e.getMessage());
        }
    }

    private String csvValue(Object value) {
        String text = value == null ? "" : value.toString().replace("\"", "\"\"");
        return "\"" + text + "\"";
    }
}
