package com.study.DAO;

import com.study.DTO.BoardDTO;
import com.study.condition.EditCondition;
import com.study.condition.SearchCondition;
import com.study.connection.DBCPConnection;
import com.study.utils.IntegerUtils;
import com.study.utils.StringUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BoardDAO {

    public int insertBoard(BoardDTO board) {
        DBCPConnection dbcpConnection = new DBCPConnection();
        Connection connection = dbcpConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        int boardId = 0;
        String sql = "INSERT INTO tb_board (category_id,title,content,user_name,password,created_at) VALUES (?,?,?,?,?,NOW())";

        try {
            int idx = 1;
            pstmt = connection.prepareStatement(sql, 1);
            pstmt.setInt(idx++, board.getCategoryId());
            pstmt.setString(idx++, board.getTitle());
            pstmt.setString(idx++, board.getContent());
            pstmt.setString(idx++, board.getUserName());
            pstmt.setString(idx++, board.getPassword());
            pstmt.executeUpdate();

            for(resultSet = pstmt.getGeneratedKeys(); resultSet.next(); boardId = resultSet.getInt(1)) {
            }
        } catch (SQLException var12) {
            var12.printStackTrace();
        } finally {
            dbcpConnection.closeConnections(connection, pstmt, resultSet);
        }

        return boardId;
    }

    public List<BoardDTO> selectBoard(SearchCondition searchCondition, int pageSize, int startRow) {
        DBCPConnection dbcpConnection = new DBCPConnection();
        Connection connection = dbcpConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        List<BoardDTO> boardList = new ArrayList();
        int idx = 1;
        String sql = "SELECT * FROM tb_board WHERE created_at BETWEEN ? AND ?";
        if (!StringUtils.isNull(searchCondition.getSearchText())) {
            sql = sql + " AND (title LIKE ? OR user_name LIKE ? OR content LIKE ?)";
        }

        if (IntegerUtils.isUnsigned(searchCondition.getCategoryId())) {
            sql = sql + " AND category_id = ?";
        }

        sql = sql + " ORDER BY created_at DESC LIMIT ? OFFSET ?";

        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setTimestamp(idx++, searchCondition.getStartDate());
            pstmt.setTimestamp(idx++, searchCondition.getEndDate());
            if (!StringUtils.isNull(searchCondition.getSearchText())) {
                pstmt.setString(idx++, "%" + searchCondition.getSearchText() + "%");
                pstmt.setString(idx++, "%" + searchCondition.getSearchText() + "%");
                pstmt.setString(idx++, "%" + searchCondition.getSearchText() + "%");
            }

            if (IntegerUtils.isUnsigned(searchCondition.getCategoryId())) {
                pstmt.setInt(idx++, searchCondition.getCategoryId());
            }

            pstmt.setInt(idx++, pageSize);
            pstmt.setInt(idx++, startRow);
            resultSet = pstmt.executeQuery();
            boardList = this.getBoardList(resultSet);
        } catch (SQLException var15) {
            var15.printStackTrace();
        } finally {
            dbcpConnection.closeConnections(connection, pstmt, resultSet);
        }

        return (List)boardList;
    }

    public BoardDTO findById(int boardId) {
        DBCPConnection dbcpConnection = new DBCPConnection();
        Connection connection = dbcpConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        List<BoardDTO> boardList = new ArrayList();
        String sql = "SELECT * FROM tb_board WHERE board_id = ?;";

        try {
            pstmt = connection.prepareStatement(sql);
            int idx = 1;
            pstmt.setInt(idx++, boardId);
            resultSet = pstmt.executeQuery();
            boardList = this.getBoardList(resultSet);
        } catch (SQLException var12) {
            var12.printStackTrace();
        } finally {
            dbcpConnection.closeConnections(connection, pstmt, (ResultSet)null);
        }

        return (BoardDTO)((List)boardList).get(0);
    }

    public void deleteById(int boardId) {
        DBCPConnection dbcpConnection = new DBCPConnection();
        Connection connection = dbcpConnection.getConnection();
        PreparedStatement pstmt = null;
        String sql = "DELETE FROM tb_board WHERE board_id = ?";

        try {
            pstmt = connection.prepareStatement(sql);
            int idx = 1;
            pstmt.setInt(idx++, boardId);
            pstmt.execute();
        } catch (SQLException var10) {
            var10.printStackTrace();
        } finally {
            dbcpConnection.closeConnections(connection, pstmt, (ResultSet)null);
        }

    }

    public void plusViewById(int boardId) {
        DBCPConnection dbcpConnection = new DBCPConnection();
        Connection connection = dbcpConnection.getConnection();
        PreparedStatement pstmt = null;
        int idx = 1;
        String sql = "UPDATE tb_board SET views = views + 1 WHERE board_id = ?";

        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(idx++, boardId);
            pstmt.executeUpdate();
        } catch (SQLException var11) {
            var11.printStackTrace();
        } finally {
            dbcpConnection.closeConnections(connection, pstmt, (ResultSet)null);
        }

    }

    public int countBoard(SearchCondition searchCondition) {
        DBCPConnection dbcpConnection = new DBCPConnection();
        Connection connection = dbcpConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        int rowCount = 0;
        int idx = 1;
        String sql = "SELECT (SELECT COUNT(*) FROM tb_board WHERE created_at BETWEEN ? AND ?";
        if (!StringUtils.isNull(searchCondition.getSearchText())) {
            sql = sql + " AND (title LIKE ? OR user_name LIKE ? OR content LIKE ?)";
        }

        if (IntegerUtils.isUnsigned(searchCondition.getCategoryId())) {
            sql = sql + " AND category_id = ?";
        }

        sql = sql + ") AS row_count";

        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setTimestamp(idx++, searchCondition.getStartDate());
            pstmt.setTimestamp(idx++, searchCondition.getEndDate());
            if (!StringUtils.isNull(searchCondition.getSearchText())) {
                pstmt.setString(idx++, "%" + searchCondition.getSearchText() + "%");
                pstmt.setString(idx++, "%" + searchCondition.getSearchText() + "%");
                pstmt.setString(idx++, "%" + searchCondition.getSearchText() + "%");
            }

            if (IntegerUtils.isUnsigned(searchCondition.getCategoryId())) {
                pstmt.setInt(idx++, searchCondition.getCategoryId());
            }

            for(resultSet = pstmt.executeQuery(); resultSet.next(); rowCount = resultSet.getInt("row_count")) {
            }
        } catch (SQLException var13) {
            var13.printStackTrace();
        } finally {
            dbcpConnection.closeConnections(connection, pstmt, resultSet);
        }

        return rowCount;
    }

    public void editBoard(int boardId, BoardDTO board, EditCondition editCondition) {
        DBCPConnection dbcpConnection = new DBCPConnection();
        Connection connection = dbcpConnection.getConnection();
        PreparedStatement pstmt = null;
        String sql = "UPDATE tb_board SET ";
        int idx = 1;
        if (!board.getTitle().equals(editCondition.getTitle())) {
            sql = sql + "title = ?,";
        }

        if (!board.getUserName().equals(editCondition.getUserName())) {
            sql = sql + "user_name = ?,";
        }

        if (!board.getContent().equals(editCondition.getContent())) {
            sql = sql + "content = ?,";
        }

        if (sql.endsWith(",")) {
            sql = sql.substring(0, sql.length() - 1);
        }

        sql = sql + ", edited_at = NOW() WHERE board_id = ?";

        try {
            pstmt = connection.prepareStatement(sql);
            if (!board.getTitle().equals(editCondition.getTitle())) {
                pstmt.setString(idx++, editCondition.getTitle());
            }

            if (!board.getUserName().equals(editCondition.getUserName())) {
                pstmt.setString(idx++, editCondition.getUserName());
            }

            if (!board.getContent().equals(editCondition.getContent())) {
                pstmt.setString(idx++, editCondition.getContent());
            }

            pstmt.setInt(idx++, boardId);
            pstmt.executeUpdate();
        } catch (SQLException var13) {
            var13.printStackTrace();
        } finally {
            dbcpConnection.closeConnections(connection, pstmt, (ResultSet)null);
        }

    }

    private List<BoardDTO> getBoardList(ResultSet resultSet) throws SQLException {
        List<BoardDTO> boardList = new ArrayList();

        while(resultSet.next()) {
            BoardDTO board = new BoardDTO();
            board.setBoardId(resultSet.getInt("board_id"));
            board.setCategoryId(resultSet.getInt("category_id"));
            board.setTitle(resultSet.getString("title"));
            board.setViews(resultSet.getInt("views"));
            board.setCreatedAt(resultSet.getTimestamp("created_at"));
            board.setEditedAt(resultSet.getTimestamp("edited_at"));
            board.setContent(resultSet.getString("content"));
            board.setUserName(resultSet.getString("user_name"));
            board.setPassword(resultSet.getString("password"));
            boardList.add(board);
        }

        return boardList;
    }
}
