package com.study;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.study.mapper")
@SpringBootApplication
public class Model3Application {

	public static void main(String[] args) {
		SpringApplication.run(Model3Application.class, args);
	}

}
