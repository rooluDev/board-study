package com.study.repository;

import com.study.condition.SearchCondition;
import com.study.connection.JDBCConnection;
import com.study.dto.Board;
import lombok.Getter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 게시판 DB 레포
 */
public class BoardRepository {

    @Getter
    private static final BoardRepository instance = new BoardRepository();

    private BoardRepository() {
    }


    /**
     * 검색조건에 따른 게시물 리스트 반환
     *
     * @param searchCondition 검색조건
     * @return 게시물 리스트
     * @throws Exception
     */
    public List<Board> selectBoardList(SearchCondition searchCondition) throws Exception {
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        List<Board> boardList = new ArrayList<>();
        int idx = 1;

        String sql = "SELECT tb_board.board_id, tb_board.title, tb_board.user_name, tb_board.views, tb_board.created_at, tb_board.edited_at, tb_category.category_name, " +
                "(SELECT file_id " +
                "FROM tb_file " +
                "WHERE tb_file.board_id = tb_board.board_id " +
                "LIMIT 1) AS file_id " +
                "FROM tb_board " +
                "JOIN tb_category ON tb_board.category_id = tb_category.category_id " +
                "WHERE tb_board.created_at BETWEEN ? AND ? ";

        if (!searchCondition.getSearchText().equals("")) {
            sql += "AND (tb_board.title LIKE ? OR tb_board.user_name LIKE ? OR tb_board.content LIKE ?) ";
        }
        if (searchCondition.getCategoryId() != -1) {
            sql += "AND tb_board.category_id = ? ";
        }
        sql += "ORDER BY created_at DESC LIMIT 10 OFFSET ?";

        pstmt = connection.prepareStatement(sql);
        pstmt.setTimestamp(idx++, searchCondition.getStartDateTimestamp());
        pstmt.setTimestamp(idx++, searchCondition.getEndDateTimestamp());

        if (!searchCondition.getSearchText().equals("")) {
            pstmt.setString(idx++, "%" + searchCondition.getSearchText() + "%");
            pstmt.setString(idx++, "%" + searchCondition.getSearchText() + "%");
            pstmt.setString(idx++, "%" + searchCondition.getSearchText() + "%");
        }
        if (searchCondition.getCategoryId() != -1) {
            pstmt.setInt(idx++, searchCondition.getCategoryId());
        }
        pstmt.setInt(idx++, searchCondition.getStartRow());

        resultSet = pstmt.executeQuery();

        while (resultSet.next()) {
            Board board = Board.builder()
                    .boardId(resultSet.getInt("board_id"))
                    .title(resultSet.getString("title"))
                    .userName(resultSet.getString("user_name"))
                    .views(resultSet.getInt("views"))
                    .createdAt(resultSet.getTimestamp("created_at"))
                    .editedAt(resultSet.getTimestamp("edited_at"))
                    .categoryName(resultSet.getString("category_name"))
                    .fileId(resultSet.getInt("file_id"))
                    .build();
            boardList.add(board);
        }

        jdbcConnection.closeConnections(connection, pstmt, resultSet);

        return boardList;
    }

    /**
     * 검색조건에 따른 전체 게시물 리스트의 수 반환
     *
     * @param searchCondition 검색조건
     * @return 게시물 수
     * @throws Exception
     */
    public int selectRowCountForBoardList(SearchCondition searchCondition) throws Exception {
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        int idx = 1;
        int countAll = 0;

        String sql = "SELECT COUNT(*) AS row_count " +
                "FROM ( " +
                "  SELECT *, " +
                "    (SELECT file_id " +
                "     FROM tb_file " +
                "     WHERE tb_file.board_id = tb_board.board_id " +
                "     LIMIT 1 " +
                "    ) AS file_id " +
                "  FROM tb_board " +
                "  WHERE tb_board.created_at BETWEEN ? AND ? ";

        if (!searchCondition.getSearchText().equals("")) {
            sql += "AND (tb_board.title LIKE ? OR tb_board.user_name LIKE ? OR tb_board.content LIKE ?) ";
        }
        if (searchCondition.getCategoryId() != -1) {
            sql += "AND tb_board.category_id = ? ";
        }


        sql += ") AS sub";

        pstmt = connection.prepareStatement(sql);
        pstmt.setTimestamp(idx++, searchCondition.getStartDateTimestamp());
        pstmt.setTimestamp(idx++, searchCondition.getEndDateTimestamp());

        if (!searchCondition.getSearchText().equals("")) {
            pstmt.setString(idx++, "%" + searchCondition.getSearchText() + "%");
            pstmt.setString(idx++, "%" + searchCondition.getSearchText() + "%");
            pstmt.setString(idx++, "%" + searchCondition.getSearchText() + "%");
        }
        if (searchCondition.getCategoryId() != -1) {
            pstmt.setInt(idx++, searchCondition.getCategoryId());
        }

        resultSet = pstmt.executeQuery();

        while (resultSet.next()) {
            countAll = resultSet.getInt("row_count");
        }

        jdbcConnection.closeConnections(connection, pstmt, resultSet);

        return countAll;
    }

    /**
     * 단일 게시물 SELECT
     *
     * @param boardId boardId PK
     * @return 게시물
     */
    public Board selectBoardById(int boardId) throws Exception {
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        int idx = 1;

        String sql = "SELECT title,views, created_at, edited_at, user_name, category_name, content " +
                "FROM tb_board " +
                "LEFT JOIN tb_category " +
                "ON tb_board.category_id = tb_category.category_id " +
                "WHERE board_id = ?";

        pstmt = connection.prepareStatement(sql);

        pstmt.setInt(idx++, boardId);
        resultSet = pstmt.executeQuery();

        Board board = null;
        while (resultSet.next()) {
            board = Board.builder()
                    .title(resultSet.getString("title"))
                    .views(resultSet.getInt("views"))
                    .createdAt(resultSet.getTimestamp("created_at"))
                    .editedAt(resultSet.getTimestamp("edited_at"))
                    .userName(resultSet.getString("user_name"))
                    .categoryName(resultSet.getString("category_name"))
                    .content(resultSet.getString("content"))
                    .build();
        }

        jdbcConnection.closeConnections(connection, pstmt, resultSet);

        return board;
    }

    /**
     * tb_board 추가
     *
     * @param board
     * @return
     * @throws Exception
     */
    public int insertBoard(Board board) throws Exception {
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        int idx = 1;
        int generatedKey = 0;

        String sql = "INSERT INTO tb_board (title,content,user_name,password,category_id) VALUES (?,?,?,SHA2(?,256),?)";
        pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        pstmt.setString(idx++, board.getTitle());
        pstmt.setString(idx++, board.getContent());
        pstmt.setString(idx++, board.getUserName());
        pstmt.setString(idx++, board.getPassword());
        pstmt.setInt(idx++, board.getCategoryId());
        pstmt.executeUpdate();

        ResultSet generatedKeys = pstmt.getGeneratedKeys();

        while (generatedKeys.next()) {
            generatedKey = generatedKeys.getInt(1);
        }

        jdbcConnection.closeConnections(connection, pstmt, resultSet);

        return generatedKey;
    }

    /**
     * 게시물 삭제
     *
     * @param boardId pk
     * @throws Exception
     */
    public void deleteById(int boardId) throws Exception {
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt = null;
        int idx = 1;

        String sql = "DELETE FROM tb_board WHERE board_id = ?";
        pstmt = connection.prepareStatement(sql);

        pstmt.setInt(idx++, boardId);
        pstmt.executeUpdate();

        jdbcConnection.closeConnections(connection, pstmt, null);
    }


    /**
     * 조회수 증가
     *
     * @param boardId
     * @throws Exception
     */
    public void plusViews(int boardId) throws Exception {
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt = null;
        int idx = 1;

        String sql = "UPDATE tb_board SET views = views + 1 WHERE board_id = ?;";
        pstmt = connection.prepareStatement(sql);
        pstmt.setInt(idx++, boardId);
        pstmt.executeUpdate();

        jdbcConnection.closeConnections(connection, pstmt, null);
    }

    /**
     * 게시물 수정
     *
     * @param board 게시물
     * @throws Exception
     */
    public void updateBoard(Board board) throws Exception {
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt = null;
        int idx = 1;

        String sql = "UPDATE tb_board SET user_name = ?, title = ?, content = ?, edited_at = CURRENT_TIMESTAMP WHERE board_id = ?";
        pstmt = connection.prepareStatement(sql);
        pstmt.setString(idx++, board.getUserName());
        pstmt.setString(idx++, board.getTitle());
        pstmt.setString(idx++, board.getContent());
        pstmt.setInt(idx++, board.getBoardId());
        pstmt.executeUpdate();

        jdbcConnection.closeConnections(connection, pstmt, null);
    }

    /**
     * pw와 boardId가 일치하는 게시물 row수 반환
     *
     * @param boardId  pk
     * @param password pw
     * @return row count
     * @throws Exception
     */
    public int selectBoardByPassword(int boardId, String password) throws Exception {
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        int count = 0;
        int idx = 1;

        String sql = "SELECT COUNT(*) AS row_count FROM tb_board WHERE board_id = ? AND password = SHA2(?,256)";
        pstmt = connection.prepareStatement(sql);
        pstmt.setInt(idx++, boardId);
        pstmt.setString(idx++, password);
        resultSet = pstmt.executeQuery();

        while (resultSet.next()) {
            count = resultSet.getInt("row_count");
        }

        jdbcConnection.closeConnections(connection, pstmt, resultSet);

        return count;
    }
}
