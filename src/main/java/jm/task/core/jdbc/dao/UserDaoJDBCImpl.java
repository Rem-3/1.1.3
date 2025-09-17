package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private String e;

    public UserDaoJDBCImpl() {

    }

    public void loadSQL(String sql) {
        try (var connection = Util.openConnection();
             var statement = connection.createStatement()) {
            var executeResult = statement.execute(sql);
//            System.out.println(executeResult);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createUsersTable() {
        //language=MySQL
        String sql = """
                CREATE TABLE IF NOT EXISTS user (
                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                     name VARCHAR(255),
                     last_name VARCHAR(255),
                     age TINYINT NOT NULL
                 );
                 """;

        loadSQL(sql);
    }

    public void dropUsersTable() {
        //language=MySQL
        String sql = """
                DROP TABLE user;
                """;
        loadSQL(sql);
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = """
                INSERT INTO user (name, last_name, age)
                VALUES (?, ?, ?);
                """;
        try (var connection = Util.openConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("User с именем - " + name + " добавлен в базу данных");
    }

    public void removeUserById(long id) {
        String sql = """
                DELETE FROM user 
                where (id = ?);
                """;
        try (var connection = Util.openConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        String sql = """
                SELECT name, last_name, age 
                FROM user
                """;
        List<User> users = new ArrayList<>();
        try (var connection = Util.openConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            var resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getString("name"),
                        resultSet.getString("last_name"),
                        resultSet.getByte("age")
                ));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void cleanUsersTable() {
        String sql = """
                DELETE FROM user 
                """;
        try (var connection = Util.openConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
