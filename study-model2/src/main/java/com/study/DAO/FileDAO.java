package com.study.DAO;

import com.study.DTO.FileDTO;
import com.study.connection.DBCPConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * File DAO
 */
public class FileDAO {

    /**
     * 게시물에 있는 FileList 반환
     *
     * @param boardId pk
     * @return 게시물에 있는 첨부파일 리스트
     * @throws Exception
     */
    public List<FileDTO> selectFileListByBoardId(int boardId) throws Exception {
        DBCPConnection dbcpConnection = new DBCPConnection();
        Connection connection = dbcpConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        List<FileDTO> fileList = new ArrayList<>();

        String sql = "SELECT * FROM tb_file WHERE board_id = ?";
        int idx = 1;

        pstmt = connection.prepareStatement(sql);
        pstmt.setInt(idx++, boardId);
        resultSet = pstmt.executeQuery();

        while (resultSet.next()) {
            FileDTO file = FileDTO.builder()
                    .originalName(resultSet.getString("original_name"))
                    .fileId(resultSet.getInt("file_id"))
                    .build();

            fileList.add(file);
        }

        dbcpConnection.closeConnections(connection, pstmt, resultSet);

        return fileList;
    }

    /**
     * 파일 INSERT
     *
     * @param file 파일 저장
     * @throws Exception
     */
    public void insertFile(FileDTO file) throws Exception {
        DBCPConnection dbcpConnection = new DBCPConnection();
        Connection connection = dbcpConnection.getConnection();
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

        dbcpConnection.closeConnections(connection, pstmt, null);
    }

    /**
     * 단일 파일 SELECT
     *
     * @param fileId pk
     * @return file
     * @throws Exception
     */
    public FileDTO selectFileById(int fileId) throws Exception {
        DBCPConnection dbcpConnection = new DBCPConnection();
        Connection connection = dbcpConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        FileDTO file = null;
        int idx = 1;

        String sql = "SELECT * FROM tb_file WHERE file_id = ?";

        pstmt = connection.prepareStatement(sql);
        pstmt.setInt(idx++, fileId);
        resultSet = pstmt.executeQuery();

        while (resultSet.next()) {
            file = FileDTO.builder()
                    .boardId(resultSet.getInt("board_id"))
                    .originalName(resultSet.getString("original_name"))
                    .physicalName(resultSet.getString("physical_name"))
                    .filePath(resultSet.getString("file_path"))
                    .extension(resultSet.getString("extension"))
                    .size(resultSet.getInt("size"))
                    .build();
        }

        dbcpConnection.closeConnections(connection, pstmt, resultSet);

        return file;
    }

    /**
     * 단일 파일 삭제
     *
     * @param fileId PK
     * @throws Exception
     */
    public void deleteById(int fileId) throws Exception {
        DBCPConnection dbcpConnection = new DBCPConnection();
        Connection connection = dbcpConnection.getConnection();
        PreparedStatement pstmt = null;
        int idx = 1;

        String sql = "DELETE FROM tb_file WHERE file_id = ?";

        pstmt = connection.prepareStatement(sql);
        pstmt.setInt(idx++, fileId);
        pstmt.executeUpdate();

        dbcpConnection.closeConnections(connection, pstmt, null);
    }

    /**
     * 게시물에 있는 모든 파일 삭제
     *
     * @param boardId board PK
     * @throws Exception
     */
    public void deleteByBoardId(int boardId) throws Exception {
        DBCPConnection dbcpConnection = new DBCPConnection();
        Connection connection = dbcpConnection.getConnection();
        PreparedStatement pstmt = null;
        int idx = 1;

        String sql = "DELETE FROM tb_file WHERE board_id = ?";

        pstmt = connection.prepareStatement(sql);
        pstmt.setInt(idx++, boardId);
        pstmt.executeUpdate();

        dbcpConnection.closeConnections(connection, pstmt, null);
    }
}
