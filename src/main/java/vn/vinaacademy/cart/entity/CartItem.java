package vn.vinaacademy.cart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "carts")
public class CartItem {

    @Field("course_id")
    private UUID courseId;

    private BigDecimal price;

    @CreatedDate
    @JsonIgnore
    private LocalDateTime addedAt;
}
