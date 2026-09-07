package EnterpriseProcurementSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import EnterpriseProcurementSystem.dto.SupplierOrderResponse;
import EnterpriseProcurementSystem.enums.SupplierOrderStatus;
import EnterpriseProcurementSystem.service.SupplierOrderService;

@RestController
@RequestMapping("/supplier/orders")
public class SupplierOrderController {

    @Autowired
    private SupplierOrderService supplierOrderService;

    @GetMapping
    public List<SupplierOrderResponse> getSupplierOrders(
            Authentication authentication) {

        String email = authentication.getName();

        return supplierOrderService.getSupplierOrders(email);
    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadSupplierOrders(
            Authentication authentication) {

        String email = authentication.getName();

        byte[] csv = supplierOrderService.downloadSupplierOrdersCsv(email);

        ByteArrayResource resource = new ByteArrayResource(csv);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=supplier-orders.csv"
                )
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(csv.length)
                .body(resource);
    }

    @PutMapping("/{orderId}/status")
    public SupplierOrderResponse updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody StatusRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        return supplierOrderService.updateOrderStatus(
                orderId,
                email,
                request.getStatus()
        );
    }

    public static class StatusRequest {

        private SupplierOrderStatus status;

        public SupplierOrderStatus getStatus() {
            return status;
        }

        public void setStatus(SupplierOrderStatus status) {
            this.status = status;
        }
    }
}