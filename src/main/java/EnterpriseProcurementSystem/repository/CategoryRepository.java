package EnterpriseProcurementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import EnterpriseProcurementSystem.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}