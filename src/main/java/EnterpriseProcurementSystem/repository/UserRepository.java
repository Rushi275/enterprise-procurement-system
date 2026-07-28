package EnterpriseProcurementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import EnterpriseProcurementSystem.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}