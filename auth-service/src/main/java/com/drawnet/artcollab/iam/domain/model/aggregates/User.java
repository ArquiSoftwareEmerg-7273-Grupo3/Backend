package com.drawnet.artcollab.iam.domain.model.aggregates;

import com.drawnet.artcollab.iam.domain.model.entities.Role;

import com.drawnet.artcollab.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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

    @NotBlank
    @Size(max = 100)
    @Column(name = "ubicacion")
    private String ubicacion;

    @NotBlank
    @Size(max = 100)
    @Column(name = "nombres")
    private String nombres;

    @NotBlank
    @Size(max = 100)
    @Column(name = "apellidos")
    private String apellidos;

    @NotBlank
    @Size(max = 20)
    @Column(name = "telefono")
    private String telefono;

    @Size(max = 255)
    @Column(name = "foto", length = 255)
    private String foto; // opcional, puede ser URL o path

    @Size(max = 1000)
    @Column(name = "descripcion", length = 1000)
    private String descripcion; // opcional

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @ElementCollection
    @CollectionTable(name = "usuario_redes_sociales", joinColumns = @JoinColumn(name = "usuario_id"))
    @MapKeyColumn(name = "nombre")
    @Column(name = "url")
    private Map<String, String> redesSociales = new HashMap<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol")
    private Role role;

    // Relaciones opcionales con perfiles especializados
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private com.drawnet.artcollab.profiles.domain.model.aggregates.Ilustrador ilustrador;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private com.drawnet.artcollab.profiles.domain.model.aggregates.Escritor escritor;


    public User(){}

    public User(String username, String contrasenia, Role role) {
        this();
        this.username = username;
        this.contrasenia = contrasenia;
        this.role = role;
    }

    // Constructor extendido con todos los campos
    public User(String username,
                String contrasenia,
                Role role,
                String ubicacion,
                String nombres,
                String apellidos,
                String telefono,
                String foto,
                String descripcion,
                LocalDate fechaNacimiento,
                Map<String, String> redesSociales) {
        this(username, contrasenia, role);
        this.ubicacion = ubicacion;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.foto = foto;
        this.descripcion = descripcion;
        this.fechaNacimiento = fechaNacimiento;
        if (redesSociales != null) this.redesSociales = redesSociales;
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

    public String getUbicacion() {
        return ubicacion;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getFoto() {
        return foto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public Map<String, String> getRedesSociales() {
        return redesSociales;
    }

    // Getters para perfiles especializados
    public com.drawnet.artcollab.profiles.domain.model.aggregates.Ilustrador getIlustrador() {
        return ilustrador;
    }

    public com.drawnet.artcollab.profiles.domain.model.aggregates.Escritor getEscritor() {
        return escritor;
    }

    // Métodos de utilidad para verificar tipo de perfil
    public boolean isIlustrador() {
        return ilustrador != null;
    }

    public boolean isEscritor() {
        return escritor != null;
    }

    public boolean hasSpecializedProfile() {
        return isIlustrador() || isEscritor();
    }
}
