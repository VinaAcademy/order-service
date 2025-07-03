package vn.vinaacademy.cart.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import vn.vinaacademy.cart.entity.Cart;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findByUserId(UUID userId);
    void deleteByUserId(UUID userId);
}