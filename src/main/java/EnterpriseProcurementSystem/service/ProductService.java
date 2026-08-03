package EnterpriseProcurementSystem.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import EnterpriseProcurementSystem.entity.Product;
import EnterpriseProcurementSystem.enums.ProductStatus;
import EnterpriseProcurementSystem.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product saveProduct(Product product) {

        product.setStatus(ProductStatus.ACTIVE);

        product.setCreatedDate(LocalDateTime.now());

        product.setUpdatedDate(LocalDateTime.now());

        product.setTotalPrice(
                product.getPricePerProduct() * product.getNumberOfQuantities());

        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProduct(Long id, Product product) {

        Product existingProduct = productRepository.findById(id).orElse(null);

        if (existingProduct != null) {

            existingProduct.setName(product.getName());
            existingProduct.setUser(product.getUser());
            existingProduct.setPricePerProduct(product.getPricePerProduct());
            existingProduct.setNumberOfQuantities(product.getNumberOfQuantities());

            existingProduct.setTotalPrice(
                    product.getPricePerProduct() * product.getNumberOfQuantities());

            existingProduct.setDepartment(product.getDepartment());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setDescription(product.getDescription());

            existingProduct.setStatus(ProductStatus.ACTIVE);

            existingProduct.setCreatedDate(existingProduct.getCreatedDate());
            existingProduct.setUpdatedDate(LocalDateTime.now());

            return productRepository.save(existingProduct);
        }

        return null;
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}