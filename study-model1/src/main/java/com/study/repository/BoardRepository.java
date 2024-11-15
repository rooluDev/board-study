package com.study.repository;

import com.mysql.cj.util.StringUtils;
import com.study.ListCondition;
import com.study.connection.JDBCConnection;
import com.study.domain.Board;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BoardRepository {

    public int insertBoard(String title,String content,String userName,String password) throws Exception {
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        int generatedKey = 0;

        Timestamp createdAt = new Timestamp(new Date().getTime());

        String sql = "insert into tb_board (title,created_at,content,user_name,password) values (?,?,?,?,?)";
        pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        pstmt.setString(1, title);
        pstmt.setTimestamp(2,createdAt);
        pstmt.setString(3, content);
        pstmt.setString(4, userName);
        pstmt.setString(5,password);
        pstmt.executeUpdate();
        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        while(generatedKeys.next()){
            generatedKey = generatedKeys.getInt(1);
        }
        connection.close();
        return generatedKey;
    }

    public List<Board> selectAll(ListCondition condition) throws Exception {
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;

        String sql = "SELECT * FROM tb_board ORDER BY created_at DESC LIMIT ? OFFSET ?";

        if(condition.isExist()) {
            sql += "where"
        }

        if(StringUtils.isEmpty(condition.getSearchText())) {
            sql += " searText List"
        }
        pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, pageSize);
        pstmt.setInt(2, startRow);
        resultSet = pstmt.executeQuery();

        List<Board> boardList = new ArrayList<>();
        while (resultSet.next()) {
            Board board = new Board();
            board.setBoardId(resultSet.getInt("board_id"));
            board.setTitle(resultSet.getString("title"));
            board.setUserName(resultSet.getString("user_name"));
            board.setViews(resultSet.getInt("views"));
            board.setCreatedAt(resultSet.getTimestamp("created_at"));
            board.setEditedAt(resultSet.getTimestamp("edited_at"));
            board.setPassword(resultSet.getString("password"));
            board.setContent(resultSet.getString("content"));
            boardList.add(board);
        }
        connection.close();
        return boardList;
    }

    public int countAll() throws Exception {
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        ResultSet resultSet = null;
        PreparedStatement pstmt = null;

        String sql = "select count(*) as row_count from tb_board";
        pstmt = connection.prepareStatement(sql);
        resultSet = pstmt.executeQuery();
        while (resultSet.next()) {
            connection.close();
            return resultSet.getInt("row_count");
        }
        connection.close();
        return -1;
    }

    public int getTotalPageCount() throws Exception{
        int countAll = countAll();
        return (int) Math.ceil((double) countAll / 10);
    }

    public Board findById(int boardId) throws Exception{
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        ResultSet resultSet = null;
        PreparedStatement pstmt = null;

        String sql = "select * from tb_board where board_id=?";
        pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, boardId);
        resultSet = pstmt.executeQuery();
        while (resultSet.next()){
            Board board = new Board();
            board.setBoardId(resultSet.getInt("board_id"));
            board.setTitle(resultSet.getString("title"));
            board.setUserName(resultSet.getString("user_name"));
            board.setViews(resultSet.getInt("views"));
            board.setCreatedAt(resultSet.getTimestamp("created_at"));
            board.setEditedAt(resultSet.getTimestamp("edited_at"));
            board.setPassword(resultSet.getString("password"));
            board.setContent(resultSet.getString("content"));
            connection.close();
            return board;
        }
        connection.close();
        return null;
    }

    public void deleteById(int boardId) throws Exception{
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt = null;

        String sql = "delete from tb_board where board_id = ?";
        pstmt = connection.prepareStatement(sql);

        pstmt.setInt(1, boardId);
        pstmt.execute();
        connection.close();
    }

    public List<Board> findBySearch (String startDate, String endDate, String category, String searchText) throws Exception{
        List<Board> boardList = new ArrayList<>();
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;

        String sql = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedStartDate = dateFormat.parse(startDate);
        Date parsedEndDate = dateFormat.parse(endDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parsedEndDate);
        calendar.add(Calendar.DAY_OF_YEAR,1);
        parsedEndDate = calendar.getTime();

        Timestamp startTimeStamp = new Timestamp(parsedStartDate.getTime());
        Timestamp endTimeStamp = new Timestamp(parsedEndDate.getTime());

        if (!category.equals("all")) {
            sql = "SELECT *\n" +
                    "FROM tb_board\n" +
                    "JOIN tb_category ON tb_board.board_id = tb_category.board_id\n" +
                    "WHERE tb_board.created_at BETWEEN ? AND ?\n" +
                    "  AND tb_category.category_name = ?\n" +
                    "AND tb_board.content LIKE ?\n" +
                    "OR tb_board.user_name LIKE ?\n" +
                    "OR tb_board.title LIKE ?;";
            pstmt = connection.prepareStatement(sql);
            int idx = 1;
            pstmt.setTimestamp(idx++,startTimeStamp);
            pstmt.setTimestamp(idx++,endTimeStamp);
            pstmt.setString(idx++,category);
            pstmt.setString(idx++,"%"+searchText+"%");
            pstmt.setString(idx++,"%"+searchText+"%");
            pstmt.setString(idx++,"%"+searchText+"%");
        }
        if (category.equals("all")){
            sql = "SELECT *\n" +
                    "FROM tb_board\n" +
                    "JOIN tb_category ON tb_board.board_id = tb_category.board_id\n" +
                    "WHERE tb_board.created_at BETWEEN ? AND ?\n" +
                    "AND tb_board.content LIKE ?\n" +
                    "OR tb_board.user_name LIKE ?\n" +
                    "OR tb_board.title LIKE ?;";
            pstmt = connection.prepareStatement(sql);
            pstmt.setTimestamp(1,startTimeStamp);
            pstmt.setTimestamp(2,endTimeStamp);
            pstmt.setString(3, "%"+searchText+"%");
            pstmt.setString(4,"%"+searchText+"%");
            pstmt.setString(5,"%"+searchText+"%");
        }
        resultSet = pstmt.executeQuery();

        while (resultSet.next()) {
            Board board = new Board();
            board.setBoardId(resultSet.getInt("board_id"));
            board.setTitle(resultSet.getString("title"));
            board.setUserName(resultSet.getString("user_name"));
            board.setViews(resultSet.getInt("views"));
            board.setCreatedAt(resultSet.getTimestamp("created_at"));
            board.setEditedAt(resultSet.getTimestamp("edited_at"));
            board.setPassword(resultSet.getString("password"));
            board.setContent(resultSet.getString("content"));
            boardList.add(board);
        }


        resultSet.close();

        pstmt.close();

        connection.close();

        return boardList;
    }

    public void plusViews(int boardId) throws Exception {
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt = null;
        String sql = "update tb_board set views = views + 1 where board_id = ?;";
        pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, boardId);
        pstmt.executeUpdate();
        connection.close();
    }
}
