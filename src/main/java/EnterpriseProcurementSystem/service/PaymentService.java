package EnterpriseProcurementSystem.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import EnterpriseProcurementSystem.dto.PaymentRequest;
import EnterpriseProcurementSystem.entity.Payment;
import EnterpriseProcurementSystem.entity.Request;
import EnterpriseProcurementSystem.entity.Supplier;
import EnterpriseProcurementSystem.enums.PaymentStatus;
import EnterpriseProcurementSystem.enums.RequestStatus;
import EnterpriseProcurementSystem.repository.PaymentRepository;
import EnterpriseProcurementSystem.repository.RequestRepository;
import EnterpriseProcurementSystem.repository.SupplierRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Payment processPayment(PaymentRequest paymentRequest) {

        Request request = requestRepository.findById(paymentRequest.getRequestId())
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() != RequestStatus.APPROVED) {
            throw new RuntimeException("Request is not approved for payment");
        }

        Supplier supplier = supplierRepository.findById(paymentRequest.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        if (supplier.getMpinHash() == null) {
            throw new RuntimeException("Supplier MPIN is not set");
        }

        if (!passwordEncoder.matches(
                paymentRequest.getMpin(),
                supplier.getMpinHash())) {
            throw new RuntimeException("Invalid supplier MPIN");
        }

        Payment existingPayment = paymentRepository.findByRequest(request);

        if (existingPayment != null) {
            throw new RuntimeException("Payment already exists for this request");
        }

        if (paymentRequest.getPaymentMethod() == null) {
            throw new RuntimeException("Payment method is required");
        }

        if (request.getTotalPrice() == null || request.getTotalPrice() <= 0) {
            throw new RuntimeException("Invalid request amount");
        }

        Payment payment = new Payment();

        payment.setRequest(request);
        payment.setSupplier(supplier);
        payment.setAmount(request.getTotalPrice());
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setTransactionId(generateTransactionId());
        payment.setPaymentDate(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();
    }
}