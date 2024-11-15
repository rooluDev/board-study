package com.study;

import com.study.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendApplicationTests {
	@Autowired
	private BoardService boardService;
	@Test
	void contextLoads() {

	}

}
