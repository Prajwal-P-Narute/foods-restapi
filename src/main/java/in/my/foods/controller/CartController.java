package in.my.foods.controller;

import in.my.foods.io.CartRequest;
import in.my.foods.io.CartResponse;
import in.my.foods.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public CartResponse addToCart(@RequestBody CartRequest request) {

        String foodId = request.getFoodId();
        if (foodId == null || foodId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "FoodId Not Found");
        }

        return cartService.addToCart(request);
    }

    @GetMapping
    public CartResponse getCart() {
        return cartService.getCart();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart() {
        cartService.clearCart();
    }


//    reduces quantity of a food item.
    @PostMapping("/remove")
    public CartResponse removeFromCart(@RequestBody CartRequest request) {
        String foodId = request.getFoodId();
        if (foodId == null || foodId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "FoodId Not Found");
        }
        return cartService.removeFromCart(request);
    }

//    delete a single food item entirely from the cart
    @DeleteMapping("/item")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItemFromCart(@RequestBody CartRequest request) {
        System.out.println(request);
        String foodId = request.getFoodId();
        if (foodId == null || foodId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "FoodId Not Found");
        }
        cartService.deleteItemFromCart(foodId);
    }

}
