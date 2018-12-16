package data.util;

import util.BreakoutException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnection {

    private static String URL = "";
    private static String UID = "";
    private static String PWD = "";

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private MySqlConnection() {

    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, UID, PWD);
        } catch (SQLException e) {
            throw new BreakoutException("Couldn't connect to DB", e);
        }
    }
}
