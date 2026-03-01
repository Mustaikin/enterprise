package project.controller;

import project.entity.Customer;
import project.service.CustomerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    // Конструктор для внедрения зависимости
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Получить всех клиентов
     * Доступно: USER и ADMIN
     * GET /api/v1/customers/all
     */
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Customer> readCustomerAll() {
        return customerService.readCustomerAll();
    }

    /**
     * Получить клиента по ID
     * Доступно: USER и ADMIN
     * GET /api/v1/customers/find={id}
     */
    @GetMapping("/find={id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Customer readCustomer(@PathVariable Long id) {
        return customerService.readCustomer(id);
    }

    /**
     * Создать несколько клиентов (пакетное создание)
     * Доступно: Только ADMIN
     * POST /api/v1/customers/batch
     */
    @PostMapping("/batch")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Customer> createCustomers(@RequestBody List<Customer> customers) {
        return customerService.createCustomers(customers);
    }

    /**
     * Обновить клиента
     * Доступно: Только ADMIN
     * PUT /api/v1/customers/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        return customerService.updateCustomer(id, customer);
    }

    /**
     * Удалить клиента
     * Доступно: Только ADMIN
     * DELETE /api/v1/customers/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }
}