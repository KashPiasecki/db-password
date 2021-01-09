package pl.kashp.dao;

import org.mindrot.jbcrypt.BCrypt;
import pl.kashp.DBUtils;
import pl.kashp.entity.User;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;

public class UserDao {
    private static final String DB_NAME = "workshop2";
    private static final String CREATE_USER_QUERY = "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?;";
    private static final String READ_USER_QUERY = "SELECT * FROM users WHERE id = '%s'";
    private static final String READALL_USER_QUERY = "SELECT * FROM users";

    public User create(User user) {
        try (Connection conn = DBUtils.connect(DB_NAME)) {
            PreparedStatement statement =
                    conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();
            //Pobieramy wstawiony do bazy identyfikator, a następnie ustawiamy id obiektu user.
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User read(int userId) throws SQLException {
        String input = String.format(READ_USER_QUERY, userId);
        try (Connection conn = DBUtils.connect(DB_NAME)) {
            ArrayList<String> list = DBUtils.parseIntoUser(conn, input, "id", "email", "username", "password");
            return new User(Integer.parseInt(list.get(0)), list.get(1), list.get(2), list.get(3));
        } catch (Exception e) {
            return null;
        }
    }

    public void update(User user) {
        try (Connection conn = DBUtils.connect(DB_NAME)) {
            PreparedStatement statement =
                    conn.prepareStatement(UPDATE_USER_QUERY);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.setString(4, Integer.toString(user.getId()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection conn = DBUtils.connect(DB_NAME)) {
            DBUtils.remove(conn, "users", id);
        }
    }

    public ArrayList<User> findAll() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        try (Connection conn = DBUtils.connect(DB_NAME)) {
            var list = DBUtils.parseIntoUser(conn, READALL_USER_QUERY, "id", "username", "email", "password");
            int userParamCounter = list.size();
            for (int i = 0; i < userParamCounter; i += 4) {
                int id = Integer.parseInt(list.get(i));
                String name = list.get(i + 1);
                String email = list.get(i + 2);
                String password = list.get(i + 3);
                users.add(new User(id, name, email, password));
            }
        }
        return users;
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

}

