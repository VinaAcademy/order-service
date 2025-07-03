package vn.vinaacademy.cart.exception;

public class CartNotFoundException extends CartException {
    public CartNotFoundException(String userId) {
        super("Cart not found for user: " + userId);
    }
}
