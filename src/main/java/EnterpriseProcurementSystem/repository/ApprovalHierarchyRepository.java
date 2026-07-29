package EnterpriseProcurementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import EnterpriseProcurementSystem.entity.ApprovalHierarchy;

@Repository
public interface ApprovalHierarchyRepository extends JpaRepository<ApprovalHierarchy, Long> {
}