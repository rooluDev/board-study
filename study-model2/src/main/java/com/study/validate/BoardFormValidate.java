
package com.study.validate;

import com.study.utils.IntegerUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BoardFormValidate {

    public boolean validateCategory(int categoryId) {
        return IntegerUtils.isUnsigned(categoryId);
    }

    public boolean validateUserName(String userName) {
        if (userName == null) {
            return false;
        } else {
            return userName.length() >= 3 || userName.length() < 5;
        }
    }

    public boolean validatePassword(String password) {
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{4,16}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public boolean validatePasswordMatch(String password, String passwordRe) {
        return password.equals(passwordRe);
    }

    public boolean validateTitle(String title) {
        if (title.length() < 4 && title.length() >= 100) {
            System.out.println("aaaaa");
            return false;
        } else {
            return true;
        }
    }

    public boolean validateContent(String content) {
        return content.length() >= 4 || content.length() < 2000;
    }
}
