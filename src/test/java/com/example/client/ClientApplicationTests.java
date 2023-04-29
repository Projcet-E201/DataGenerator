package com.example.client;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(profiles = {"local",  "secret", "client1"})
@SpringBootTest
class ClientApplicationTests {

	@Test
	void contextLoads() {
	}

}
