package com.valgames.service;

import com.valgames.model.Product;
import com.valgames.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findByName(String name) {
        return productRepository.findByProductNameContainingIgnoreCase(name);
    }

    public List<Product> findByCategoryId(int categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public Optional<Product> findById(int id) {
        return productRepository.findById(id);
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public void delete(int id) {
        productRepository.deleteById(id);
    }
}
