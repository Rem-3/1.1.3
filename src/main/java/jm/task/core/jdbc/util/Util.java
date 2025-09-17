package jm.task.core.jdbc.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Util {
    // реализуйте настройку соеденения с БД
    private static final String USER_KEY = "db.user";
    private static final String PASSWORD_KEY = "db.password";
    private static final String URL_KEY = "db.url";

    private Util() {
    }
    public static Connection openConnection() {
        try {
           return DriverManager.getConnection(
                   PropertiesUtil.getProperty(URL_KEY),
                   PropertiesUtil.getProperty(USER_KEY),
                   PropertiesUtil.getProperty(PASSWORD_KEY)
           );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
