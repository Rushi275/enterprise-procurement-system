package EnterpriseProcurementSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import EnterpriseProcurementSystem.dto.OrderTrackingResponse;
import EnterpriseProcurementSystem.entity.Order;
import EnterpriseProcurementSystem.entity.User;
import EnterpriseProcurementSystem.repository.OrderRepository;
import EnterpriseProcurementSystem.repository.UserRepository;

@Service
public class OrderTrackingService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    public OrderTrackingResponse getOrderTracking(
            Long orderId,
            String email) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getRequest().getUser().getUserId()
                .equals(user.getUserId())) {

            throw new RuntimeException(
                    "You are not authorized to view this order");
        }

        OrderTrackingResponse response = new OrderTrackingResponse();

        response.setOrderId(order.getOrderId());
        response.setRequestId(order.getRequest().getRequestId());
        response.setProductName(order.getRequest().getProduct().getName());
        response.setSupplierName(order.getSupplier().getName());
        response.setStatus(order.getStatus());
        response.setUpdatedDate(order.getUpdatedDate());

        return response;
    }
}