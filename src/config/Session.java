package config;

public class Session {

    private static int userId;
    private static String name;
    private static String email;
    private static String role;
    private static String status;

    // ✅ SET SESSION
    public static void setSession(int id,
                                  String n,
                                  String e,
                                  String r,
                                  String s) {

        userId = id;
        name = n;
        email = e;
        role = r;
        status = s;
    }

    // ✅ GETTERS
    public static int getUserId() {
        return userId;
    }

    public static String getName() {
        return name;
    }

    public static String getEmail() {
        return email;
    }

    public static String getRole() {
        return role;
    }

    public static String getStatus() {
        return status;
    }

    // ✅ LOGOUT
    public static void logout() {
        userId = 0;
        name = null;
        email = null;
        role = null;
        status = null;
    }

public static boolean isLoggedIn() {
    return userId != 0;
}

public static boolean isAdmin() {
    return role != null && role.equalsIgnoreCase("ADMIN");
}

public static boolean isUser() {
    return role != null && role.equalsIgnoreCase("USER");
}









}








