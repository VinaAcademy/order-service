package vn.vinaacademy.cart.cqrs.query;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class GetCartQuery {
    private String userId;
}