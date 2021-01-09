package pl.kashp;

import java.sql.*;

public class DBUtils {

    private static final String DB_URL= "jdbc:mysql://localhost:3306/%s?useSSL=false&characterEncoding=utf8";
    private static final String DB_USER= "root";
    private static final String DB_PASS= "coderslab";
    private static final String DELETE_QUERY = "DELETE FROM tableName WHERE id = ?";


    public static Connection connect(String dbName) throws SQLException {
        String urlWithDbName = String.format(DB_URL, dbName);
        return DriverManager.getConnection(urlWithDbName, DB_USER, DB_PASS);
    }

    public static void insert(Connection conn, String query, String... params) {
        try ( PreparedStatement statement = conn.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void printData(Connection conn, String query, String... columnNames) throws SQLException {

        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                for (String param : columnNames) {
                    System.out.println(resultSet.getString(param));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void remove(Connection conn, String tableName, int id) {
        try (PreparedStatement statement =
                     conn.prepareStatement(DELETE_QUERY.replace("tableName", tableName));) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
