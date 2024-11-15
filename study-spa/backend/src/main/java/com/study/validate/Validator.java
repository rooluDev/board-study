package com.study.validate;

import com.study.dto.BoardDto;
import com.study.dto.BoardUpdateFormDto;
import com.study.exception.IllegalLengthException;
import com.study.exception.NoSelectedException;
import com.study.exception.PasswordIncorrectException;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 유효성 검증
 */
public class Validator {

    /**
     * Board 생성 form 유효성 검증
     *
     * @param boardDTO
     * @param passwordCheck
     * @return
     */
    public static void validateBoardInput(BoardDto boardDTO, String passwordCheck) throws Exception {
        validateCategory(boardDTO.getCategoryId());
        validateContent(boardDTO.getContent());
        validatePassword(boardDTO.getPassword());
        validatePasswordEquals(boardDTO.getPassword(), passwordCheck);
        validateTitle(boardDTO.getTitle());
        validateUserName(boardDTO.getUserName());
    }

    /**
     * Board Update Form Data 유효성 검증
     * @param boardUpdateFormDto
     * @throws Exception
     */
    public static void validateUpdateBoardInput(BoardUpdateFormDto boardUpdateFormDto,String password) throws Exception{
        validateUserName(boardUpdateFormDto.getUserName());
        validateTitle(boardUpdateFormDto.getTitle());
        validateContent(boardUpdateFormDto.getContent());
        validatePasswordEquals(password, boardUpdateFormDto.getPasswordCheck());
    }

    /**
     * 카테고리 필수 선택
     *
     * @param categoryId
     * @return
     */
    private static void validateCategory(Long categoryId) throws Exception {
        if (categoryId < 1) {
            throw new NoSelectedException("No category selected");
        }
    }

    /**
     * userName null 체크, 글자 길이 체크
     *
     * @param userName
     * @return
     */
    private static void validateUserName(String userName) throws Exception {
        if (StringUtils.isEmpty(userName)) {
            throw new NullPointerException("UserName is Empty");
        } else if (!(userName.length() > 2 && userName.length() < 5)) {
            throw new IllegalLengthException("UserName length invalidate");
        }
    }

    /**
     * password 4글자 이상, 16자 미만, 영문 / 숫자 / 특수문자 포함
     *
     * @param password
     * @return
     */
    private static void validatePassword(String password) throws Exception {
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{4,16}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new PasswordIncorrectException("Password regex Invalidate");
        }
    }

    /**
     * password, passwordCheck 동일한지
     *
     * @param password
     * @param passwordCheck
     * @return
     */
    private static void validatePasswordEquals(String password, String passwordCheck) throws Exception {
        if (StringUtils.isEmpty(password) && StringUtils.isEmpty(passwordCheck)) {
            throw new NullPointerException("Password is empty");
        } else if (!password.equals(passwordCheck)) {
            throw new PasswordIncorrectException("Password incorrect");
        }
    }

    /**
     * title 4글자 이상, 100글자 미만
     *
     * @param title
     * @return
     */
    private static void validateTitle(String title) throws Exception {
        if (StringUtils.isEmpty(title)) {
            throw new NullPointerException("Title is Empty");
        } else if (!(title.length() > 3 && title.length() < 100)) {
            throw new IllegalLengthException("Title length is invalidate");
        }
    }

    /**
     * content 4글자 2000글자 미만
     *
     * @param content
     * @return
     */
    private static void validateContent(String content) throws Exception {
        if (StringUtils.isEmpty(content)) {
            throw new NullPointerException("Content is Empty");
        } else if (!(content.length() > 3 && content.length() < 2000)) {
            throw new IllegalLengthException("Content length is invalidate");
        }
    }
}
