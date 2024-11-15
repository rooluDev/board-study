package com.study.validate;

import com.study.form.CreateBoardForm;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO : Exception
/**
 * 유효성 검증
 */
public class Validator {

    /**
     * Board 생성 form 유효성 검증
     * @param createBoardForm
     * @return
     */
    public static boolean validateBoardInput(CreateBoardForm createBoardForm){
        if (!validateCategory(createBoardForm.getCategoryId())
                && validateContent(createBoardForm.getContent())
                && validatePassword(createBoardForm.getPassword())
                && validatePasswordEquals(createBoardForm.getPassword(), createBoardForm.getPasswordCheck())
                && validateTitle(createBoardForm.getTitle())
                && validateUserName(createBoardForm.getUserName())) {
            throw new IllegalArgumentException("입력 오류");
        }
        return true;
    }

    /**
     * 카테고리 필수 선택
     * @param categoryId
     * @return
     */
    private static boolean validateCategory(Long categoryId){
        if(categoryId > 0){
            return true;
        }
        return false;
    }

    /**
     * userName null 체크, 글자 길이 체크
     * @param userName
     * @return
     */
    private static boolean validateUserName(String userName){
        if(StringUtils.isEmpty(userName)){
            return false;
        }
        return userName.length() >= 3 || userName.length() < 5;
    }

    /**
     * password 4글자 이상, 16자 미만, 영문 / 숫자 / 특수문자 포함
     * @param password
     * @return
     */
    private static boolean validatePassword(String password){
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{4,16}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    /**
     * password, passwordCheck 동일한지
     * @param password
     * @param passwordCheck
     * @return
     */
    private static boolean validatePasswordEquals(String password, String passwordCheck){
        if(StringUtils.isEmpty(password) && StringUtils.isEmpty(passwordCheck)){
            return false;
        }
        return password.equals(passwordCheck);
    }

    /**
     * title 4글자 이상, 100글자 미만
     * @param title
     * @return
     */
    private static boolean validateTitle(String title){
        if(StringUtils.isEmpty(title)){
            return false;
        }
        return title.length() < 4 && title.length() >= 100;
    }

    /**
     * content 4글자 2000글자 미만
     * @param content
     * @return
     */
    private static boolean validateContent(String content){
        if(StringUtils.isEmpty(content)){
            return false;
        }
        return content.length() < 4 && content.length() >= 2000;
    }
}
