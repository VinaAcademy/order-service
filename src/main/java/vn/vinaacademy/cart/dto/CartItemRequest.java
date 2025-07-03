package vn.vinaacademy.cart.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CartItemRequest {
    @NotNull(message = "Course ID is required")
    @JsonAlias({"courseId", "course_id"})
    private UUID courseId;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
}