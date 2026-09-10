package EnterpriseProcurementSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import EnterpriseProcurementSystem.dto.OrderTrackingResponse;
import EnterpriseProcurementSystem.entity.Admin;
import EnterpriseProcurementSystem.entity.Order;
import EnterpriseProcurementSystem.entity.User;
import EnterpriseProcurementSystem.repository.AdminRepository;
import EnterpriseProcurementSystem.repository.OrderRepository;
import EnterpriseProcurementSystem.repository.UserRepository;

@Service
public class OrderTrackingService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    public OrderTrackingResponse getOrderTrackingByRequestId(
            Long requestId,
            String email,
            String role) {

        Order order = orderRepository.findByRequestRequestId(requestId);

        if (order == null) {
            throw new RuntimeException("Order not found for this request");
        }

        if ("ADMIN".equals(role)) {
            Admin admin = adminRepository.findByEmail(email);

            if (admin == null) {
                throw new RuntimeException("Admin not found");
            }
        } else {
            User user = userRepository.findByEmail(email);

            if (user == null) {
                throw new RuntimeException("User not found");
            }

            boolean isOwner = order.getRequest().getUser().getUserId()
                    .equals(user.getUserId());

            if (!isOwner) {
                throw new RuntimeException(
                        "You are not authorized to view this order");
            }
        }

        OrderTrackingResponse response = new OrderTrackingResponse();

        response.setOrderId(order.getOrderId());
        response.setRequestId(order.getRequest().getRequestId());
        response.setProductName(order.getRequest().getProduct().getName());
        response.setSupplierName(order.getSupplier().getName());
        response.setStatus(order.getStatus());
        response.setRequestStatus(order.getRequest().getStatus());
        response.setUpdatedDate(order.getUpdatedDate());

        return response;
    }
}