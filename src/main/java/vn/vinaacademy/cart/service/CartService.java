package vn.vinaacademy.cart.service;

import jakarta.validation.Valid;
import vn.vinaacademy.cart.dto.CartDto;
import vn.vinaacademy.cart.dto.CartItemRequest;

import java.util.UUID;

public interface CartService {
    CartDto getCurrentUserCart();

    void addToCart(@Valid CartItemRequest request);

    void removeFromCart(UUID courseId);

    void clearCart();
}
