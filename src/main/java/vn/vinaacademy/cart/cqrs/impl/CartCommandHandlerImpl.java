package vn.vinaacademy.cart.cqrs.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import vn.vinaacademy.cart.cqrs.CartCommandHandler;
import vn.vinaacademy.cart.cqrs.command.AddToCartCommand;
import vn.vinaacademy.cart.cqrs.command.ClearCartCommand;
import vn.vinaacademy.cart.cqrs.command.RemoveFromCartCommand;
import vn.vinaacademy.cart.entity.Cart;
import vn.vinaacademy.cart.entity.CartItem;
import vn.vinaacademy.common.exception.BadRequestException;
import vn.vinaacademy.cart.constants.KafkaTopic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static vn.vinaacademy.cart.constants.RedisKey.CART_KEY_PREFIX;

@Component
@Slf4j
@RequiredArgsConstructor
public class CartCommandHandlerImpl implements CartCommandHandler {
    private final RedisTemplate<String, Object> redisTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(@Valid AddToCartCommand addToCartCommand) {
        String userId = addToCartCommand.getUserId();
        String redisKey = CART_KEY_PREFIX + userId;

        Cart cart = getOrCreate(redisKey, userId);

        // check items already exist
        boolean itemExists = cart.getCartItems().stream()
                .anyMatch(item -> addToCartCommand.getCourseId().equals(item.getCourseId()));

        if (itemExists) {
            throw BadRequestException.message("Khóa học đã có trong giỏ hàng");
        }

        CartItem item = CartItem.builder()
                .price(addToCartCommand.getPrice())
                .courseId(addToCartCommand.getCourseId())
                .build();
        cart.getCartItems().add(item);
        updateCartMetadata(cart);
        redisTemplate.opsForValue().set(redisKey, cart);
        publishCartUpdate(userId, cart);
        log.info("Added course {} to cart for user {}", addToCartCommand.getCourseId(), userId);
    }

    @Override
    public void handle(@Valid RemoveFromCartCommand removeFromCartCommand) {
        String userId = removeFromCartCommand.getUserId();
        String redisKey = CART_KEY_PREFIX + userId;

        Cart cart = getOrCreate(redisKey, userId);

        boolean isItemRemoved = cart.getCartItems().removeIf(item ->
                removeFromCartCommand.getCourseId().equals(item.getCourseId()));
        if (!isItemRemoved) {
            throw BadRequestException.message("Khóa học không có trong giỏ hàng");
        }

        updateCartMetadata(cart);
        redisTemplate.opsForValue().set(redisKey, cart);
        publishCartUpdate(userId, cart);
        log.info("Removed course {} from cart for user {}", removeFromCartCommand.getCourseId(), userId);
    }

    @Override
    public void handle(ClearCartCommand clearCartCommand) {
        String userId = clearCartCommand.getUserId();
        String redisKey = CART_KEY_PREFIX + userId;

        redisTemplate.delete(redisKey);
        kafkaTemplate.send(KafkaTopic.CART_CLEARED_TOPIC, userId, userId);

        log.info("Cleared cart for user {}", userId);
    }


    private Cart getOrCreate(String redisKey, String userId) {
        Cart cart = objectMapper.convertValue(
                redisTemplate.opsForValue().get(redisKey),
                Cart.class);
        if (cart == null) {
            cart = Cart.builder()
                    .userId(UUID.fromString(userId))
                    .cartItems(new ArrayList<>())
                    .updatedAt(LocalDateTime.now())
                    .version(0L)
                    .build();
        }
        return cart;
    }

    private void publishCartUpdate(String userId, Cart cart) {
        try {
            kafkaTemplate.send(KafkaTopic.CART_UPDATE_TOPIC, userId,
                    objectMapper.writeValueAsString(cart));
        } catch (Exception e) {
            log.error("Failed to publish cart update for user {}: {}", userId, e.getMessage());
        }
    }

    private void updateCartMetadata(Cart cart) {
        cart.setUpdatedAt(LocalDateTime.now());
        cart.setVersion(cart.getVersion() + 1);
    }
}
