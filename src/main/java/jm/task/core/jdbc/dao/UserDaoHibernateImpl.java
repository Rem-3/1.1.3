package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        Util userDaoHibernate = new Util();
        Session session = userDaoHibernate.HibernateStarter();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            String createTableSQL = """
                    CREATE TABLE IF NOT EXISTS user (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         name VARCHAR(255),
                         last_name VARCHAR(255),
                         age TINYINT NOT NULL
                     );
                     """;
            NativeQuery<?> query = session.createNativeQuery(createTableSQL);
            query.executeUpdate();
            transaction.commit();
            System.out.println("Таблица 'users' успешно создана");
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Ошибка при создании таблицы: " + e.getMessage());
            throw new RuntimeException("Failed to create users table", e);
        }
    }

    @Override
    public void dropUsersTable() {
        Util userDaoHibernate = new Util();
        Session session = userDaoHibernate.HibernateStarter();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            String dropTable = """
                    DROP TABLE IF EXISTS user;
                     """;
            NativeQuery<?> query = session.createNativeQuery(dropTable);
            query.executeUpdate();
            transaction.commit();
            System.out.println("Таблица 'users' успешно удалена");
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Ошибка при удалении таблицы: " + e.getMessage());
            throw new RuntimeException("Failed to drop users table", e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Util userDaoHibernate = new Util();
        Session session = userDaoHibernate.HibernateStarter();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            transaction.commit();
            System.out.println("Пользователь успешно сохранён");

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Ошибка при сохранении пользователя: " + e.getMessage());
            throw new RuntimeException("Failed to save user", e);
        }
    }

    @Override
    public void removeUserById(long id) {
        Util userDaoHibernate = new Util();
        Session session = userDaoHibernate.HibernateStarter();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(session.get(User.class, id));
            transaction.commit();
            System.out.println("Пользователь успешно удалён");
        } catch (Exception e) {
            System.err.println("Ошибка при удалении пользователя: " + e.getMessage());
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        Util userDaoHibernate = new Util();
        Session session = userDaoHibernate.HibernateStarter();
        Transaction transaction = null;
        List<User> users = null;
        try {
            transaction = session.beginTransaction();
            users = session.createQuery("from User").list();
            transaction.commit();
            System.out.println("Пользователь успешно сохранён");
        } catch (Exception e) {
            System.err.println("Ошибка при запросе пользователей: " + e.getMessage());
            throw new RuntimeException("Failed to post users", e);
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        dropUsersTable();
        createUsersTable();
    }
}
