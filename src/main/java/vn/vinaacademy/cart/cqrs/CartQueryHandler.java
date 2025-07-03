package vn.vinaacademy.cart.cqrs;


import vn.vinaacademy.cart.cqrs.query.GetCartQuery;
import vn.vinaacademy.cart.entity.Cart;

public interface CartQueryHandler {
    Cart handle(GetCartQuery getCartQuery);
}
