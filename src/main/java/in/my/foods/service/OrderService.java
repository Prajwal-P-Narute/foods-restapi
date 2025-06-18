package in.my.foods.service;

import in.my.foods.io.OrderRequest;
import in.my.foods.io.OrderResponse;

import java.util.List;
import java.util.Map;

public interface OrderService {

    OrderResponse createOrderWithPayment(OrderRequest request);
    void verifyPayment(Map<String, String>paymentData, String status);
    List<OrderResponse> getUserOrders();

    void removeOrder(String orderId);

    List<OrderResponse> getOrdersOfAllUsers();

    void updateOrderStatus(String orderId, String status);
}
