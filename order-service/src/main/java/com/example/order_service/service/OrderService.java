package com.example.order_service.service;

import com.example.order_service.feign.ProductClient;
import com.example.order_service.model.Order;
import com.example.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductClient productClient;

    @Transactional
    public Order placeOrder(Long productId, Integer quantity) {
        ProductClient.ProductResponse product = productClient.getProductById(productId);

        if (product != null && quantity > 0) {
            Double totalPrice = product.getPrice() * quantity;

            Order order = new Order();
            order.setProductId(productId);
            order.setQuantity(quantity);
            order.setTotalPrice(totalPrice);

            return orderRepository.save(order);
        }
        throw new IllegalArgumentException("Invalid product or quantity");
    }
}
