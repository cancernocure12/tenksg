package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JTable;
import net.proteanit.sql.DbUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class config {

    private static boolean initialized = false;

    // ================= DATABASE CONNECTION =================
    public static Connection connectDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:forest.db");
            initializeDatabase(conn);
            return conn;
        } catch (Exception e) {
            System.out.println("Database Connection Error: " + e.getMessage());
            return null;
        }
    }

    private static synchronized void initializeDatabase(Connection conn) throws SQLException {
        if (initialized) {
            return;
        }

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tbl_accounts ("
                    + "u_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "u_fname TEXT NOT NULL,"
                    + "u_lname TEXT NOT NULL,"
                    + "u_email TEXT NOT NULL UNIQUE,"
                    + "u_password TEXT NOT NULL,"
                    + "u_role TEXT NOT NULL DEFAULT 'USER',"
                    + "u_status TEXT NOT NULL DEFAULT 'Pending'"
                    + ")");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tbl_cabins ("
                    + "c_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "c_name TEXT NOT NULL,"
                    + "c_description TEXT NOT NULL DEFAULT '',"
                    + "c_price REAL NOT NULL DEFAULT 0,"
                    + "c_capacity INTEGER NOT NULL DEFAULT 1,"
                    + "c_status TEXT NOT NULL DEFAULT 'Available'"
                    + ")");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tbl_transactions ("
                    + "t_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "t_uid INTEGER NOT NULL,"
                    + "t_cid INTEGER NOT NULL,"
                    + "t_date TEXT NOT NULL,"
                    + "t_status TEXT NOT NULL DEFAULT 'Booked',"
                    + "t_amount REAL NOT NULL DEFAULT 0,"
                    + "t_payment_method TEXT NOT NULL DEFAULT 'Cash',"
                    + "t_notes TEXT NOT NULL DEFAULT '',"
                    + "FOREIGN KEY (t_uid) REFERENCES tbl_accounts(u_id),"
                    + "FOREIGN KEY (t_cid) REFERENCES tbl_cabins(c_id)"
                    + ")");
        }

        ensureColumn(conn, "tbl_accounts", "u_role", "TEXT NOT NULL DEFAULT 'USER'");
        ensureColumn(conn, "tbl_accounts", "u_status", "TEXT NOT NULL DEFAULT 'Pending'");
        ensureColumn(conn, "tbl_cabins", "c_description", "TEXT NOT NULL DEFAULT ''");
        ensureColumn(conn, "tbl_cabins", "c_price", "REAL NOT NULL DEFAULT 0");
        ensureColumn(conn, "tbl_cabins", "c_capacity", "INTEGER NOT NULL DEFAULT 1");
        ensureColumn(conn, "tbl_cabins", "c_status", "TEXT NOT NULL DEFAULT 'Available'");
        ensureColumn(conn, "tbl_transactions", "t_amount", "REAL NOT NULL DEFAULT 0");
        ensureColumn(conn, "tbl_transactions", "t_payment_method", "TEXT NOT NULL DEFAULT 'Cash'");
        ensureColumn(conn, "tbl_transactions", "t_notes", "TEXT NOT NULL DEFAULT ''");

        migrateLegacyPasswords(conn);
        seedDefaultAdmin(conn);
        initialized = true;
    }

    private static void ensureColumn(Connection conn, String tableName, String columnName, String definition)
            throws SQLException {
        boolean exists = false;

        try (PreparedStatement pst = conn.prepareStatement("PRAGMA table_info(" + tableName + ")");
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                if (columnName.equalsIgnoreCase(rs.getString("name"))) {
                    exists = true;
                    break;
                }
            }
        }

        if (!exists) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + definition);
            }
        }
    }

    private static void migrateLegacyPasswords(Connection conn) throws SQLException {
        String sql = "SELECT u_id, u_password FROM tbl_accounts";
        String updateSql = "UPDATE tbl_accounts SET u_password=? WHERE u_id=?";

        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery();
             PreparedStatement updatePst = conn.prepareStatement(updateSql)) {

            while (rs.next()) {
                int userId = rs.getInt("u_id");
                String storedPassword = rs.getString("u_password");

                if (storedPassword != null && !isSha256Hash(storedPassword)) {
                    updatePst.setString(1, hashPassword(storedPassword));
                    updatePst.setInt(2, userId);
                    updatePst.executeUpdate();
                }
            }
        }
    }

    private static void seedDefaultAdmin(Connection conn) throws SQLException {
        String countSql = "SELECT COUNT(*) FROM tbl_accounts WHERE UPPER(u_role)='ADMIN'";

        try (PreparedStatement pst = conn.prepareStatement(countSql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next() && rs.getInt(1) == 0) {
                String insertSql = "INSERT INTO tbl_accounts "
                        + "(u_fname, u_lname, u_email, u_password, u_role, u_status) "
                        + "VALUES (?, ?, ?, ?, ?, ?)";

                try (PreparedStatement insertPst = conn.prepareStatement(insertSql)) {
                    insertPst.setString(1, "System");
                    insertPst.setString(2, "Administrator");
                    insertPst.setString(3, "admin@staycation.local");
                    insertPst.setString(4, hashPassword("admin123"));
                    insertPst.setString(5, "ADMIN");
                    insertPst.setString(6, "Approved");
                    insertPst.executeUpdate();
                }
            }
        }
    }

    public static boolean isSha256Hash(String value) {
        return value != null && value.matches("^[a-fA-F0-9]{64}$");
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
             "SELECT COUNT(*) FROM tbl_accounts WHERE UPPER(u_role)='USER' AND UPPER(u_status)='APPROVED'");
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
