package vn.vinaacademy.cart.exception;

public class CartItemNotFoundException extends CartException {
    public CartItemNotFoundException(String courseId) {
        super("Cart item not found for course: " + courseId);
    }
}
