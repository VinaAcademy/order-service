package vn.vinaacademy.cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.vinaacademy.cart.dto.CartDto;
import vn.vinaacademy.cart.dto.CartItemRequest;
import vn.vinaacademy.cart.service.CartService;
import vn.vinaacademy.common.response.ApiResponse;
import vn.vinaacademy.common.security.annotation.HasAnyRole;
import vn.vinaacademy.common.constant.AuthConstants;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Cart", description = "Quản lý giỏ hàng")
public class CartController {

    private final CartService cartService;

    @GetMapping("/me")
    @HasAnyRole({AuthConstants.STUDENT_ROLE})
    @Operation(summary = "Lấy giỏ hàng của người dùng hiện tại", description = "Lấy giỏ hàng của người dùng đã đăng nhập")
    public ApiResponse<CartDto> getCurrentUserCart() {
        log.debug("Fetching cart for current user");
        CartDto cart = cartService.getCurrentUserCart();
        return ApiResponse.success("Lấy giỏ hàng thành công", cart);
    }

    @PostMapping("/items")
    @HasAnyRole({AuthConstants.STUDENT_ROLE})
    @Operation(summary = "Thêm khóa học vào giỏ hàng", description = "Thêm một khóa học mới vào giỏ hàng")
    public ApiResponse<Void> addToCart(@Valid @RequestBody CartItemRequest request) {
        log.debug("Adding course {} to cart", request.getCourseId());
        cartService.addToCart(request);
        return ApiResponse.success("Đã thêm khóa học vào giỏ hàng");
    }

    @DeleteMapping("/items/{courseId}")
    @HasAnyRole({AuthConstants.STUDENT_ROLE})
    @Operation(summary = "Xóa khóa học khỏi giỏ hàng", description = "Xóa một khóa học khỏi giỏ hàng")
    public ApiResponse<Void> removeFromCart(@PathVariable UUID courseId) {
        log.debug("Removing course {} from cart", courseId);
        cartService.removeFromCart(courseId);
        return ApiResponse.success("Đã xóa khóa học khỏi giỏ hàng");
    }

    @DeleteMapping("/clear")
    @HasAnyRole({AuthConstants.STUDENT_ROLE})
    @Operation(summary = "Xóa toàn bộ giỏ hàng", description = "Xóa tất cả khóa học trong giỏ hàng")
    public ApiResponse<Void> clearCart() {
        log.debug("Clearing cart for current user");
        cartService.clearCart();
        return ApiResponse.success("Đã xóa toàn bộ giỏ hàng");
    }

    @GetMapping("/count")
    @HasAnyRole({AuthConstants.STUDENT_ROLE})
    @Operation(summary = "Lấy số lượng khóa học trong giỏ hàng", description = "Trả về số lượng khóa học hiện có trong giỏ hàng")
    public ApiResponse<Integer> getCartItemCount() {
        log.debug("Getting cart item count for current user");
        CartDto cart = cartService.getCurrentUserCart();
        int count = cart.getCartItems() != null ? cart.getCartItems().size() : 0;
        return ApiResponse.success("Lấy số lượng giỏ hàng thành công", count);
    }
}