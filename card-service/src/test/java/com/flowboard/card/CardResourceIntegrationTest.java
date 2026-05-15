package com.flowboard.card;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowboard.card.entity.Card;
import com.flowboard.card.repository.CardRepository;
import com.flowboard.card.repository.CardSearchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
public class CardResourceIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    @MockitoBean
    private RestTemplate restTemplate;

    @MockitoBean
    private CardSearchRepository cardSearchRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        cardRepository.deleteAll();
    }

    @Test
    void createAndGetCard_Success() throws Exception {
        // 1. Create Card
        Card card = new Card();
        card.setTitle("Integration Card");
        card.setListId(1);
        card.setBoardId(10);
        card.setCreatedById(100);

        String result = mockMvc.perform(post("/api/v1/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(card)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Integration Card"))
                .andReturn().getResponse().getContentAsString();

        Card saved = objectMapper.readValue(result, Card.class);

        // 2. Get Card by ID
        mockMvc.perform(get("/api/v1/cards/" + saved.getCardId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Integration Card"));
    }
}
