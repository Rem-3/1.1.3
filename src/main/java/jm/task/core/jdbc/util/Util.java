package jm.task.core.jdbc.util;


import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Util {
    // реализуйте настройку соеденения с БД
    private static final String USER_KEY = "db.user";
    private static final String PASSWORD_KEY = "db.password";
    private static final String URL_KEY = "db.url";


    public Util() {
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

    public static Session HibernateStarter() {
        var configuration = new Configuration().addAnnotatedClass(User.class);
        Properties settings = new Properties();
        settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        settings.put(Environment.URL, "jdbc:mysql://localhost:3307/company");
        settings.put(Environment.USER, "root");
        settings.put(Environment.PASS, "123123");
        settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
        settings.put("hibernate.current_session_context_class", "thread");
        configuration.setProperties(settings);

        var sessionFactory = configuration.buildSessionFactory();
        var session = sessionFactory.getCurrentSession();
        return session;
    }
}