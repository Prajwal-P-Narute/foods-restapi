package in.my.foods.service;

import in.my.foods.io.CartRequest;
import in.my.foods.io.CartResponse;

public interface CartService {

    CartResponse addToCart(CartRequest request);
    CartResponse getCart();

    void clearCart();

    CartResponse removeFromCart(CartRequest cartRequest);

    void deleteItemFromCart(String foodId);



}
