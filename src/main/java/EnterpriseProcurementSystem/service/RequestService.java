package EnterpriseProcurementSystem.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import EnterpriseProcurementSystem.entity.Product;
import EnterpriseProcurementSystem.entity.Request;
import EnterpriseProcurementSystem.enums.RequestStatus;
import EnterpriseProcurementSystem.repository.ProductRepository;
import EnterpriseProcurementSystem.repository.RequestRepository;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ProductRepository productRepository;

    public Request raiseRequest(Request request) {

        Product product = productRepository.findById(request.getProduct().getProductId()).orElse(null);

        if (product == null) {
            return null;
        }

        request.setProduct(product);
        request.setStatus(RequestStatus.PENDING);
        request.setCreatedDate(LocalDateTime.now());
        request.setUpdatedDate(LocalDateTime.now());

        double totalPrice = product.getPricePerProduct() * request.getNumberOfQuantities();
        request.setTotalPrice(totalPrice);

        return requestRepository.save(request);
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

        if (request != null && request.getStatus() == RequestStatus.PENDING) {
            request.setStatus(RequestStatus.MANAGER_APPROVED);
            request.setUpdatedDate(LocalDateTime.now());
            return requestRepository.save(request);
        }

        return null;
    }

    public Request managerRejectRequest(Long id) {

        Request request = requestRepository.findById(id).orElse(null);

        if (request != null && request.getStatus() == RequestStatus.PENDING) {
            request.setStatus(RequestStatus.MANAGER_REJECTED);
            request.setUpdatedDate(LocalDateTime.now());
            return requestRepository.save(request);
        }

        return null;
    }

    public Request approveRequest(Long id) {

        Request request = requestRepository.findById(id).orElse(null);

        if (request != null && request.getStatus() == RequestStatus.MANAGER_APPROVED) {
            request.setStatus(RequestStatus.APPROVED);
            request.setUpdatedDate(LocalDateTime.now());
            return requestRepository.save(request);
        }

        return null;
    }

    public Request rejectRequest(Long id) {

        Request request = requestRepository.findById(id).orElse(null);

        if (request != null && request.getStatus() == RequestStatus.MANAGER_APPROVED) {
            request.setStatus(RequestStatus.REJECTED);
            request.setUpdatedDate(LocalDateTime.now());
            return requestRepository.save(request);
        }

        return null;
    }

    public void deleteRequest(Long id) {
        requestRepository.deleteById(id);
    }
}