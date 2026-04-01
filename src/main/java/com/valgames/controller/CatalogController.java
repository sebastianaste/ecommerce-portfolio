package com.valgames.controller;

import com.valgames.service.CartService;
import com.valgames.service.CategoryService;
import com.valgames.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CatalogController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartService cartService;

    @GetMapping("/catalog")
    public String catalog(@RequestParam(required = false) String search,
                          @RequestParam(required = false) Integer categoryId,
                          HttpSession session,
                          Model model) {
        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("list", productService.findByName(search));
            model.addAttribute("search", search);
        } else if (categoryId != null) {
            model.addAttribute("list", productService.findByCategoryId(categoryId));
            model.addAttribute("selectedCategory", categoryId);
        } else {
            model.addAttribute("list", productService.findAll());
        }
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("cartCount", cartService.getCount(session));
        return "catalog/products";

    }
}
