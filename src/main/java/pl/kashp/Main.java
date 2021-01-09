package pl.kashp;

import pl.kashp.dao.UserDao;
import pl.kashp.entity.User;

import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws SQLException {
        final UserDao userDao = new UserDao();
        ArrayList<User> users =  userDao.findAll();
        users.forEach(System.out::println);
    }
}
