package com.rajat.caching;

import com.rajat.caching.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class CachingJavaApplicationTests {

	@Autowired
	private UserController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

}
