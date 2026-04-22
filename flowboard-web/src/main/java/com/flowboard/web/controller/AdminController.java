package com.flowboard.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${auth.service.url}")
    private String authServiceUrl;

    @Value("${workspace.service.url}")
    private String workspaceServiceUrl;

    @Value("${board.service.url}")
    private String boardServiceUrl;

    @Value("${card.service.url}")
    private String cardServiceUrl;

    @Value("${notification.service.url}")
    private String notificationServiceUrl;

    // ADMIN DASHBOARD
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        return "admin/dashboard";
    }

    // MANAGE ALL USERS
    @GetMapping("/users")
    public String manageUsers(Model model) {
        try {
            List users = restTemplate.getForObject(
                    authServiceUrl +
                            "/api/v1/auth/search?query=",
                    List.class);
            model.addAttribute("users", users);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "admin/users";
    }

    // MANAGE ALL WORKSPACES
    @GetMapping("/workspaces")
    public String manageWorkspaces(Model model) {
        try {
            List workspaces = restTemplate.getForObject(
                    workspaceServiceUrl +
                            "/api/v1/workspaces/public",
                    List.class);
            model.addAttribute(
                    "workspaces", workspaces);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "admin/workspaces";
    }

    // MANAGE ALL BOARDS
    @GetMapping("/boards")
    public String manageBoards(Model model) {
        return "admin/boards";
    }

    // VIEW OVERDUE CARDS
    @GetMapping("/overdue-cards")
    public String viewOverdueCards(Model model) {
        try {
            List cards = restTemplate.getForObject(
                    cardServiceUrl +
                            "/api/v1/cards/overdue",
                    List.class);
            model.addAttribute("cards", cards);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "admin/overdue-cards";
    }

    // SEND PLATFORM NOTIFICATION
    @PostMapping("/notifications/send")
    public String sendNotification(
            @RequestParam String title,
            @RequestParam String message,
            Model model) {
        try {
            Map<String, Object> request = Map.of(
                    "recipientIds", List.of(1, 2, 3),
                    "title", title,
                    "message", message
            );
            restTemplate.postForObject(
                    notificationServiceUrl +
                            "/api/v1/notifications/bulk",
                    request, String.class);
            model.addAttribute("success",
                    "Notification sent!");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "admin/dashboard";
    }
}