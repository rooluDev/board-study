
package com.study.controller;

import com.study.command.Command;
import com.study.factory.CommandFactory;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Front Controller
 */
@WebServlet({"/board"})
public class FrontController extends HttpServlet {

    /**
     * GET 요청 Controller
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String cmd = request.getParameter("command");

        if (cmd == null) {
            cmd = "list";
        }

        Command command = this.createCommand(cmd);

        if (command == null) {
            response.sendRedirect("/error");
        }
        try {
            command.execute(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * POST 요청 Controller
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        String cmd = request.getParameter("command");
        Command command = this.createCommand(cmd);
        try {
            command.execute(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Command createCommand(String cmd) {
        CommandFactory factory = CommandFactory.getInstance();
        return factory.createCommand(cmd);
    }
}
