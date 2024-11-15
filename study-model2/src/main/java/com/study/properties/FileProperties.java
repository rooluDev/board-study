
package com.study.properties;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class FileProperties {

    public Map<String, String> getProperties() {
        Map<String, String> fileProperties = new HashMap();

        try {
            FileReader fileReader = new FileReader("/Users/user/workspace/eb-study-templates-1week/src/main/webapp/WEB-INF/file.properties");

            try {
                Properties properties = new Properties();
                properties.load(fileReader);
                Iterator<Map.Entry<Object, Object>> iterator = properties.entrySet().iterator();

                while(iterator.hasNext()) {
                    Map.Entry<Object, Object> entry = (Map.Entry)iterator.next();
                    String key = (String)entry.getKey();
                    String value = (String)entry.getValue();
                    fileProperties.put(key, value);
                }
            } catch (Throwable var9) {
                try {
                    fileReader.close();
                } catch (Throwable var8) {
                    var9.addSuppressed(var8);
                }

                throw var9;
            }

            fileReader.close();
        } catch (IOException var10) {
            var10.printStackTrace();
        }

        return fileProperties;
    }
}
