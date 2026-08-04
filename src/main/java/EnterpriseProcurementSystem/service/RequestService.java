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

    public void deleteRequest(Long id) {
        requestRepository.deleteById(id);
    }
}