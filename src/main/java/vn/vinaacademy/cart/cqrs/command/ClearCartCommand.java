package vn.vinaacademy.cart.cqrs.command;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class ClearCartCommand {
    private String userId;
}