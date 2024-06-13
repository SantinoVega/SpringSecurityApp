package com.app;

import com.app.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class SpringSecurityAppApplicationTests {

	@Test
	void main() {
		UserRepository userRepository;
		Assertions.assertNull(null);
	}

}
