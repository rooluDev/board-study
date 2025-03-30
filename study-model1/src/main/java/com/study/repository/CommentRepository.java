package com.study.repository;

import com.study.connection.JDBCConnection;
import com.study.dto.Comment;
import lombok.Getter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 댓글 DB 레포
 */
public class CommentRepository {

    @Getter
    public static final CommentRepository instance = new CommentRepository();

    private CommentRepository() {
    }

    /**
     * 게시물에 있는 댓글 리스트 DB SELECT
     *
     * @param boardId 게시물 PK
     * @return 댓글 리스트
     * @throws Exception
     */
    public List<Comment> selectCommentListByBoardId(int boardId) throws Exception {
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        List<Comment> commentList = new ArrayList<>();
        int idx = 1;

        String sql = "SELECT * FROM tb_comment WHERE board_id = ? ORDER BY created_at ASC";

        pstmt = connection.prepareStatement(sql);
        pstmt.setInt(idx++, boardId);
        resultSet = pstmt.executeQuery();

        while (resultSet.next()) {
            Comment comment = Comment.builder()
                    .comment(resultSet.getString("comment"))
                    .createdAt(resultSet.getTimestamp("created_at"))
                    .build();

            commentList.add(comment);
        }

        jdbcConnection.closeConnections(connection, pstmt, resultSet);

        return commentList;
    }

    /**
     * 댓글 등록
     *
     * @param comment 댓글
     * @throws Exception
     */
    public void insertComment(Comment comment) throws Exception {
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt = null;
        int idx = 1;

        String sql = "INSERT INTO tb_comment (board_id,comment) values (?,?)";

        pstmt = connection.prepareStatement(sql);
        pstmt.setInt(idx++, comment.getBoardId());
        pstmt.setString(idx++, comment.getComment());
        pstmt.executeUpdate();

        jdbcConnection.closeConnections(connection, pstmt, null);
    }

    /**
     * 게시물 내 모든 댓글 삭제
     *
     * @param boardId boardPk
     * @throws Exception
     */
    public void deleteCommentsByBoardId(int boardId) throws Exception {
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt = null;
        int idx = 1;

        String sql = "DELETE FROM tb_comment WHERE board_id = ?";

        pstmt = connection.prepareStatement(sql);
        pstmt.setInt(idx++, boardId);
        pstmt.executeUpdate();

        jdbcConnection.closeConnections(connection, pstmt, null);
    }
}
