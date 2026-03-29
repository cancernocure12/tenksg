package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JTable;
import net.proteanit.sql.DbUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class config {

    // ================= DATABASE CONNECTION =================
    public static Connection connectDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:forest.db");
        } catch (Exception e) {
            System.out.println("Database Connection Error: " + e.getMessage());
            return null;
        }
    }

    // ================= INSERT RECORD =================
    public void addRecord(String sql, Object... values) {

        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Add Record Error: " + e.getMessage());
        }
    }

    // ================= EXECUTE UPDATE/DELETE =================
    public void executeSQL(String sql, Object... values) {

        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("SQL Execution Error: " + e.getMessage());
        }
    }

    // ================= SELECT DATA =================
    public ResultSet getData(String sql, Object... values) {

        try {
            Connection conn = connectDB();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            return pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println("Query Error: " + e.getMessage());
            return null;
        }
    }

    // ================= LOGIN AUTHENTICATION =================
    public boolean authenticate(String sql, Object... values) {

        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
        }

        return false;
    }

    // ================= DISPLAY DATA IN TABLE =================
    public void displayData(String sql, JTable table, Object... values) {

        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            ResultSet rs = pstmt.executeQuery();
            table.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (SQLException e) {
            System.out.println("Display Error: " + e.getMessage());
        }
    }

    // ================= PASSWORD HASHING =================
    public static String hashPassword(String password) {

        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {

                String hex = Integer.toHexString(0xff & b);

                if (hex.length() == 1) {
                    hexString.append('0');
                }

                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
            return password;

        }
    }

    public static boolean verifyPassword(String inputPassword, String storedHash) {

        String inputHash = hashPassword(inputPassword);
        return inputHash.equals(storedHash);

    }

    // ================= DASHBOARD STATISTICS =================

    public static int getTotalUsers() {

        int count = 0;

        try (Connection conn = connectDB();
             PreparedStatement pst = conn.prepareStatement(
             "SELECT COUNT(*) FROM tbl_accounts WHERE u_role != 'admin'");
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    public static int getTotalCabins() {

        int count = 0;

        try (Connection conn = connectDB();
             PreparedStatement pst = conn.prepareStatement(
             "SELECT COUNT(*) FROM tbl_cabins");
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    public static int getTotalBookings() {

        int count = 0;

        try (Connection conn = connectDB();
             PreparedStatement pst = conn.prepareStatement(
             "SELECT COUNT(*) FROM tbl_transactions");
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    public static int getAvailableCabins() {

        int count = 0;

        try (Connection conn = connectDB();
             PreparedStatement pst = conn.prepareStatement(
             "SELECT COUNT(*) FROM tbl_cabins WHERE c_status='Available'");
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    public static int getOccupiedCabins() {

        int count = 0;

        try (Connection conn = connectDB();
             PreparedStatement pst = conn.prepareStatement(
             "SELECT COUNT(*) FROM tbl_cabins WHERE c_status='Occupied'");
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    public static int getActiveGuests() {

        int count = 0;

        try (Connection conn = connectDB();
             PreparedStatement pst = conn.prepareStatement(
             "SELECT COUNT(*) FROM tbl_guests WHERE g_status='Active'");
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

}