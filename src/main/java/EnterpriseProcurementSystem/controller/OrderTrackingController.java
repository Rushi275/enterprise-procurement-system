package EnterpriseProcurementSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import EnterpriseProcurementSystem.dto.OrderTrackingResponse;
import EnterpriseProcurementSystem.service.OrderTrackingService;

@RestController
@RequestMapping("/orders")
public class OrderTrackingController {

    @Autowired
    private OrderTrackingService orderTrackingService;

    @GetMapping("/{orderId}/tracking")
    public OrderTrackingResponse getOrderTracking(
            @PathVariable Long orderId,
            Authentication authentication) {

        String email = authentication.getName();

        return orderTrackingService.getOrderTracking(
                orderId,
                email
        );
    }
}