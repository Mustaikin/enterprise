package project.specification;

import project.entity.Customer;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class CustomerSpecification {

    public static Specification<Customer> hasFirstName(String firstName) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(firstName)
                        ? criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%")
                        : criteriaBuilder.conjunction();
    }

    public static Specification<Customer> hasLastName(String lastName) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(lastName)
                        ? criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%")
                        : criteriaBuilder.conjunction();
    }

    public static Specification<Customer> hasEmail(String email) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(email)
                        ? criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%")
                        : criteriaBuilder.conjunction();
    }

    public static Specification<Customer> combineFilters(String firstName, String lastName, String email) {
        return Specification
                .where(hasFirstName(firstName))
                .and(hasLastName(lastName))
                .and(hasEmail(email));
    }
}