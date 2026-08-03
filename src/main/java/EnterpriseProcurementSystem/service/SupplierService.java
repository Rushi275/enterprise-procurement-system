package EnterpriseProcurementSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import EnterpriseProcurementSystem.entity.Supplier;
import EnterpriseProcurementSystem.repository.SupplierRepository;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    public Supplier saveSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier updateSupplier(Long id, Supplier supplier) {
        Supplier existingSupplier = supplierRepository.findById(id).orElse(null);

        if (existingSupplier != null) {
            existingSupplier.setProduct(supplier.getProduct());
            existingSupplier.setName(supplier.getName());
            existingSupplier.setPhone(supplier.getPhone());
            existingSupplier.setAddress(supplier.getAddress());
            existingSupplier.setEmail(supplier.getEmail());
            existingSupplier.setGstNumber(supplier.getGstNumber());
            existingSupplier.setStatus(supplier.getStatus());
            existingSupplier.setRating(supplier.getRating());
            existingSupplier.setFeedback(supplier.getFeedback());

            return supplierRepository.save(existingSupplier);
        }

        return null;
    }

    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }
}