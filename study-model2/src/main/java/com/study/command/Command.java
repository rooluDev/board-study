
package com.study.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command Interface
 */
public interface Command {

    /**
     * 실행 메소드
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception exception
     */
    void execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
