package com.study.repository;

import com.study.connection.JDBCConnection;
import com.study.dto.File;
import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 파일 DB 레포
 */
public class FileRepository {

    @Getter
    private static final FileRepository instance = new FileRepository();

    private FileRepository() {
    }

    /**
     * 게시물에 있는 FileList 반환
     *
     * @param boardId 게시물 PK
     * @return 게시물에 있는 첨부파일 리스트
     * @throws Exception
     */
    public List<File> selectFileListByBoardId(int boardId) throws Exception {
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        List<File> fileList = new ArrayList<>();

        String sql = "SELECT * FROM tb_file WHERE board_id = ?";
        int idx = 1;

        pstmt = connection.prepareStatement(sql);
        pstmt.setInt(idx++, boardId);
        resultSet = pstmt.executeQuery();

        while (resultSet.next()) {
            File file = File.builder()
                    .originalName(resultSet.getString("original_name"))
                    .fileId(resultSet.getInt("file_id"))
                    .build();

            fileList.add(file);
        }

        jdbcConnection.closeConnections(connection, pstmt, resultSet);

        return fileList;
    }

    /**
     * 단일 파일 SELECT
     *
     * @param fileId
     * @return
     * @throws Exception
     */
    public File selectFileById(int fileId) throws Exception {
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        File file = null;
        int idx = 1;

        String sql = "SELECT * FROM tb_file WHERE file_id = ?";

        pstmt = connection.prepareStatement(sql);
        pstmt.setInt(idx++, fileId);
        resultSet = pstmt.executeQuery();

        while (resultSet.next()) {
            file = File.builder()
                    .boardId(resultSet.getInt("board_id"))
                    .originalName(resultSet.getString("original_name"))
                    .physicalName(resultSet.getString("physical_name"))
                    .filePath(resultSet.getString("file_path"))
                    .extension(resultSet.getString("extension"))
                    .size(resultSet.getInt("size"))
                    .build();
        }

        jdbcConnection.closeConnections(connection, pstmt, resultSet);

        return file;
    }

    /**
     * File 저장
     *
     * @param file 저장될 파일
     */
    public void insertFile(File file) throws Exception {
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt = null;
        int idx = 1;

        String sql = "INSERT INTO tb_file (board_id, original_name, physical_name, file_path, extension,size) VALUES (?,?,?,?,?,?)";

        pstmt = connection.prepareStatement(sql);
        pstmt.setInt(idx++, file.getBoardId());
        pstmt.setString(idx++, file.getOriginalName());
        pstmt.setString(idx++, file.getPhysicalName());
        pstmt.setString(idx++, file.getFilePath());
        pstmt.setString(idx++, file.getExtension());
        pstmt.setInt(idx++, file.getSize());
        pstmt.executeUpdate();

        jdbcConnection.closeConnections(connection, pstmt, null);
    }

    /**
     * 단일 파일 삭제
     *
     * @param fileId PK
     * @throws Exception
     */
    public void deleteById(int fileId) throws Exception {
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt = null;
        int idx = 1;

        String sql = "DELETE FROM tb_file WHERE file_id = ?";

        pstmt = connection.prepareStatement(sql);
        pstmt.setInt(idx++, fileId);
        pstmt.executeUpdate();

        jdbcConnection.closeConnections(connection, pstmt, null);
    }

    /**
     * 게시물에 있는 모든 파일 삭제
     *
     * @param boardId board PK
     * @throws Exception
     */
    public void deleteByBoardId(int boardId) throws Exception {
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt = null;
        int idx = 1;

        String sql = "DELETE FROM tb_file WHERE board_id = ?";

        pstmt = connection.prepareStatement(sql);
        pstmt.setInt(idx++, boardId);
        pstmt.executeUpdate();

        jdbcConnection.closeConnections(connection, pstmt, null);
    }
}