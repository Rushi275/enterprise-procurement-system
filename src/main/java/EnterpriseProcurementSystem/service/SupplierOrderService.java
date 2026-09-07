package EnterpriseProcurementSystem.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import EnterpriseProcurementSystem.dto.SupplierOrderResponse;
import EnterpriseProcurementSystem.entity.Order;
import EnterpriseProcurementSystem.entity.Supplier;
import EnterpriseProcurementSystem.entity.User;
import EnterpriseProcurementSystem.enums.SupplierOrderStatus;
import EnterpriseProcurementSystem.repository.OrderRepository;
import EnterpriseProcurementSystem.repository.SupplierRepository;
import EnterpriseProcurementSystem.repository.UserRepository;

@Service
public class SupplierOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    public List<SupplierOrderResponse> getSupplierOrders(String email) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Supplier supplier = supplierRepository.findByUser(user);

        if (supplier == null) {
            throw new RuntimeException("Supplier not found");
        }

        return orderRepository.findBySupplier(supplier)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public SupplierOrderResponse updateOrderStatus(
            Long orderId,
            String email,
            SupplierOrderStatus newStatus) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Supplier supplier = supplierRepository.findByUser(user);

        if (supplier == null) {
            throw new RuntimeException("Supplier not found");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getSupplier().getSupplierId()
                .equals(supplier.getSupplierId())) {

            throw new RuntimeException(
                    "You are not authorized to update this order");
        }

        SupplierOrderStatus currentStatus = order.getStatus();

        if (!isValidNextStatus(currentStatus, newStatus)) {
            throw new RuntimeException(
                    "Invalid status transition from "
                            + currentStatus
                            + " to "
                            + newStatus);
        }

        order.setStatus(newStatus);
        order.setUpdatedDate(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        return convertToResponse(savedOrder);
    }

    private boolean isValidNextStatus(
            SupplierOrderStatus currentStatus,
            SupplierOrderStatus newStatus) {

        if (currentStatus == SupplierOrderStatus.RECEIVED
                && newStatus == SupplierOrderStatus.PACKED) {
            return true;
        }

        if (currentStatus == SupplierOrderStatus.PACKED
                && newStatus == SupplierOrderStatus.SHIPPED) {
            return true;
        }

        if (currentStatus == SupplierOrderStatus.SHIPPED
                && newStatus == SupplierOrderStatus.DELIVERED) {
            return true;
        }

        if (currentStatus == SupplierOrderStatus.DELIVERED
                && newStatus == SupplierOrderStatus.COMPLETED) {
            return true;
        }

        return false;
    }

    private SupplierOrderResponse convertToResponse(Order order) {

        SupplierOrderResponse response = new SupplierOrderResponse();

        response.setOrderId(order.getOrderId());
        response.setRequestId(order.getRequest().getRequestId());
        response.setSupplierId(order.getSupplier().getSupplierId());
        response.setProductName(order.getRequest().getProduct().getName());
        response.setQuantity(order.getRequest().getNumberOfQuantities());
        response.setAmount(order.getRequest().getTotalPrice());
        response.setStatus(order.getStatus());
        response.setCreatedDate(order.getCreatedDate());
        response.setUpdatedDate(order.getUpdatedDate());

        return response;
    }
}