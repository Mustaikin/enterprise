package project.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import project.model.enumeration.RoleEnum;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private RoleEnum name;

    public Role(RoleEnum roleUser) {
        this.name = roleUser;
    }

    public Role() {

    }
}