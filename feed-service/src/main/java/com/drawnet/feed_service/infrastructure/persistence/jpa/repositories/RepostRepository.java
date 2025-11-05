package com.drawnet.feed_service.infrastructure.persistence.jpa.repositories;

import com.drawnet.feed_service.domain.model.entities.Repost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RepostRepository extends JpaRepository<Repost, Long> {
    
    // Verificar si un usuario ya reposteó un post
    boolean existsByOriginalPostIdAndUserIdAndActive(Long originalPostId, Long userId, boolean active);
    
    // Obtener repost específico
    Optional<Repost> findByOriginalPostIdAndUserIdAndActive(Long originalPostId, Long userId, boolean active);
    
    // Reposts de un usuario
    Page<Repost> findByUserIdAndActiveOrderByRepostDateDesc(Long userId, boolean active, Pageable pageable);
    
    // Reposts de un post específico
    Page<Repost> findByOriginalPostIdAndActiveOrderByRepostDateDesc(Long originalPostId, boolean active, Pageable pageable);
    
    // Contar reposts de un post
    long countByOriginalPostIdAndActive(Long originalPostId, boolean active);
    
    // Reposts recientes
    @Query("SELECT r FROM Repost r WHERE r.active = true AND r.repostDate >= :since ORDER BY r.repostDate DESC")
    Page<Repost> findRecentReposts(@Param("since") LocalDateTime since, Pageable pageable);
    
    // Feed de reposts de usuarios seguidos
    @Query("SELECT r FROM Repost r WHERE r.active = true AND r.userId IN :followingIds ORDER BY r.repostDate DESC")
    Page<Repost> findRepostsByFollowingUsers(@Param("followingIds") List<Long> followingIds, Pageable pageable);
    
    // Reposts con comentario
    @Query("SELECT r FROM Repost r WHERE r.active = true AND r.comment IS NOT NULL AND r.comment != '' ORDER BY r.repostDate DESC")
    Page<Repost> findRepostsWithComment(Pageable pageable);
    
    // Posts más reposteados
    @Query("SELECT r.originalPost.id, COUNT(r) as repostCount FROM Repost r WHERE r.active = true " +
           "GROUP BY r.originalPost.id ORDER BY repostCount DESC")
    List<Object[]> findMostRepostedPosts(Pageable pageable);
    
    // Eliminar repost
    void deleteByOriginalPostIdAndUserId(Long originalPostId, Long userId);
}