package com.flowboard.card.service;

import com.flowboard.card.dto.CardDto;
import com.flowboard.card.entity.Card;
import com.flowboard.card.exception.BadRequestException;
import com.flowboard.card.repository.CardRepository;
import com.flowboard.card.repository.CardSearchRepository;
import com.flowboard.card.serviceimpl.CardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardSearchRepository cardSearchRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private CardServiceImpl cardService;

    private Card testCard;
    private CardDto testCardDto;

    @BeforeEach
    void setUp() {
        testCard = new Card();
        testCard.setCardId(1);
        testCard.setTitle("Test Card");
        testCard.setListId(10);
        testCard.setBoardId(100);
        testCard.setPosition(0);
        testCard.setIsArchived(false);

        testCardDto = new CardDto();
        testCardDto.setCardId(1);
        testCardDto.setTitle("Test Card");
        testCardDto.setListId(10);
        testCardDto.setBoardId(100);
    }

    @Test
    void createCard_Success() {
        // Arrange
        when(cardRepository.countByListId(anyInt())).thenReturn(0L);
        when(cardRepository.save(any(Card.class))).thenReturn(testCard);

        // Act
        CardDto saved = cardService.createCard(testCardDto);

        // Assert
        assertNotNull(saved);
        assertEquals(0, saved.getPosition());
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void createCard_EmptyTitle_ThrowsException() {
        // Arrange
        testCardDto.setTitle("");

        // Act & Assert
        assertThrows(BadRequestException.class, () -> cardService.createCard(testCardDto));
    }
}
