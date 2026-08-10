package EnterpriseProcurementSystem.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import EnterpriseProcurementSystem.entity.Admin;
import EnterpriseProcurementSystem.entity.Department;
import EnterpriseProcurementSystem.entity.Product;
import EnterpriseProcurementSystem.entity.Request;
import EnterpriseProcurementSystem.entity.Supplier;
import EnterpriseProcurementSystem.enums.NotificationType;
import EnterpriseProcurementSystem.enums.RequestStatus;
import EnterpriseProcurementSystem.repository.AdminRepository;
import EnterpriseProcurementSystem.repository.DepartmentRepository;
import EnterpriseProcurementSystem.repository.ProductRepository;
import EnterpriseProcurementSystem.repository.RequestRepository;
import EnterpriseProcurementSystem.repository.SupplierRepository;

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

            String message = "New procurement request requires your approval.";

            notificationService.createNotification(
                    message,
                    department.getManager(),
                    savedRequest,
                    NotificationType.MANAGER_APPROVAL_REQUIRED
            );

            emailService.sendEmail(
                    department.getManager().getEmail(),
                    "Procurement Request Approval Required",
                    message + "\n\nRequest ID: " + savedRequest.getRequestId()
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

    public Request managerApproveRequest(Long id) {

        Request request = requestRepository.findById(id).orElse(null);

        if (request != null &&
                request.getStatus() == RequestStatus.PENDING) {

            request.setStatus(RequestStatus.MANAGER_APPROVED);
            request.setUpdatedDate(LocalDateTime.now());

            Request savedRequest = requestRepository.save(request);

            List<Admin> admins = adminRepository.findAll();

            if (!admins.isEmpty()) {

                Admin admin = admins.get(0);

                emailService.sendEmail(
                        admin.getEmail(),
                        "Procurement Request Approved by Manager",
                        "Manager has approved procurement request.\n\n"
                                + "Request ID: " + savedRequest.getRequestId()
                );
            }

            return savedRequest;
        }

        return null;
    }

    public Request managerRejectRequest(Long id) {

        Request request = requestRepository.findById(id).orElse(null);

        if (request != null &&
                request.getStatus() == RequestStatus.PENDING) {

            request.setStatus(RequestStatus.MANAGER_REJECTED);
            request.setUpdatedDate(LocalDateTime.now());

            Request savedRequest = requestRepository.save(request);

            if (savedRequest.getUser() != null &&
                    savedRequest.getUser().getEmail() != null) {

                emailService.sendEmail(
                        savedRequest.getUser().getEmail(),
                        "Procurement Request Rejected",
                        "Your procurement request has been rejected by the Manager.\n\n"
                                + "Request ID: " + savedRequest.getRequestId()
                );
            }

            return savedRequest;
        }

        return null;
    }

    public Request approveRequest(Long id) {

        Request request = requestRepository.findById(id).orElse(null);

        if (request != null &&
                request.getStatus() == RequestStatus.MANAGER_APPROVED) {

            request.setStatus(RequestStatus.APPROVED);
            request.setUpdatedDate(LocalDateTime.now());

            Request savedRequest = requestRepository.save(request);

            if (savedRequest.getUser() != null &&
                    savedRequest.getUser().getEmail() != null) {

                emailService.sendEmail(
                        savedRequest.getUser().getEmail(),
                        "Procurement Request Approved",
                        "Your procurement request has been approved.\n\n"
                                + "Request ID: " + savedRequest.getRequestId()
                                + "\nTotal Price: " + savedRequest.getTotalPrice()
                );
            }

            List<Supplier> suppliers =
                    supplierRepository.findByProduct(savedRequest.getProduct());

            for (Supplier supplier : suppliers) {

                if (supplier.getEmail() != null) {

                    emailService.sendEmail(
                            supplier.getEmail(),
                            "New Procurement Order",
                            "A procurement request has been approved.\n\n"
                                    + "Request ID: " + savedRequest.getRequestId()
                                    + "\nProduct: " + savedRequest.getProduct().getName()
                                    + "\nQuantity: " + savedRequest.getNumberOfQuantities()
                                    + "\nTotal Price: " + savedRequest.getTotalPrice()
                    );
                }
            }

            return savedRequest;
        }

        return null;
    }

    public Request rejectRequest(Long id) {

        Request request = requestRepository.findById(id).orElse(null);

        if (request != null &&
                request.getStatus() == RequestStatus.MANAGER_APPROVED) {

            request.setStatus(RequestStatus.REJECTED);
            request.setUpdatedDate(LocalDateTime.now());

            Request savedRequest = requestRepository.save(request);

            if (savedRequest.getUser() != null &&
                    savedRequest.getUser().getEmail() != null) {

                emailService.sendEmail(
                        savedRequest.getUser().getEmail(),
                        "Procurement Request Rejected",
                        "Your procurement request has been rejected by the Admin.\n\n"
                                + "Request ID: " + savedRequest.getRequestId()
                );
            }

            return savedRequest;
        }

        return null;
    }

    public void deleteRequest(Long id) {
        requestRepository.deleteById(id);
    }
}