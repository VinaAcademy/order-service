package vn.vinaacademy.cart.cqrs.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import vn.vinaacademy.cart.constants.RedisKey;
import vn.vinaacademy.cart.cqrs.CartQueryHandler;
import vn.vinaacademy.cart.cqrs.query.GetCartQuery;
import vn.vinaacademy.cart.entity.Cart;
import vn.vinaacademy.cart.repository.CartRepository;
import vn.vinaacademy.common.security.SecurityContextHelper;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartQueryHandlerImpl implements CartQueryHandler {
    private final RedisTemplate<String, Object> redisTemplate;
    private final CartRepository cartRepository;
    @Autowired
    private SecurityContextHelper securityContextHelper;
    private final ObjectMapper objectMapper;

    @Override
    public Cart handle(GetCartQuery getCartQuery) {
        String userId = getCartQuery.getUserId();
        userId = StringUtils.isNotBlank(userId) ? userId :
                securityContextHelper.getCurrentUserId();

        String key = RedisKey.CART_KEY_PREFIX + userId;
        Cart cart = objectMapper.convertValue(
                redisTemplate.opsForValue().get(key),
                Cart.class);
        if (cart != null) {
            log.debug("Cart found in Redis for userId: {}", userId);
            return cart;
        }

        cart = cartRepository.findByUserId(UUID.fromString(userId))
                .orElse(null);

        if (cart != null) {
            log.debug("Cart found in database for userId: {}", userId);
            redisTemplate.opsForValue().set(key, cart);
            return cart;
        }

        log.debug("No cart found for userId: {}", userId);

        // Create a new cart if it doesn't exist
        cart = Cart.builder()
                .userId(UUID.fromString(userId))
                .cartItems(new ArrayList<>())
                .build();
        cartRepository.save(cart);
        redisTemplate.opsForValue().set(key, cart);
        log.debug("New cart created for userId: {}", userId);
        return cart;
    }
}
