package nl.ckarakoc.jellycash;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class JellycashApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("Cultivate!");
	}

}
