package pl.kashp;

import pl.kashp.dao.UserDao;
import pl.kashp.entity.User;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        final UserDao userDao = new UserDao();
//        userDao.create(user1);
//        User user1 = userDao.read(3);
//        userDao.update(user1);
//        userDao.delete(3);
        System.out.println(userDao.read(3));
    }
}
