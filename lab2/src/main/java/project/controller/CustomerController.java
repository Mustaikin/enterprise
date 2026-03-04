package project.controller;

import project.entity.Customer;
import project.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Получить всех клиентов с пагинацией, сортировкой и фильтрацией
     * Доступно: USER и ADMIN
     *
     * Примеры запросов:
     * - /api/v1/customers?page=0&size=10&sort=lastName,asc
     * - /api/v1/customers?firstName=Иван&email=@mail.ru
     * - /api/v1/customers?lastName=Петров&page=1&size=5&sort=id,desc
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Page<Customer> getAllCustomers(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email) {

        return customerService.findAllWithFilters(firstName, lastName, email, pageable);
    }

    /**
     * Получить клиента по ID
     * Доступно: USER и ADMIN
     * GET /api/v1/customers/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Customer getCustomerById(@PathVariable Long id) {
        return customerService.findById(id);
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