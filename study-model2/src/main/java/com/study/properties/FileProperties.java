
package com.study.properties;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * File Properties Utils
 */
public class FileProperties {

    /**
     * 프로퍼티 파일에 있는 key,value 가져오기
     *
     * @return fileProperties(key,value)
     * @throws Exception exception
     */
    public Map<String, String> getProperties() throws Exception {
        Map<String, String> fileProperties = new HashMap<>();

        FileReader fileReader = new FileReader("/Users/user/workspace/board-study/study-model2/src/main/webapp/file.properties");

        Properties properties = new Properties();
        properties.load(fileReader);

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            fileProperties.put(key, value);
        }
        fileReader.close();

        return fileProperties;
    }
}
