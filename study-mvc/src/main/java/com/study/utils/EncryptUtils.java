package com.study.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA-256 암호화 알고리즘 유틸
 */
public class EncryptUtils {

    /**
     * password 생성기
     * @param password
     * @return
     */
    public static String encryptPassword(String password){
        StringBuffer stringBuffer = new StringBuffer();
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());

            byte[] data = messageDigest.digest();

            for(byte b : data){
                stringBuffer.append(String.format("%02x",b));
            }
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }


}
