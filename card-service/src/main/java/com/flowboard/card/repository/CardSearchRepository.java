package com.flowboard.card.repository;

import com.flowboard.card.model.CardDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardSearchRepository extends ElasticsearchRepository<CardDocument, Integer> {
    List<CardDocument> findByTitleContainingOrDescriptionContaining(String title, String description);
}
