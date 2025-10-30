package com.drawnet.artcollab.iam.infrastructure.persistence.jpa.repositories;

import com.drawnet.artcollab.iam.domain.model.aggregates.User;
import com.drawnet.artcollab.iam.domain.model.valueobjects.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    // UserRepository.java
    Optional<User> findByIdAndRole_Name(Long id, Roles name);

    // Método para cargar usuario con todos sus perfiles relacionados (sin JPQL)
    @EntityGraph(attributePaths = {"ilustrador", "escritor"})
    Optional<User> findWithProfilesById(Long id);
}
