package com.drawnet.artcollab.iam.domain.model.aggregates;

import com.drawnet.artcollab.iam.domain.model.entities.Role;

import com.drawnet.artcollab.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Entity
@Table(name = "usuarios")
public class User extends AuditableAbstractAggregateRoot<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(unique = true)
    private String username;

    @NotBlank
    @Size(max = 100)
    @Column(length = 100)
    private String contrasenia;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol")
    private Role role;


    public User(){}

    public User(String username, String contrasenia, Role role) {
        this();
        this.username = username;
        this.contrasenia = contrasenia;
        this.role = role;
    }

    public Role getRole() {
        return role;
    }
    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return contrasenia;
    }
}
