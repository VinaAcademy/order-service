package vn.vinaacademy.cart.exception;

public class DuplicateCartItemException extends CartException {
    public DuplicateCartItemException(String courseId) {
        super("Course already exists in cart: " + courseId);
    }
}
