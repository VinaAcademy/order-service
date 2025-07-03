package vn.vinaacademy.cart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "carts")
public class Cart {

    @Id
    private String id;

    private UUID userId;

    private UUID couponId;

    private List<CartItem> cartItems = new ArrayList<>();

    @LastModifiedDate
    @JsonIgnore
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    public void addCartItem(CartItem item) {
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
        cartItems.add(item);
    }

    public void removeCartItem(UUID courseId) {
        if (cartItems == null) {
            return;
        }
        cartItems.removeIf(i -> i.getCourseId().equals(courseId));
    }

    @Override
    public String toString() {
        return "Cart{id=" + id + ", userId=" + userId + '}';
    }
}
