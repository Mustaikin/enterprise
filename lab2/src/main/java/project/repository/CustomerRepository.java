package project.repository;

import project.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends
        JpaRepository<Customer, Long>,
        JpaSpecificationExecutor<Customer> {  // Добавляем для спецификаций
}