package com.study.validate;

import com.study.dto.BoardDTO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 유효성 검증
 */
public class BoardValidator {

    /**
     * 게시물 추가 시 validator
     *
     * @param board 추가 할 게시물
     * @return validate 여부
     */
    public static boolean validateBoardForPost(BoardDTO board) {
        boolean isValid = validateCategory(board.getCategoryId()) || validateUserName(board.getUserName()) || validatePassword(board.getPassword())
                || validatePasswordMatch(board.getPassword(), board.getPasswordRe()) || validateTitle(board.getTitle()) || validateContent(board.getContent());

        return isValid;
    }

    /**
     * 게시물 수정 시 validator
     *
     * @param board 수정 할 게시물
     * @return validate 여부
     */
    public static boolean validateBoardForEdit(BoardDTO board) {
        boolean isValid = validateUserName(board.getUserName()) || validateTitle(board.getTitle()) || validateContent(board.getContent());

        return isValid;
    }

    private static boolean validateCategory(Long categoryId) {
        return categoryId > 0;
    }

    private static boolean validateUserName(String userName) {
        return userName != null && userName.length() > 2 && userName.length() < 5;
    }

    private static boolean validatePassword(String password) {
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{4,16}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    private static boolean validatePasswordMatch(String password, String passwordRe) {
        return password != null && password.equals(passwordRe);
    }

    private static boolean validateTitle(String title) {
        return title != null && title.length() > 3 && title.length() < 101;
    }

    private static boolean validateContent(String content) {
        return content != null && content.length() > 3 && content.length() < 2001;
    }
}
