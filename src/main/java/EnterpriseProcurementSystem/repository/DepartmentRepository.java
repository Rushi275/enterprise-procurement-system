package EnterpriseProcurementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import EnterpriseProcurementSystem.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

}