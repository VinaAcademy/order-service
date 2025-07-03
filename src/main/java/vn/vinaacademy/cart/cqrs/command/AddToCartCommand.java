package vn.vinaacademy.cart.cqrs.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class AddToCartCommand {
    @NotBlank(message = "ID người dùng không được để trống")
    private String userId;
    @NotNull(message = "ID khóa học không được để trống")
    private UUID courseId;
    @NotNull(message = "Số lượng khóa học không được để trống")
    @Min(value = 0, message = "Số lượng khóa học phải lớn hơn bằng 0")
    private BigDecimal price;
}