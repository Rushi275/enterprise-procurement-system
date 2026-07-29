package EnterpriseProcurementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import EnterpriseProcurementSystem.entity.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}