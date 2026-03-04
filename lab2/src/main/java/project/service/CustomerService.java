package project.service;

import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.entity.Customer;
import project.exception.CustomerNotFoundException;
import project.repository.CustomerRepository;
import project.specification.CustomerSpecification;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CustomerService {

    public final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Получить клиента по ID с кэшированием
     */
    @Cacheable(value = "customers", key = "#id")
    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
    }

    /**
     * Получить всех клиентов с фильтрацией и пагинацией
     */
    @Cacheable(value = "allCustomers", key = "#pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort + #firstName + #lastName + #email")
    public Page<Customer> findAllWithFilters(String firstName, String lastName, String email, Pageable pageable) {
        var spec = CustomerSpecification.combineFilters(firstName, lastName, email);
        return customerRepository.findAll(spec, pageable);
    }

    /**
     * Создать клиентов (очищает кэш)
     */
    @CacheEvict(value = {"customers", "allCustomers"}, allEntries = true)
    public List<Customer> createCustomers(List<Customer> customers) {
        for (Customer customer : customers) {
            if (customer.getCreatedAt() == null) {
                customer.setCreatedAt(LocalDateTime.now());
            }
        }
        return customerRepository.saveAll(customers);
    }

    /**
     * Обновить клиента (очищает кэш)
     */
    @CacheEvict(value = {"customers", "allCustomers"}, allEntries = true)
    public Customer updateCustomer(Long id, Customer customer) {
        Customer updateCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

        updateCustomer.setFirstName(customer.getFirstName());
        updateCustomer.setLastName(customer.getLastName());
        updateCustomer.setEmail(customer.getEmail());

        return customerRepository.save(updateCustomer);
    }

    /**
     * Удалить клиента (очищает кэш)
     */
    @CacheEvict(value = {"customers", "allCustomers"}, allEntries = true)
    public void deleteCustomer(Long id) {
        Customer deleteCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        customerRepository.delete(deleteCustomer);
    }
}