
package com.study.factory;

import com.study.command.AddCommand;
import com.study.command.AddProcCommand;
import com.study.command.Command;
import com.study.command.CommentProc;
import com.study.command.DeleteCommand;
import com.study.command.DownloadCommand;
import com.study.command.EditCommand;
import com.study.command.EditProcCommand;
import com.study.command.ErrorCommand;
import com.study.command.ListCommand;
import com.study.command.PasswordConfirmCommand;
import com.study.command.ViewCommand;
import lombok.Getter;

/**
 * Command Factory
 */
public class CommandFactory {

    @Getter
    private static CommandFactory instance = new CommandFactory();

    private CommandFactory() {
    }

    /**
     * Command에 맞는 Command 생성
     *
     * @param cmd command
     * @return Command 객체
     */
    public Command createCommand(String cmd) {
        switch (cmd) {
            case "list":
                return new ListCommand();
            case "add":
                return new AddCommand();
            case "addProc":
                return new AddProcCommand();
            case "view":
                return new ViewCommand();
            case "edit":
                return new EditCommand();
            case "delete":
                return new DeleteCommand();
            case "commentProc":
                return new CommentProc();
            case "download":
                return new DownloadCommand();
            case "error":
                return new ErrorCommand();
            case "passwordConfirm":
                return new PasswordConfirmCommand();
            case "editProc":
                return new EditProcCommand();
            default:
                return null;
        }
    }
}
