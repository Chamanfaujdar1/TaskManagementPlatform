package com.flowboard.card;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import com.flowboard.card.repository.CardSearchRepository;

@SpringBootTest
@ActiveProfiles("test")
class CardServiceApplicationTests {

    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    @MockitoBean
    private RestTemplate restTemplate;

    @MockitoBean
    private CardSearchRepository cardSearchRepository;

	@Test
	void contextLoads() {
	}

}
