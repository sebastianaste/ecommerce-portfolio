package com.valgames.controller;

import com.valgames.service.CartService;
import com.valgames.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartService cartService;

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("cartCount", cartService.getCount(session));
        return "index";
    }
}