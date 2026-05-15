package com.flowboard.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
@RequestMapping("/card")
public class CardController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${card.service.url}")
    private String cardServiceUrl;

    // VIEW CARD DETAIL
    @GetMapping("/{id}")
    public String viewCard(
            @PathVariable int id,
            Model model) {
        try {
            Map card = restTemplate.getForObject(
                    cardServiceUrl + "/api/v1/cards/" + id,
                    Map.class);
            model.addAttribute("card", card);
        } catch (Exception e) {
            model.addAttribute("error", "Could not fetch card details: " + e.getMessage());
        }
        return "card";
    }

    // UPDATE CARD
    @PostMapping("/{id}/update")
    public String updateCard(
            @PathVariable int id,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String status,
            @RequestParam String priority) {
        try {
            Map<String, String> request = Map.of(
                    "title", title,
                    "description", description,
                    "status", status,
                    "priority", priority
            );
            restTemplate.put(cardServiceUrl + "/api/v1/cards/" + id, request);
        } catch (Exception e) {
            // Handle error (optionally add to redirect attributes)
        }
        return "redirect:/card/" + id;
    }
}
