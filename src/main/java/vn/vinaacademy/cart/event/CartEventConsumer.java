package vn.vinaacademy.cart.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import vn.vinaacademy.cart.constants.KafkaTopic;
import vn.vinaacademy.cart.entity.Cart;
import vn.vinaacademy.cart.repository.CartRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class CartEventConsumer {
    private final ObjectMapper objectMapper;
    private final CartRepository cartRepository; // Mongo repository

    @KafkaListener(topics = KafkaTopic.CART_UPDATE_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeCartUpdate(String message) throws JsonProcessingException {
        Cart cart = objectMapper.readValue(message, Cart.class);
        cartRepository.save(cart); // Persist to MongoDB
    }

    @KafkaListener(topics = KafkaTopic.CART_CLEARED_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeCartCleared(String message) throws JsonProcessingException {
        Cart cart = objectMapper.readValue(message, Cart.class);
        cartRepository.deleteByUserId(cart.getUserId());
        log.info("Cart cleared for user: {}", cart.getUserId());
    }
}