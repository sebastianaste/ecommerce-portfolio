package com.valgames.controller;

import com.valgames.model.Product;
import com.valgames.service.CategoryService;
import com.valgames.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String list(@RequestParam(required = false) String search, Model model) {
        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("list", productService.findByName(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("list", productService.findAll());
        }
        return "admin/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        return "admin/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute Product product,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
            model.addAttribute("error", "Product name is required.");
            model.addAttribute("categories", categoryService.findAll());
            return "admin/new";
        }
        if (product.getUnitPrice() == null || product.getUnitPrice() <= 0) {
            model.addAttribute("error", "Price must be greater than 0.");
            model.addAttribute("categories", categoryService.findAll());
            return "admin/new";
        }
        productService.save(product);
        redirectAttributes.addFlashAttribute("success", "Product created successfully.");
        return "redirect:/admin/products";
    }

    @GetMapping("/edit")
    public String editSearch(@RequestParam(required = false) String name,
                             @RequestParam(required = false) Integer id,
                             Model model) {
        if (id != null) {
            Optional<Product> product = productService.findById(id);
            if (product.isEmpty()) {
                model.addAttribute("error", "No product found with ID: " + id);
            } else {
                model.addAttribute("product", product.get());
                model.addAttribute("categories", categoryService.findAll());
            }
        } else if (name != null && !name.trim().isEmpty()) {
            List<Product> results = productService.findByName(name);
            if (results.isEmpty()) {
                model.addAttribute("error", "No products found matching: " + name);
            } else if (results.size() == 1) {
                model.addAttribute("product", results.get(0));
                model.addAttribute("categories", categoryService.findAll());
            } else {
                model.addAttribute("results", results);
            }
        }
        return "admin/edit";
    }

    @PostMapping("/edit")
    public String update(@Valid @ModelAttribute Product product,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "admin/edit";
        }
        productService.save(product);
        redirectAttributes.addFlashAttribute("success", "Product updated successfully.");
        return "redirect:/admin/products";
    }

    @GetMapping("/delete")
    public String deleteSearch(@RequestParam(required = false) String name,
                               @RequestParam(required = false) Integer id,
                               Model model) {
        if (id != null) {
            Optional<Product> product = productService.findById(id);
            if (product.isEmpty()) {
                model.addAttribute("error", "No product found with ID: " + id);
            } else {
                model.addAttribute("product", product.get());
            }
        } else if (name != null && !name.trim().isEmpty()) {
            List<Product> results = productService.findByName(name);
            if (results.isEmpty()) {
                model.addAttribute("error", "No products found matching: " + name);
            } else if (results.size() == 1) {
                model.addAttribute("product", results.get(0));
            } else {
                model.addAttribute("results", results);
            }
        }
        return "admin/delete";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam int id, RedirectAttributes redirectAttributes) {
        productService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Product deleted successfully.");
        return "redirect:/admin/products";
    }
}
