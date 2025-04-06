package com.study.DAO;

import com.study.DTO.CommentDTO;
import com.study.connection.DBCPConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Comment DAO
 */
public class CommentDAO {

    /**
     * 게시물에 있는 댓글 리스트 DB SELECT
     *
     * @param boardId 게시물 PK
     * @return 댓글 리스트
     * @throws Exception
     */
    public List<CommentDTO> selectCommentListByBoardId(int boardId) throws Exception {
        DBCPConnection dbcpConnection = new DBCPConnection();
        Connection connection = dbcpConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        List<CommentDTO> commentList = new ArrayList<>();
        int idx = 1;

        String sql = "SELECT * FROM tb_comment WHERE board_id = ? ORDER BY created_at ASC";

        pstmt = connection.prepareStatement(sql);
        pstmt.setInt(idx++, boardId);
        resultSet = pstmt.executeQuery();

        while (resultSet.next()) {
            CommentDTO comment = CommentDTO.builder()
                    .comment(resultSet.getString("comment"))
                    .createdAt(resultSet.getTimestamp("created_at"))
                    .build();

            commentList.add(comment);
        }

        dbcpConnection.closeConnections(connection, pstmt, resultSet);

        return commentList;
    }

    /**
     * 댓글 등록
     *
     * @param comment 댓글
     * @throws Exception
     */
    public void insertComment(CommentDTO comment) throws Exception {
        DBCPConnection dbcpConnection = new DBCPConnection();
        Connection connection = dbcpConnection.getConnection();
        PreparedStatement pstmt = null;
        int idx = 1;

        String sql = "INSERT INTO tb_comment (board_id,comment) values (?,?)";

        pstmt = connection.prepareStatement(sql);
        pstmt.setInt(idx++, comment.getBoardId());
        pstmt.setString(idx++, comment.getComment());
        pstmt.executeUpdate();

        dbcpConnection.closeConnections(connection, pstmt, null);
    }

    /**
     * 게시물 내 모든 댓글 삭제
     *
     * @param boardId boardPk
     * @throws Exception
     */
    public void deleteCommentsByBoardId(int boardId) throws Exception {
        DBCPConnection dbcpConnection = new DBCPConnection();
        Connection connection = dbcpConnection.getConnection();
        PreparedStatement pstmt = null;
        int idx = 1;

        String sql = "DELETE FROM tb_comment WHERE board_id = ?";

        pstmt = connection.prepareStatement(sql);
        pstmt.setInt(idx++, boardId);
        pstmt.executeUpdate();

        dbcpConnection.closeConnections(connection, pstmt, null);
    }
}
