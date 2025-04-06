package com.study.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * 커넥션 풀
 */
public class DBCPConnection {

    /**
     * connection 연결
     *
     * @return Connection
     */
    public Connection getConnection() {
        Connection connection = null;
        String jndiName = "jdbc/mysql";

        try {
            Context initContext = (Context) (new InitialContext()).lookup("java:comp/env");
            DataSource dataSource = (DataSource) initContext.lookup(jndiName);
            connection = dataSource.getConnection();
        } catch (SQLException | NamingException sqlException) {
            sqlException.printStackTrace();
        }

        return connection;
    }

    /**
     * 리소스 연결 해제
     *
     * @param connection Connection
     * @param pstmt PreparedStatement
     * @param resultSet ResultSet
     */
    public void closeConnections(Connection connection, PreparedStatement pstmt, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }

        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }

    }
}
