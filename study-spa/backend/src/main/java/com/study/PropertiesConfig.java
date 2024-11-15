package com.study;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * properties file config
 */
@Configuration
public class PropertiesConfig {

    @Bean(name = "file")
    public PropertiesFactoryBean filePropertiesBean() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        ClassPathResource classPathResource = new ClassPathResource("properties/file.properties");

        propertiesFactoryBean.setLocation(classPathResource);

        return propertiesFactoryBean;
    }

    @Bean(name = "pagination")
    public PropertiesFactoryBean paginationPropertiesBean(){
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        ClassPathResource classPathResource = new ClassPathResource("properties/pagination.properties");

        propertiesFactoryBean.setLocation(classPathResource);

        return propertiesFactoryBean;
    }
}
