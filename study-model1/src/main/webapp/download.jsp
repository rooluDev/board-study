<%@ page import="com.study.service.FileService" %>
<%@ page import="com.study.dto.File" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    int fileId = Integer.parseInt(request.getParameter("fileId"));
    FileService fileService = new FileService();
    File file = fileService.getFileById(fileId);

    String physicalName = file.getPhysicalName() + "." + file.getExtension();

    java.io.File downlaodFile = new java.io.File(file.getFilePath(), physicalName);
    String originalName = file.getOriginalName();

    String encodedName = URLEncoder.encode(originalName, "UTF-8").replace("+", "%20");


    response.setContentType("application/octet-stream");
    response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedName + "\";");


    FileInputStream fis = null;
    OutputStream os = null;
    try {
        fis = new FileInputStream(downlaodFile);
        os = response.getOutputStream();

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if (fis != null) try { fis.close(); } catch (IOException e) {}
        if (os  != null) try { os.close();  } catch (IOException e) {}
    }
%>
