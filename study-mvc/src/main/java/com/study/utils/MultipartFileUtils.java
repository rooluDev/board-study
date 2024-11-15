package com.study.utils;

/**
 * MultipartFile Utils
 */
public class MultipartFileUtils {

    /**
     * 이름에서 확장자 추출
     * @param originalFileName
     * @return
     */
    public static String extractExtension(String originalFileName){
        if (originalFileName != null) {
            int lastDotIndex = originalFileName.lastIndexOf('.');
            if (lastDotIndex > 0) {
                return originalFileName.substring(lastDotIndex + 1);
            }
        }
        return null;
    }
}
