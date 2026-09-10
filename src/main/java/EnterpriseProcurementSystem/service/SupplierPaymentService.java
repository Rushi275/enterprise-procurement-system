package EnterpriseProcurementSystem.service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import EnterpriseProcurementSystem.dto.PaymentResponse;
import EnterpriseProcurementSystem.entity.Payment;
import EnterpriseProcurementSystem.entity.Supplier;
import EnterpriseProcurementSystem.entity.User;
import EnterpriseProcurementSystem.repository.PaymentRepository;
import EnterpriseProcurementSystem.repository.SupplierRepository;
import EnterpriseProcurementSystem.repository.UserRepository;

@Service
public class SupplierPaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    public List<PaymentResponse> getSupplierPayments(String email) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Supplier supplier = supplierRepository.findByUser(user);

        if (supplier == null) {
            throw new RuntimeException("Supplier not found");
        }

        return paymentRepository.findBySupplier(supplier)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public byte[] downloadSupplierPaymentsCsv(String email) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Supplier supplier = supplierRepository.findByUser(user);

        if (supplier == null) {
            throw new RuntimeException("Supplier not found");
        }

        List<Payment> payments = paymentRepository.findBySupplier(supplier);

        StringBuilder csv = new StringBuilder();

        csv.append(
                "Payment ID,Request ID,Amount,Payment Method,Transaction ID,Status,Payment Date\n"
        );

        for (Payment payment : payments) {
            csv.append(payment.getPaymentId()).append(",")
                    .append(payment.getRequest().getRequestId()).append(",")
                    .append(payment.getAmount()).append(",")
                    .append(payment.getPaymentMethod()).append(",")
                    .append(payment.getTransactionId()).append(",")
                    .append(payment.getStatus()).append(",")
                    .append(payment.getPaymentDate()).append("\n");
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private PaymentResponse convertToResponse(Payment payment) {

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
}