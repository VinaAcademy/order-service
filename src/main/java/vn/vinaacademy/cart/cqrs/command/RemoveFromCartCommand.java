package vn.vinaacademy.cart.cqrs.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;

import java.util.UUID;

@Data
@Builder
public class RemoveFromCartCommand {
    @NotBlank(message = "ID người dùng không được để trống")
    private String userId;
    @NotNull(message = "ID khóa học không được để trống")
    private UUID courseId;
}