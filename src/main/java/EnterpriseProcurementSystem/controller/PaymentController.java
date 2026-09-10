package EnterpriseProcurementSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import EnterpriseProcurementSystem.dto.PaymentRequest;
import EnterpriseProcurementSystem.dto.PaymentResponse;
import EnterpriseProcurementSystem.entity.Payment;
import EnterpriseProcurementSystem.service.PaymentService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public PaymentResponse processPayment(
            @RequestBody PaymentRequest paymentRequest) {

        Payment payment = paymentService.processPayment(paymentRequest);

        PaymentResponse response = new PaymentResponse();

        response.setPaymentId(payment.getPaymentId());
        response.setRequestId(payment.getRequest().getRequestId());
        response.setSupplierId(payment.getSupplier().getSupplierId());
        response.setAmount(payment.getAmount());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setTransactionId(payment.getTransactionId());
        response.setStatus(payment.getStatus());
        response.setPaymentDate(payment.getPaymentDate());

        return response;
    }

    @GetMapping
    public List<PaymentResponse> getPayments() {
        return paymentService.getPayments();
    }
}