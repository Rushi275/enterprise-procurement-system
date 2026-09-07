package EnterpriseProcurementSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import EnterpriseProcurementSystem.service.SupplierPaymentService;

@RestController
@RequestMapping("/supplier/payments")
public class SupplierPaymentController {

    @Autowired
    private SupplierPaymentService supplierPaymentService;

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadSupplierPayments(
            Authentication authentication) {

        String email = authentication.getName();

        byte[] csv =
                supplierPaymentService.downloadSupplierPaymentsCsv(email);

        ByteArrayResource resource = new ByteArrayResource(csv);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=supplier-payment-history.csv"
                )
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(csv.length)
                .body(resource);
    }
}