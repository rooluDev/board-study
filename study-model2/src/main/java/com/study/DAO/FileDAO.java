package com.study.DAO;

import com.study.DTO.FileDTO;
import com.study.connection.DBCPConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileDAO {

    public int insertFile(FileDTO file) {
        DBCPConnection dbcpConnection = new DBCPConnection();
        Connection connection = dbcpConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        int fileId = 0;
        int idx = 1;
        String sql = "INSERT INTO tb_file (board_id,original_name,physical_name,file_path,extension,size,created_at) VALUES (?,?,?,?,?,?,NOW());";

        try {
            pstmt = connection.prepareStatement(sql, 1);
            pstmt.setInt(idx++, file.getBoardId());
            pstmt.setString(idx++, file.getOriginalName());
            pstmt.setString(idx++, file.getPhysicalName());
            pstmt.setString(idx++, file.getFilePath());
            pstmt.setString(idx++, file.getExtension());
            pstmt.setInt(idx++, file.getSize());
            pstmt.executeUpdate();

            for(resultSet = pstmt.getGeneratedKeys(); resultSet.next(); fileId = resultSet.getInt(1)) {
            }
        } catch (SQLException var13) {
            var13.printStackTrace();
        } finally {
            dbcpConnection.closeConnections(connection, pstmt, resultSet);
        }

        return fileId;
    }

    public List<FileDTO> findByBoardId(int boardId) {
        DBCPConnection dbcpConnection = new DBCPConnection();
        Connection connection = dbcpConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        int idx = 1;
        List<FileDTO> fileList = new ArrayList();
        String sql = "SELECT * FROM tb_file WHERE board_id = ?";

        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(idx++, boardId);
            resultSet = pstmt.executeQuery();
            fileList = this.getFileList(resultSet);
        } catch (SQLException var13) {
            var13.printStackTrace();
        } finally {
            dbcpConnection.closeConnections(connection, pstmt, resultSet);
        }

        return (List)fileList;
    }

    public FileDTO findById(int fileId) {
        DBCPConnection dbcpConnection = new DBCPConnection();
        Connection connection = dbcpConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        FileDTO file = new FileDTO();
        int idx = 1;
        String sql = "SELECT * FROM tb_file WHERE file_id = ?";

        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(idx++, fileId);
            resultSet = pstmt.executeQuery();
            file = (FileDTO)this.getFileList(resultSet).get(0);
        } catch (SQLException var13) {
            var13.printStackTrace();
        } finally {
            dbcpConnection.closeConnections(connection, pstmt, resultSet);
        }

        return file;
    }

    public void deleteById(int fileId) {
        DBCPConnection dbcpConnection = new DBCPConnection();
        Connection connection = dbcpConnection.getConnection();
        PreparedStatement pstmt = null;
        int idx = 1;
        String sql = "DELETE FROM tb_file WHERE file_id = ?";

        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(idx++, fileId);
            pstmt.execute();
        } catch (SQLException var11) {
            var11.printStackTrace();
        } finally {
            dbcpConnection.closeConnections(connection, pstmt, (ResultSet)null);
        }

    }

    private List<FileDTO> getFileList(ResultSet resultSet) throws SQLException {
        List<FileDTO> fileList = new ArrayList();

        while(resultSet.next()) {
            FileDTO file = new FileDTO();
            file.setFileId(resultSet.getInt("file_id"));
            file.setBoardId(resultSet.getInt("board_id"));
            file.setOriginalName(resultSet.getString("original_name"));
            file.setPhysicalName(resultSet.getString("physical_name"));
            file.setFilePath(resultSet.getString("file_path"));
            file.setExtension(resultSet.getString("extension"));
            file.setSize(resultSet.getInt("size"));
            file.setCreatedAt(resultSet.getTimestamp("created_at"));
            fileList.add(file);
        }

        return fileList;
    }
}
