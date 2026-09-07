package EnterpriseProcurementSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import EnterpriseProcurementSystem.entity.Supplier;
import EnterpriseProcurementSystem.entity.User;
import EnterpriseProcurementSystem.enums.UserRole;
import EnterpriseProcurementSystem.repository.SupplierRepository;
import EnterpriseProcurementSystem.repository.UserRepository;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public User createSupplierAccount(Long supplierId, String password) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        if (supplier.getUser() != null) {
            throw new RuntimeException("Supplier already has an account");
        }

        if (supplier.getEmail() == null || supplier.getEmail().isEmpty()) {
            throw new RuntimeException("Supplier email is required");
        }

        if (userRepository.findByEmail(supplier.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(supplier.getName());
        user.setEmail(supplier.getEmail());
        user.setPassword(passwordEncoder.encode(password));
        user.setPhoneNumber(supplier.getPhone());
        user.setDesignation("SUPPLIER");
        user.setRole(UserRole.SUPPLIER);

        User savedUser = userRepository.save(user);

        supplier.setUser(savedUser);
        supplierRepository.save(supplier);

        return savedUser;
    }

    public Supplier setSupplierMpin(Long supplierId, String mpin) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        if (mpin == null || mpin.length() != 4 || !mpin.matches("\\d{4}")) {
            throw new RuntimeException("MPIN must be exactly 4 digits");
        }

        supplier.setMpinHash(passwordEncoder.encode(mpin));

        return supplierRepository.save(supplier);
    }
}