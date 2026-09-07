package EnterpriseProcurementSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import EnterpriseProcurementSystem.entity.Payment;
import EnterpriseProcurementSystem.entity.Request;
import EnterpriseProcurementSystem.entity.Supplier;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findByRequest(Request request);

    List<Payment> findBySupplier(Supplier supplier);
}