package com.study.DAO;

import com.study.DTO.CategoryDTO;
import com.study.connection.DBCPConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Category DAO
 */
public class CategoryDAO {

    /**
     * 카테고리 리스트 가져오기
     *
     * @return 카테고리 리스트
     * @throws Exception
     */
    public List<CategoryDTO> getCategoryList() throws Exception{
        DBCPConnection dbcpConnection = new DBCPConnection();
        Connection connection = dbcpConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;

        List<CategoryDTO> categoryList = new ArrayList<>();

        String sql = "SELECT * FROM tb_category";
        pstmt = connection.prepareStatement(sql);
        resultSet = pstmt.executeQuery();

        while (resultSet.next()) {
            CategoryDTO category = CategoryDTO.builder()
                    .categoryId(resultSet.getInt("category_id"))
                    .categoryName(resultSet.getString("category_name"))
                    .build();

            categoryList.add(category);
        }

        dbcpConnection.closeConnections(connection, pstmt, resultSet);

        return categoryList;
    }
}
