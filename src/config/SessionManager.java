package config;

import Main.login;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class SessionManager {

    public static boolean checkLogin(JFrame frame){

        if(!Session.isLoggedIn()){
            JOptionPane.showMessageDialog(frame,"You must login first!");
            new login().setVisible(true);
            frame.dispose();
            return false;
        }

        return true;
    }

    public static boolean checkAdmin(JFrame frame){

        if(!Session.isLoggedIn() || !Session.isAdmin()){
            JOptionPane.showMessageDialog(frame,"Admin access only!");
            new login().setVisible(true);
            frame.dispose();
            return false;
        }

        return true;
    }

    public static boolean checkUser(JFrame frame){

        if(!Session.isLoggedIn() || !Session.isUser()){
            JOptionPane.showMessageDialog(frame,"User access only!");
            new login().setVisible(true);
            frame.dispose();
            return false;
        }

        return true;
        }
}