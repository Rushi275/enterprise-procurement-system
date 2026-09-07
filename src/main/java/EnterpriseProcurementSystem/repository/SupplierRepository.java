package EnterpriseProcurementSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import EnterpriseProcurementSystem.entity.Product;
import EnterpriseProcurementSystem.entity.Supplier;
import EnterpriseProcurementSystem.entity.User;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    List<Supplier> findByProduct(Product product);

    Supplier findByUser(User user);
}