package com.study.repository;

import com.study.connection.JDBCConnection;
import com.study.dto.Category;
import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 카테고리 DB 레포
 */
public class CategoryRepository {

    @Getter
    private static final CategoryRepository instance = new CategoryRepository();

    private CategoryRepository(){}

    /**
     * 카테고리 리스트 가져오기
     *
     * @return 카테고리 리스트
     * @throws Exception
     */
    public List<Category> getCategoryList() throws Exception{
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;

        List<Category> categoryList = new ArrayList<>();

        String sql = "SELECT * FROM tb_category";
        pstmt = connection.prepareStatement(sql);
        resultSet = pstmt.executeQuery();

        while (resultSet.next()) {
            Category category = Category.builder()
                    .categoryId(resultSet.getInt("category_id"))
                    .categoryName(resultSet.getString("category_name"))
                    .build();

            categoryList.add(category);
        }

        jdbcConnection.closeConnections(connection, pstmt, resultSet);

        return categoryList;
    }
}
