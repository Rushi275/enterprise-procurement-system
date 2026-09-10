package EnterpriseProcurementSystem.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import EnterpriseProcurementSystem.dto.PaymentRequest;
import EnterpriseProcurementSystem.dto.PaymentResponse;
import EnterpriseProcurementSystem.entity.Order;
import EnterpriseProcurementSystem.entity.Payment;
import EnterpriseProcurementSystem.entity.Product;
import EnterpriseProcurementSystem.entity.Request;
import EnterpriseProcurementSystem.entity.Supplier;
import EnterpriseProcurementSystem.enums.PaymentStatus;
import EnterpriseProcurementSystem.enums.RequestStatus;
import EnterpriseProcurementSystem.enums.SupplierOrderStatus;
import EnterpriseProcurementSystem.repository.OrderRepository;
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
    private OrderRepository orderRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

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

        Product product = request.getProduct();

        if (product == null) {
            throw new RuntimeException("Request product not found");
        }

        List<Supplier> suppliers = supplierRepository.findByProduct(product);

        boolean supplierMatches = suppliers.stream()
                .anyMatch(item -> item.getSupplierId().equals(supplier.getSupplierId()));

        if (!supplierMatches) {
            throw new RuntimeException("Supplier does not supply this product");
        }

        Payment payment = new Payment();

        payment.setRequest(request);
        payment.setSupplier(supplier);
        payment.setAmount(request.getTotalPrice());
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setTransactionId(generateTransactionId());
        payment.setPaymentDate(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        Order order = new Order();

        order.setRequest(request);
        order.setSupplier(supplier);
        order.setStatus(SupplierOrderStatus.RECEIVED);
        order.setCreatedDate(LocalDateTime.now());
        order.setUpdatedDate(LocalDateTime.now());

        orderRepository.save(order);

        emailService.sendEmail(
                supplier.getEmail(),
                "Procurement Order Received - Request #" + request.getRequestId(),
                "Hello " + supplier.getName() + ",\n\n"
                        + "A procurement order has been successfully processed.\n\n"
                        + "Request ID: " + request.getRequestId() + "\n"
                        + "Product: " + product.getName() + "\n"
                        + "Quantity: " + request.getNumberOfQuantities() + "\n"
                        + "Amount: ₹" + request.getTotalPrice() + "\n"
                        + "Transaction ID: " + savedPayment.getTransactionId() + "\n"
                        + "Order Status: " + order.getStatus() + "\n\n"
                        + "Please process the order accordingly.\n\n"
                        + "Enterprise Procurement System"
        );

        return savedPayment;
    }

    public List<PaymentResponse> getPayments() {

        List<Payment> payments = paymentRepository.findAll();

        return payments.stream().map(payment -> {

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

        }).toList();
    }

    private String generateTransactionId() {

        return "TXN-" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();
    }
}