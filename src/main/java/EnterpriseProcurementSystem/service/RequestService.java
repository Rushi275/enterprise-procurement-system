package EnterpriseProcurementSystem.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import EnterpriseProcurementSystem.dto.RequestStatusResponse;
import EnterpriseProcurementSystem.entity.Admin;
import EnterpriseProcurementSystem.entity.Department;
import EnterpriseProcurementSystem.entity.Product;
import EnterpriseProcurementSystem.entity.Request;
import EnterpriseProcurementSystem.entity.Supplier;
import EnterpriseProcurementSystem.entity.User;
import EnterpriseProcurementSystem.enums.NotificationType;
import EnterpriseProcurementSystem.enums.RequestStatus;
import EnterpriseProcurementSystem.repository.AdminRepository;
import EnterpriseProcurementSystem.repository.DepartmentRepository;
import EnterpriseProcurementSystem.repository.ProductRepository;
import EnterpriseProcurementSystem.repository.RequestRepository;
import EnterpriseProcurementSystem.repository.SupplierRepository;
import EnterpriseProcurementSystem.repository.UserRepository;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private UserRepository userRepository;

    public Request raiseRequest(Request request) {

        Product product = productRepository
                .findById(request.getProduct().getProductId())
                .orElse(null);

        if (product == null) {
            return null;
        }

        Department department = departmentRepository
                .findById(request.getDepartment().getDepartmentId())
                .orElse(null);

        if (department == null) {
            return null;
        }

        User user = userRepository
                .findById(request.getUser().getUserId())
                .orElse(null);

        if (user == null) {
            return null;
        }

        request.setUser(user);
        request.setProduct(product);
        request.setDepartment(department);
        request.setStatus(RequestStatus.PENDING);
        request.setCreatedDate(LocalDateTime.now());
        request.setUpdatedDate(LocalDateTime.now());

        double totalPrice =
                product.getPricePerProduct() * request.getNumberOfQuantities();

        request.setTotalPrice(totalPrice);

        Request savedRequest = requestRepository.save(request);

        if (department.getManager() != null) {

            String message =
                    "New procurement request requires your approval.\n\n"
                    + "Request Details:\n"
                    + "Request ID: " + savedRequest.getRequestId() + "\n"
                    + "Employee: " + user.getName() + "\n"
                    + "Product: " + product.getName() + "\n"
                    + "Quantity: " + savedRequest.getNumberOfQuantities() + "\n"
                    + "Department: " + department.getDepartmentName() + "\n"
                    + "Total Price: " + savedRequest.getTotalPrice() + "\n"
                    + "Status: " + savedRequest.getStatus() + "\n\n"
                    + "Please review and approve or reject the request.";

            notificationService.createNotification(
                    "New procurement request requires your approval.",
                    department.getManager(),
                    savedRequest,
                    NotificationType.MANAGER_APPROVAL_REQUIRED
            );

            emailService.sendEmail(
                    department.getManager().getEmail(),
                    "Procurement Request Approval Required",
                    message
            );
        }

        return savedRequest;
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    public Request getRequestById(Long id) {
        return requestRepository.findById(id).orElse(null);
    }

    public List<Request> getPendingRequests() {
        return requestRepository.findByStatus(RequestStatus.PENDING);
    }

    public RequestStatusResponse updateRequestStatus(Long id, String status) {

        Request request = requestRepository.findById(id).orElse(null);

        if (request == null) {
            return null;
        }

        RequestStatus newStatus;

        try {
            newStatus = RequestStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        boolean isManager =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                authority.getAuthority().equals("ROLE_MANAGER"));

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                authority.getAuthority().equals("ROLE_ADMIN"));

        if (request.getStatus() == RequestStatus.PENDING &&
                newStatus == RequestStatus.MANAGER_APPROVED) {

            if (!isManager) {
                return null;
            }

            request.setStatus(RequestStatus.MANAGER_APPROVED);
            request.setUpdatedDate(LocalDateTime.now());

            Request savedRequest = requestRepository.save(request);

            List<Admin> admins = adminRepository.findAll();

            if (!admins.isEmpty()) {

                Admin admin = admins.get(0);

                String message =
                        "Manager has approved a procurement request.\n\n"
                        + "Request Details:\n"
                        + "Request ID: " + savedRequest.getRequestId() + "\n"
                        + "Employee: " + savedRequest.getUser().getName() + "\n"
                        + "Product: " + savedRequest.getProduct().getName() + "\n"
                        + "Quantity: " + savedRequest.getNumberOfQuantities() + "\n"
                        + "Department: " + savedRequest.getDepartment().getDepartmentName() + "\n"
                        + "Total Price: " + savedRequest.getTotalPrice() + "\n"
                        + "Status: " + savedRequest.getStatus() + "\n\n"
                        + "Please review and approve or reject the request.";

                emailService.sendEmail(
                        admin.getEmail(),
                        "Procurement Request Approved by Manager",
                        message
                );
            }

            return createStatusResponse(savedRequest);
        }

        if (request.getStatus() == RequestStatus.PENDING &&
                newStatus == RequestStatus.MANAGER_REJECTED) {

            if (!isManager) {
                return null;
            }

            request.setStatus(RequestStatus.MANAGER_REJECTED);
            request.setUpdatedDate(LocalDateTime.now());

            Request savedRequest = requestRepository.save(request);

            if (savedRequest.getUser() != null &&
                    savedRequest.getUser().getEmail() != null) {

                String message =
                        "Your procurement request has been rejected by the Manager.\n\n"
                        + "Request Details:\n"
                        + "Request ID: " + savedRequest.getRequestId() + "\n"
                        + "Product: " + savedRequest.getProduct().getName() + "\n"
                        + "Quantity: " + savedRequest.getNumberOfQuantities() + "\n"
                        + "Department: " + savedRequest.getDepartment().getDepartmentName() + "\n"
                        + "Total Price: " + savedRequest.getTotalPrice() + "\n"
                        + "Status: " + savedRequest.getStatus();

                emailService.sendEmail(
                        savedRequest.getUser().getEmail(),
                        "Procurement Request Rejected",
                        message
                );
            }

            return createStatusResponse(savedRequest);
        }

        if (request.getStatus() == RequestStatus.MANAGER_APPROVED &&
                newStatus == RequestStatus.APPROVED) {

            if (!isAdmin) {
                return null;
            }

            request.setStatus(RequestStatus.APPROVED);
            request.setUpdatedDate(LocalDateTime.now());

            Request savedRequest = requestRepository.save(request);

            if (savedRequest.getUser() != null &&
                    savedRequest.getUser().getEmail() != null) {

                String message =
                        "Your procurement request has been approved.\n\n"
                        + "Request Details:\n"
                        + "Request ID: " + savedRequest.getRequestId() + "\n"
                        + "Product: " + savedRequest.getProduct().getName() + "\n"
                        + "Quantity: " + savedRequest.getNumberOfQuantities() + "\n"
                        + "Department: " + savedRequest.getDepartment().getDepartmentName() + "\n"
                        + "Total Price: " + savedRequest.getTotalPrice() + "\n"
                        + "Status: " + savedRequest.getStatus() + "\n\n"
                        + "Your procurement request has been successfully approved.";

                emailService.sendEmail(
                        savedRequest.getUser().getEmail(),
                        "Procurement Request Approved",
                        message
                );
            }

            List<Supplier> suppliers =
                    supplierRepository.findByProduct(savedRequest.getProduct());

            for (Supplier supplier : suppliers) {

                if (supplier.getEmail() != null) {

                    String message =
                            "A new procurement order has been approved.\n\n"
                            + "Order Details:\n"
                            + "Request ID: " + savedRequest.getRequestId() + "\n"
                            + "Product: " + savedRequest.getProduct().getName() + "\n"
                            + "Quantity: " + savedRequest.getNumberOfQuantities() + "\n"
                            + "Department: " + savedRequest.getDepartment().getDepartmentName() + "\n"
                            + "Total Price: " + savedRequest.getTotalPrice() + "\n"
                            + "Status: " + savedRequest.getStatus() + "\n\n"
                            + "Payment Details:\n"
                            + "Bank Account Number: 123456789012\n"
                            + "IFSC Code: DEMO0001234\n"
                            + "Account Name: Enterprise Procurement System\n\n"
                            + "Please process this procurement order.";

                    emailService.sendEmail(
                            supplier.getEmail(),
                            "New Procurement Order",
                            message
                    );
                }
            }

            return createStatusResponse(savedRequest);
        }

        if (request.getStatus() == RequestStatus.MANAGER_APPROVED &&
                newStatus == RequestStatus.REJECTED) {

            if (!isAdmin) {
                return null;
            }

            request.setStatus(RequestStatus.REJECTED);
            request.setUpdatedDate(LocalDateTime.now());

            Request savedRequest = requestRepository.save(request);

            if (savedRequest.getUser() != null &&
                    savedRequest.getUser().getEmail() != null) {

                String message =
                        "Your procurement request has been rejected by the Admin.\n\n"
                        + "Request Details:\n"
                        + "Request ID: " + savedRequest.getRequestId() + "\n"
                        + "Product: " + savedRequest.getProduct().getName() + "\n"
                        + "Quantity: " + savedRequest.getNumberOfQuantities() + "\n"
                        + "Department: " + savedRequest.getDepartment().getDepartmentName() + "\n"
                        + "Total Price: " + savedRequest.getTotalPrice() + "\n"
                        + "Status: " + savedRequest.getStatus();

                emailService.sendEmail(
                        savedRequest.getUser().getEmail(),
                        "Procurement Request Rejected",
                        message
                );
            }

            return createStatusResponse(savedRequest);
        }

        return null;
    }

    private RequestStatusResponse createStatusResponse(Request request) {

        return new RequestStatusResponse(
                request.getProduct().getProductId(),
                request.getRequestId(),
                request.getStatus().name()
        );
    }

    public byte[] downloadMyRequestsCsv(Long userId) {

        List<Request> requests =
                requestRepository.findByUserUserId(userId);

        StringBuilder csv = new StringBuilder();

        csv.append("Request ID,Product,Quantity,Department,Total Price,Status,Created Date\n");

        for (Request request : requests) {

            csv.append(request.getRequestId()).append(",")
                    .append(request.getProduct().getName()).append(",")
                    .append(request.getNumberOfQuantities()).append(",")
                    .append(request.getDepartment().getDepartmentName()).append(",")
                    .append(request.getTotalPrice()).append(",")
                    .append(request.getStatus()).append(",")
                    .append(request.getCreatedDate()).append("\n");
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    public byte[] downloadAllRequestsCsv() {

        List<Request> requests = requestRepository.findAll();

        StringBuilder csv = new StringBuilder();

        csv.append("Request ID,Employee,Product,Quantity,Department,Total Price,Status,Created Date\n");

        for (Request request : requests) {

            csv.append(request.getRequestId()).append(",")
                    .append(request.getUser().getName()).append(",")
                    .append(request.getProduct().getName()).append(",")
                    .append(request.getNumberOfQuantities()).append(",")
                    .append(request.getDepartment().getDepartmentName()).append(",")
                    .append(request.getTotalPrice()).append(",")
                    .append(request.getStatus()).append(",")
                    .append(request.getCreatedDate()).append("\n");
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    public void deleteRequest(Long id) {
        requestRepository.deleteById(id);
    }
}