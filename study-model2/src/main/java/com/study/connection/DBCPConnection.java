package com.study.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBCPConnection {

    public Connection getConnection() {
        Connection connection = null;
        String jndiName = "jdbc/mysql";

        try {
            Context initContext = (Context)(new InitialContext()).lookup("java:comp/env");
            DataSource dataSource = (DataSource)initContext.lookup(jndiName);
            connection = dataSource.getConnection();
        } catch (SQLException var5) {
            var5.printStackTrace();
        } catch (NamingException var6) {
            var6.printStackTrace();
        }

        return connection;
    }

    public void closeConnections(Connection connection, PreparedStatement pstmt, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException var7) {
                var7.printStackTrace();
            }
        }

        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException var6) {
                var6.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException var5) {
                var5.printStackTrace();
            }
        }

    }
}
