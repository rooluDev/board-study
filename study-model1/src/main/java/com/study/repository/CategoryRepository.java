package com.study.repository;

import com.study.connection.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 이 클래스는 어쩌구
 */
public class CategoryRepository {

    /**
     * 이 메서드는 어쩌구
     * @param boardId - 보드 식별자
     * @param category - 카테고리 아이디
     * @throws Exception
     */
    public void insertCategory(int boardId, String category) throws Exception {

        /*
         *
         */
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt = null;

        String sql = "INSERT INTO tb_category (board_id, category_name) VALUES(?,?)";
        pstmt = connection.prepareStatement(sql);

        pstmt.setInt(1, boardId);
        pstmt.setString(2, category);
        pstmt.executeUpdate();
        connection.close();
    }

    /**
     *
     * @param boardId
     * @return
     * @throws Exception
     */
    public String findByBoardId(int boardId) throws Exception {
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        String categoryName = null;

        String sql = "SELECT category_name from tb_category WHERE board_id = ?";
        pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, boardId);
        resultSet = pstmt.executeQuery();
        while (resultSet.next()) {
            categoryName = resultSet.getString("category_name");
        }
        connection.close();
        return categoryName;
    }
}
