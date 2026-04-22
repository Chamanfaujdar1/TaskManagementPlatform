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
public class BoardViewController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${auth.service.url}")
    private String authServiceUrl;

    @Value("${workspace.service.url}")
    private String workspaceServiceUrl;

    @Value("${board.service.url}")
    private String boardServiceUrl;

    @Value("${notification.service.url}")
    private String notificationServiceUrl;

    // HOME PAGE
    @GetMapping("/")
    public String home() {
        return "home";
    }

    // LOGIN PAGE
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // REGISTER PAGE
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // DASHBOARD
    @GetMapping("/dashboard")
    public String dashboard(
            @RequestParam int userId,
            Model model) {
        try {
            List workspaces = restTemplate.getForObject(
                    workspaceServiceUrl +
                            "/api/v1/workspaces/member/" + userId,
                    List.class);
            model.addAttribute("workspaces", workspaces);
            model.addAttribute("userId", userId);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "dashboard";
    }

    // VIEW WORKSPACE
    @GetMapping("/workspace/{id}")
    public String viewWorkspace(
            @PathVariable int id,
            Model model) {
        try {
            Map workspace = restTemplate.getForObject(
                    workspaceServiceUrl +
                            "/api/v1/workspaces/" + id,
                    Map.class);
            List boards = restTemplate.getForObject(
                    boardServiceUrl +
                            "/api/v1/boards/workspace/" + id,
                    List.class);
            model.addAttribute("workspace", workspace);
            model.addAttribute("boards", boards);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "workspace";
    }

    // VIEW BOARD
    @GetMapping("/board/{id}")
    public String viewBoard(
            @PathVariable int id,
            Model model) {
        try {
            Map board = restTemplate.getForObject(
                    boardServiceUrl +
                            "/api/v1/boards/" + id,
                    Map.class);
            model.addAttribute("board", board);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "board";
    }

    // VIEW NOTIFICATIONS
    @GetMapping("/notifications/{userId}")
    public String viewNotifications(
            @PathVariable int userId,
            Model model) {
        try {
            List notifications = restTemplate.getForObject(
                    notificationServiceUrl +
                            "/api/v1/notifications/recipient/" + userId,
                    List.class);
            model.addAttribute(
                    "notifications", notifications);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "notifications";
    }
}