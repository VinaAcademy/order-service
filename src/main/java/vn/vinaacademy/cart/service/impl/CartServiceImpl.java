package vn.vinaacademy.cart.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.vinaacademy.cart.cqrs.CartCommandHandler;
import vn.vinaacademy.cart.cqrs.CartQueryHandler;
import vn.vinaacademy.cart.cqrs.command.AddToCartCommand;
import vn.vinaacademy.cart.cqrs.command.ClearCartCommand;
import vn.vinaacademy.cart.cqrs.command.RemoveFromCartCommand;
import vn.vinaacademy.cart.cqrs.query.GetCartQuery;
import vn.vinaacademy.cart.dto.CartDto;
import vn.vinaacademy.cart.dto.CartItemRequest;
import vn.vinaacademy.cart.mapper.CartMapper;
import vn.vinaacademy.cart.service.CartService;
import vn.vinaacademy.common.security.SecurityContextHelper;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartCommandHandler cartCommand;
    private final CartQueryHandler cartQuery;

    @Autowired
    private SecurityContextHelper securityContextHelper;

    @Override
    public CartDto getCurrentUserCart() {
        String userId = securityContextHelper.getCurrentUserId();
        GetCartQuery getCartQuery = GetCartQuery.builder()
                .userId(userId)
                .build();
        log.info("Fetching cart for user: {}", userId);
        return CartMapper.INSTANCE.toDTO(cartQuery.handle(getCartQuery));
    }

    @Override
    public void addToCart(CartItemRequest request) {
        String userId = securityContextHelper.getCurrentUserId();
        AddToCartCommand addToCartCommand = AddToCartCommand.builder()
                .userId(userId)
                .courseId(request.getCourseId())
                .price(request.getPrice())
                .build();
        cartCommand.handle(addToCartCommand);
        log.info("Added course {} to cart for user: {}", request.getCourseId(), userId);
    }

    @Override
    public void removeFromCart(UUID courseId) {
        String userId = securityContextHelper.getCurrentUserId();

        RemoveFromCartCommand removeFromCartCommand = RemoveFromCartCommand.builder()
                .userId(userId)
                .courseId(courseId)
                .build();

        cartCommand.handle(removeFromCartCommand);
        log.info("Removed course {} from cart for user: {}", courseId, userId);
    }

    @Override
    public void clearCart() {
        String userId = securityContextHelper.getCurrentUserId();
        ClearCartCommand clearCartCommand = ClearCartCommand.builder()
                .userId(userId)
                .build();
        cartCommand.handle(clearCartCommand);
        log.info("Cart cleared for user: {}", userId);
    }
}
