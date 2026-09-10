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

    @GetMapping("/request/{requestId}/tracking")
    public OrderTrackingResponse getOrderTracking(
            @PathVariable Long requestId,
            Authentication authentication) {

        String email = authentication.getName();

        String role = authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                .orElse("");

        return orderTrackingService.getOrderTrackingByRequestId(
                requestId,
                email,
                role);
    }
}