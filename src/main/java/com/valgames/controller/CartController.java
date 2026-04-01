package com.valgames.controller;

import com.valgames.model.Product;
import com.valgames.service.CartService;
import com.valgames.service.CategoryService;
import com.valgames.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        model.addAttribute("items", cartService.getItems(session));
        model.addAttribute("total", cartService.getTotal(session));
        model.addAttribute("cartCount", cartService.getCount(session));
        model.addAttribute("categories", categoryService.findAll());
        return "catalog/cart";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<String> addItem(HttpSession session,
                                          @RequestParam int productId,
                                          @RequestParam(defaultValue = "1") int quantity) {
        Optional<Product> product = productService.findById(productId);
        if (product.isEmpty()) return ResponseEntity.badRequest().body("Product not found.");
        String error = cartService.addItem(session, product.get(), quantity);
        if (error != null) return ResponseEntity.badRequest().body(error);
        return ResponseEntity.ok("Added");
    }

    @GetMapping("/count")
    @ResponseBody
    public int getCount(HttpSession session) {
        return cartService.getCount(session);
    }

    @PostMapping("/update")
    public String updateItem(HttpSession session,
                             @RequestParam int productId,
                             @RequestParam int quantity,
                             RedirectAttributes redirectAttributes) {
        String error = cartService.updateQuantity(session, productId, quantity);
        if (error != null) redirectAttributes.addFlashAttribute("error", error);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeItem(HttpSession session, @RequestParam int productId) {
        cartService.removeItem(session, productId);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        cartService.clear(session);
        return "redirect:/cart";
    }
}