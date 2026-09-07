package EnterpriseProcurementSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import EnterpriseProcurementSystem.dto.SupplierAccountRequest;
import EnterpriseProcurementSystem.dto.SupplierMpinRequest;
import EnterpriseProcurementSystem.entity.Supplier;
import EnterpriseProcurementSystem.entity.User;
import EnterpriseProcurementSystem.service.SupplierService;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @PostMapping
    public Supplier createSupplier(@RequestBody Supplier supplier) {
        return supplierService.saveSupplier(supplier);
    }

    @GetMapping
    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    @PutMapping("/{id}")
    public Supplier updateSupplier(
            @PathVariable Long id,
            @RequestBody Supplier supplier) {
        return supplierService.updateSupplier(id, supplier);
    }

    @DeleteMapping("/{id}")
    public void deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
    }

    @PostMapping("/{id}/account")
    public User createSupplierAccount(
            @PathVariable Long id,
            @RequestBody SupplierAccountRequest request) {
        return supplierService.createSupplierAccount(
                id,
                request.getPassword()
        );
    }

    @PutMapping("/{id}/mpin")
    public Supplier setSupplierMpin(
            @PathVariable Long id,
            @RequestBody SupplierMpinRequest request) {
        return supplierService.setSupplierMpin(
                id,
                request.getMpin()
        );
    }
}