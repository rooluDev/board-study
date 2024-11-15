package com.study.repository;

import com.study.connection.JDBCConnection;
import com.study.domain.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentRepository {
    public List<Comment> commentList(int boardId) throws Exception{
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        List<Comment> commentList = new ArrayList<>();
        String sql = "select * from tb_comment where board_id = ? order by created_at asc";
        pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, boardId);
        resultSet = pstmt.executeQuery();
        while (resultSet.next()) {
            Comment comment = new Comment();
            comment.setBoardId(resultSet.getInt("board_id"));
            comment.setComment(resultSet.getString("comment"));
            comment.setCommentId(resultSet.getInt("comment_id"));
            comment.setCreatedAt(resultSet.getTimestamp("created_at"));
            commentList.add(comment);
        }
        connection.close();
        return commentList;
    }

    public int insertBoard(String comment,int boardId) throws Exception{
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        int generatedKey = 0;

        Timestamp createdAt = new Timestamp(new Date().getTime());

        String sql = "insert into tb_comment (board_id,comment,created_at) values (?,?,?)";
        pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        pstmt.setInt(1, boardId);
        pstmt.setString(2, comment);
        pstmt.setTimestamp(3, createdAt);
        pstmt.executeUpdate();
        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        while(generatedKeys.next()){
            generatedKey = generatedKeys.getInt(1);
        }
        connection.close();
        return generatedKey;
    }
}
