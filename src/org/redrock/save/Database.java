package org.redrock.save;

import java.io.UnsupportedEncodingException;
import java.sql.*;

public class Database {//useUnicode=true&characterEncoding=UTF-8&
    private static String url = "jdbc:mysql://localhost:3306/student?zeroDateTimeBehavior=convertToNull&useSSL=false&useUnicode=true&characterEncoding=UTF8";
    private static String username = "root";
    private static String password = "";
    private static Connection conn;
    private static Database database;

    private Database() {
    }

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Database getInstance() {
        if (database == null) {
            synchronized (Database.class) {
                if (database == null) {
                    database = new Database();
                }
            }
        }
        return database;
    }

    public Connection getConn() {
        if (Database.conn == null) {
            synchronized (Database.class) {
                try {
                    conn = DriverManager.getConnection(url, username, password);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return conn;
    }

    public void closeConnection(ResultSet rs, Statement statement, Connection con) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws SQLException, UnsupportedEncodingException {
        Connection connection = getInstance().getConn();
        String sql = "select * FROM student WHERE stuId LIKE '%2015%'";
//        String sql = "SELECT * From student WHERE stuId like \"%2013%\" and name like \"%王%\"  ORDER BY id desc";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
//        System.out.println(resultSet.next());/
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            System.out.println(new String(name.getBytes("UTF-8")));
        }
    }
}