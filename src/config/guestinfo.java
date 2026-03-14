/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

/**
 *
 * @author Archie
 */
public class guestinfo {
    private static int guestId;
    private static String guestName;
    private static int cabinId;
    private static String cabinName;
    
    public static int getGuestId() { return guestId; }
    public static void setGuestId(int id) { guestId = id; }
    
    public static String getGuestName() { return guestName; }
    public static void setGuestName(String name) { guestName = name; }
    
    public static int getCabinId() { return cabinId; }
    public static void setCabinId(int id) { cabinId = id; }
    
    public static String getCabinName() { return cabinName; }
    public static void setCabinName(String name) { cabinName = name; }
    
    public static void clear() {
        guestId = 0;
        guestName = null;
        cabinId = 0;
        cabinName = null;
    }
}
