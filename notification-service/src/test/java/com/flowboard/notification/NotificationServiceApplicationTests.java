package com.flowboard.notification;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@ActiveProfiles("test")
class NotificationServiceApplicationTests {

    @MockitoBean
    private JavaMailSender mailSender;

    @MockitoBean
    private RestTemplate restTemplate;

	@Test
	void contextLoads() {
	}

}
