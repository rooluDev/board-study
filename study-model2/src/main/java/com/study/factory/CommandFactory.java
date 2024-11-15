
package com.study.factory;

import com.study.command.AddCommand;
import com.study.command.AddProcCommand;
import com.study.command.Command;
import com.study.command.CommentProc;
import com.study.command.DeleteCommand;
import com.study.command.DeleteFileCommand;
import com.study.command.DownloadCommand;
import com.study.command.EditCommand;
import com.study.command.EditProcCommand;
import com.study.command.ErrorCommand;
import com.study.command.ListCommand;
import com.study.command.PasswordConfirm;
import com.study.command.ViewCommand;

public class CommandFactory {
    private static CommandFactory instance = new CommandFactory();

    private CommandFactory() {
    }

    public static CommandFactory getInstance() {
        return instance;
    }

    public Command createCommand(String cmd) {
        if (cmd.equals("list")) {
            return new ListCommand();
        } else if (cmd.equals("add")) {
            return new AddCommand();
        } else if (cmd.equals("addProc")) {
            return new AddProcCommand();
        } else if (cmd.equals("view")) {
            return new ViewCommand();
        } else if (cmd.equals("edit")) {
            return new EditCommand();
        } else if (cmd.equals("delete")) {
            return new DeleteCommand();
        } else if (cmd.equals("commentProc")) {
            return new CommentProc();
        } else if (cmd.equals("download")) {
            return new DownloadCommand();
        } else if (cmd.equals("deleteFile")) {
            return new DeleteFileCommand();
        } else if (cmd.equals("error")) {
            return new ErrorCommand();
        } else if (cmd.equals("passwordConfirm")) {
            return new PasswordConfirm();
        } else {
            return cmd.equals("editProc") ? new EditProcCommand() : null;
        }
    }
}
