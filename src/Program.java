import java.sql.*;
import DBManager.*;

import javax.swing.*;

public class Program {

    private static String url = "jdbc:sqlserver://localhost:1433;database=Supermarket;integratedSecurity=true;";
    //private static String userName = "sa";
    //private static String password = "myPassword";

    /**
     * @param args the command line arguments
     */

    public static String getLookAndFeelClassName(String nameSnippet) {
        UIManager.LookAndFeelInfo[] plafs = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo info : plafs) {
            if (info.getName().contains(nameSnippet)) {
                return info.getClassName();
            }
        }
        return null;
    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        /***SET GUI MAC OSX ***/
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "WikiTeX");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        /***SET GUI WINDOWS ***/
        /*String className = getLookAndFeelClassName("Nimbus");
        UIManager.setLookAndFeel(className);*/

        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost/supermarket?user=hoazell41195&password=hoa49511";

        MenuFrame menu = new MenuFrame(driver, url);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.setVisible(true);
    }
}