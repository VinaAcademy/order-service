package vn.vinaacademy.cart.cqrs;


import com.fasterxml.jackson.core.JsonProcessingException;
import vn.vinaacademy.cart.cqrs.command.AddToCartCommand;
import vn.vinaacademy.cart.cqrs.command.ClearCartCommand;
import vn.vinaacademy.cart.cqrs.command.RemoveFromCartCommand;

public interface CartCommandHandler {
    void handle(AddToCartCommand addToCartCommand);

    void handle(RemoveFromCartCommand removeFromCartCommand);

    void handle(ClearCartCommand clearCartCommand);
}
