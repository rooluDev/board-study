package com.study.connection;

import java.sql.*;

/**
 * JDBC 커넥터
 */
public class JDBCConnection {

    static final String DB_URL = "jdbc:mysql://localhost:3306/board-study";
    static final String USER = ;
    static final String PASS = ;

    public Connection getConnection() throws Exception {

        Connection conn = null;
        Statement stmt = null;

        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    /**
     * DB연결 리소스 끊기
     *
     * @param connection connection
     * @param pstmt      pstmt
     * @param resultSet  resultSet
     */
    public void closeConnections(Connection connection, PreparedStatement pstmt, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
