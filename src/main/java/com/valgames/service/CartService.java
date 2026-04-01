package com.valgames.service;

import com.valgames.model.CartItem;
import com.valgames.model.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @SuppressWarnings("unchecked")
    private List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute( "cart", cart);
        }
        return cart;
    }

    public List<CartItem> getItems(HttpSession session) {
        return getCart(session);
    }

    public String addItem(HttpSession session, Product product, int quantity) {
        if (quantity <= 0) return "Quantity must be greater than 0.";

        List<CartItem> cart = getCart(session);
        for (CartItem item : cart) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return null;
            }
        }
        cart.add(new CartItem(product, quantity));
        return null;
    }

    public void removeItem(HttpSession session, int productId) {
        getCart(session).removeIf(i -> i.getProduct().getId().equals(productId));
    }

    public String updateQuantity(HttpSession session, int productId, int newQuantity) {
        if (newQuantity < 0) return "Invalid quantity.";
        List<CartItem> cart = getCart(session);
        if (newQuantity == 0) {
            cart.removeIf(i -> i.getProduct().getId().equals(productId));
            return null;
        }
        for (CartItem item : cart) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(newQuantity);
                return null;
            }
        }
        return "Item not found.";
    }

    public double getTotal(HttpSession session) {
        return getCart(session).stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
    }

    public int getCount(HttpSession session) {
        return getCart(session).stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public void clear(HttpSession session) {
        session.removeAttribute( "cart");
    }
}