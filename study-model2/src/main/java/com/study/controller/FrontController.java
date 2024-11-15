
package com.study.controller;

import com.study.command.Command;
import com.study.factory.CommandFactory;
import com.study.utils.StringUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/board"})
public class FrontController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cmd = request.getParameter("command");
        if (StringUtils.isNull(cmd)) {
            cmd = "list";
        }

        Command command = this.createCommand(cmd);
        command.execute(request, response);
    }



    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cmd = request.getParameter("command");
        Command command = this.createCommand(cmd);
        command.execute(request, response);
    }

    private Command createCommand(String cmd) {
        CommandFactory factory = CommandFactory.getInstance();
        Command command = factory.createCommand(cmd);
        return command;
    }
}
