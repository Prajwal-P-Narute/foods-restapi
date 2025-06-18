package in.my.foods.service;

import in.my.foods.entity.CartEntity;
import in.my.foods.io.CartRequest;
import in.my.foods.io.CartResponse;
import in.my.foods.repository.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final UserService userService;

    @Override
    public CartResponse addToCart(CartRequest request) {
      String loggedInUserId = userService.findByUserId();
        Optional<CartEntity> cartOptional = cartRepository.findByUserId(loggedInUserId);
        CartEntity cart =  cartOptional.orElseGet(() -> new CartEntity(loggedInUserId, new HashMap<>()));
        Map<String, Integer>cartItems =  cart.getItems();
        cartItems.put(request.getFoodId(), cartItems.getOrDefault(request.getFoodId(), 0) + 1);
        cart.setItems(cartItems);
        cart = cartRepository.save(cart);
        return convertToResponse(cart);

    }

    @Override
    public CartResponse getCart() {
        String loggedInUserId = userService.findByUserId();

        CartEntity entity = cartRepository.findByUserId(loggedInUserId)
                .orElse(new CartEntity(null, loggedInUserId, new HashMap<>()));
        return convertToResponse(entity);
    }

    @Override
    public void clearCart() {
        String loggedInUserId = userService.findByUserId();
        cartRepository.deleteByUserId(loggedInUserId);
    }

    @Override
    public void deleteItemFromCart(String foodId) {
        String loggedInUserId = userService.findByUserId();
        CartEntity entity = cartRepository.findByUserId(loggedInUserId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Map<String, Integer> cartItems = entity.getItems();

        if (cartItems.containsKey(foodId)) {
            cartItems.remove(foodId);
            cartRepository.save(entity);
        }
    }


    @Override
    public CartResponse removeFromCart(CartRequest cartRequest) {
        String loggedInUserId = userService.findByUserId();
        CartEntity entity = cartRepository.findByUserId(loggedInUserId)
                .orElseThrow(() -> new RuntimeException(("Cart is Not Found")));

        Map<String, Integer>cartItems = entity.getItems();
        if(cartItems.containsKey(cartRequest.getFoodId())){
            int currentQty = cartItems.get(cartRequest.getFoodId());
            if (currentQty <= 1) {
                cartItems.remove(cartRequest.getFoodId()); // Remove the item
            } else {
                cartItems.put(cartRequest.getFoodId(), currentQty - 1); // Decrease qty
            }

            entity = cartRepository.save(entity);

        }
        return convertToResponse(entity);
    }

    private CartResponse convertToResponse(CartEntity cartEntity){
          return CartResponse.builder()
                  .id(cartEntity.getId())
                  .userId(cartEntity.getUserId())
                  .items(cartEntity.getItems())
                  .build();
    }
}
