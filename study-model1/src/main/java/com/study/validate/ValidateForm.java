package com.study.validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateForm {
    public boolean validateCategory(String category){
        if (category.equals("none")) {
            return false;
        }
        return true;
    }
    public boolean validateUserName(String userName){
        if(userName == null){
            return false;
        }
        if(userName.length() <3 && userName.length() >= 5){
            return false;
        }
        return true;
    }
    public boolean validatePassword(String password){
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{4,16}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    public boolean validatePasswordMatch(String password, String passwordRe){
        if(!password.equals(passwordRe)){
            return false;
        }
        return true;
    }
    public boolean validateTitle(String title){
        if (title.length() < 4 && title.length() >= 100) {
            return false;
        }
        return true;

    }
    public boolean validateContent(String content){
        if (content.length() < 4 && content.length() >= 2000) {
            return false;
        }
        return true;
    }
}
