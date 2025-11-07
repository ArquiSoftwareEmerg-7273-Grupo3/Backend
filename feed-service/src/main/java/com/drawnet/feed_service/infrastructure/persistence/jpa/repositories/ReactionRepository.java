package com.drawnet.feed_service.infrastructure.persistence.jpa.repositories;

import com.drawnet.feed_service.domain.model.entities.Reaction;
import com.drawnet.feed_service.domain.model.entities.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    
    // Buscar reacción específica de un usuario a un post
    Optional<Reaction> findByPostIdAndUserId(Long postId, Long userId);
    
    // Verificar si existe reacción
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    
    // Contar reacciones por tipo en un post
    long countByPostIdAndType(Long postId, ReactionType type);
    
    // Todas las reacciones de un post
    List<Reaction> findByPostIdOrderByCreatedAtDesc(Long postId);
    
    // Reacciones por tipo en un post
    List<Reaction> findByPostIdAndTypeOrderByCreatedAtDesc(Long postId, ReactionType type);
    
    // Reacciones de un usuario
    List<Reaction> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    // Estadísticas de reacciones por post
    @Query("SELECT r.type, COUNT(r) FROM Reaction r WHERE r.post.id = :postId GROUP BY r.type")
    List<Object[]> getReactionStatsByPost(@Param("postId") Long postId);
    
    // Posts que le gustaron a un usuario
    @Query("SELECT r.post.id FROM Reaction r WHERE r.userId = :userId AND r.type = :type")
    List<Long> findPostIdsLikedByUser(@Param("userId") Long userId, @Param("type") ReactionType type);
    
    // Eliminar reacción específica
    void deleteByPostIdAndUserId(Long postId, Long userId);
    
    // Buscar reacciones por usuario
    List<Reaction> findByUserId(Long userId);
    
    // Buscar reacciones por post ID
    List<Reaction> findByPostId(Long postId);
}

