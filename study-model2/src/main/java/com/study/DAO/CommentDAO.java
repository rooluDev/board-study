package com.study.DAO;

import com.study.DTO.CommentDTO;
import com.study.connection.DBCPConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {

    public int insertComment(CommentDTO comment) {
        DBCPConnection dbcpConnection = new DBCPConnection();
        Connection connection = dbcpConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        int generatedKey = 0;
        String sql = "INSERT INTO tb_comment (board_id,comment,created_at) VALUES (?,?,NOW());";
        int idx = 1;

        try {
            pstmt = connection.prepareStatement(sql, 1);
            pstmt.setInt(idx++, comment.getBoardId());
            pstmt.setString(idx++, comment.getComment());
            pstmt.executeUpdate();

            for(resultSet = pstmt.getGeneratedKeys(); resultSet.next(); generatedKey = resultSet.getInt(1)) {
            }
        } catch (SQLException var13) {
            var13.printStackTrace();
        } finally {
            dbcpConnection.closeConnections(connection, pstmt, resultSet);
        }

        return generatedKey;
    }

    public List<CommentDTO> getCommentList(int boardId) {
        DBCPConnection dbcpConnection = new DBCPConnection();
        Connection connection = dbcpConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        List<CommentDTO> commentList = new ArrayList();
        int idx = 1;
        String sql = "SELECT * FROM tb_comment WHERE board_id = ?;";

        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(idx++, boardId);
            resultSet = pstmt.executeQuery();

            while(resultSet.next()) {
                CommentDTO comment = new CommentDTO();
                comment.setCommentId(resultSet.getInt("comment_id"));
                comment.setBoardId(resultSet.getInt("board_id"));
                comment.setComment(resultSet.getString("comment"));
                comment.setCreatedAt(resultSet.getTimestamp("created_at"));
                commentList.add(comment);
            }
        } catch (SQLException var13) {
            var13.printStackTrace();
        } finally {
            dbcpConnection.closeConnections(connection, pstmt, resultSet);
        }

        return commentList;
    }
}
