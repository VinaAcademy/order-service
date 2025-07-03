package vn.vinaacademy.cart.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vn.vinaacademy.cart.dto.CartDto;
import vn.vinaacademy.cart.dto.CartItemDto;
import vn.vinaacademy.cart.entity.Cart;
import vn.vinaacademy.cart.entity.CartItem;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    CartDto toDTO(Cart cart);

    @InheritInverseConfiguration
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Cart toEntity(CartDto cartDTO);

    List<CartItemDto> toCartItemDTOList(List<CartItem> cartItems);

    CartItemDto toCartItemDTO(CartItem cartItem);

    List<CartItem> toCartItemEntityList(List<CartItemDto> cartItemDTOs);

    @InheritInverseConfiguration
    @Mapping(target = "addedAt", ignore = true)
    CartItem toCartItemEntity(CartItemDto cartItemDto);
}