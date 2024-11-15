package com.study.DAO;

import com.study.DTO.CategoryDTO;
import com.study.connection.DBCPConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public List<CategoryDTO> getCategoryList() {
        List<CategoryDTO> categoryList = new ArrayList();
        DBCPConnection dbcpConnection = new DBCPConnection();
        Connection connection = dbcpConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        String sql = "SELECT * FROM tb_category";

        try {
            pstmt = connection.prepareStatement(sql);
            resultSet = pstmt.executeQuery();

            while(resultSet.next()) {
                CategoryDTO category = new CategoryDTO();
                category.setCategoryId(resultSet.getInt("category_id"));
                category.setCategoryName(resultSet.getString("category_name"));
                categoryList.add(category);
            }
        } catch (SQLException var11) {
            var11.printStackTrace();
        } finally {
            dbcpConnection.closeConnections(connection, pstmt, resultSet);
        }

        return categoryList;
    }

    public String findById(int categoryId) {
        DBCPConnection dbcpConnection = new DBCPConnection();
        Connection connection = dbcpConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        CategoryDTO category = new CategoryDTO();
        String sql = "SELECT category_name FROM tb_category WHERE category_id = ?";
        int idx = 1;

        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(idx++, categoryId);
            resultSet = pstmt.executeQuery();

            while(resultSet.next()) {
                category.setCategoryId(categoryId);
                category.setCategoryName(resultSet.getString("category_name"));
            }
        } catch (SQLException var13) {
            var13.printStackTrace();
        } finally {
            dbcpConnection.closeConnections(connection, pstmt, resultSet);
        }

        return category.getCategoryName();
    }
}
