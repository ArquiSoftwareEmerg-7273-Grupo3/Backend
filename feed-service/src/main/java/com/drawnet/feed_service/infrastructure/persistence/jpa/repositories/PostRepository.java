package com.drawnet.feed_service.infrastructure.persistence.jpa.repositories;

import com.drawnet.feed_service.domain.model.aggregates.Post;
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
public interface PostRepository extends JpaRepository<Post, Long> {
    
    // Buscar posts activos por autor
    Page<Post> findByAuthorIdAndActiveOrderByCreatedAtDesc(Long authorId, boolean active, Pageable pageable);
    
    // Buscar posts activos ordenados por fecha (más recientes primero)
    Page<Post> findByActiveOrderByCreatedAtDesc(boolean active, Pageable pageable);
    
    // Buscar posts activos ordenados por fecha (más antiguos primero)
    Page<Post> findByActiveOrderByCreatedAtAsc(boolean active, Pageable pageable);
    
    // Buscar posts por contenido (búsqueda básica)
    @Query("SELECT p FROM Post p WHERE p.active = true AND p.content LIKE %:content% ORDER BY p.createdAt DESC")
    Page<Post> findByContentContaining(@Param("content") String content, Pageable pageable);
    
    // Buscar posts por tags
    @Query("SELECT DISTINCT p FROM Post p JOIN p.tags t WHERE p.active = true AND t IN :tags ORDER BY p.createdAt DESC")
    Page<Post> findByTagsIn(@Param("tags") List<String> tags, Pageable pageable);
    
    // Posts más populares (por reacciones)
    Page<Post> findByActiveOrderByReactionsCountDescCreatedAtDesc(boolean active, Pageable pageable);
    
    // Posts más comentados
    Page<Post> findByActiveOrderByCommentsCountDescCreatedAtDesc(boolean active, Pageable pageable);
    
    // Posts recientes de un período
    @Query("SELECT p FROM Post p WHERE p.active = true AND p.createdAt >= :since ORDER BY p.createdAt DESC")
    Page<Post> findRecentPosts(@Param("since") LocalDateTime since, Pageable pageable);
    
    // Posts de usuarios seguidos (feed personalizado)
    @Query("SELECT p FROM Post p WHERE p.active = true AND p.authorId IN :followingIds ORDER BY p.createdAt DESC")
    Page<Post> findPostsByFollowingUsers(@Param("followingIds") List<Long> followingIds, Pageable pageable);
    
    // Contar posts por autor
    long countByAuthorIdAndActive(Long authorId, boolean active);
    
    // Posts con media
    @Query("SELECT DISTINCT p FROM Post p JOIN p.mediaFiles m WHERE p.active = true ORDER BY p.createdAt DESC")
    Page<Post> findPostsWithMedia(Pageable pageable);
    
    // Posts trending (alto engagement reciente)
    @Query("SELECT p FROM Post p WHERE p.active = true AND p.createdAt >= :since " +
           "ORDER BY (p.reactionsCount + p.commentsCount + p.repostsCount) DESC, p.createdAt DESC")
    Page<Post> findTrendingPosts(@Param("since") LocalDateTime since, Pageable pageable);
    
    // Buscar posts por lista de autor IDs (para feed personalizado)
    List<Post> findByAuthorIdInAndActiveOrderByCreatedAtDesc(List<Long> authorIds, boolean active);
    
    // Método simplificado para compatibilidad
    default List<Post> findByUserIdIn(List<String> userIds) {
        List<Long> authorIds = userIds.stream()
                .map(Long::parseLong)
                .toList();
        return findByAuthorIdInAndActiveOrderByCreatedAtDesc(authorIds, true);
    }
    
    // Buscar posts por autor (para análisis de contenido duplicado)
    List<Post> findByAuthorIdAndCreatedAtAfter(Long authorId, LocalDateTime createdAt);
    
    // Método wrapper para compatibilidad con String userId
    default List<Post> findByUserIdAndCreatedAtAfter(Long userId, LocalDateTime createdAt) {
        return findByAuthorIdAndCreatedAtAfter(userId, createdAt);
    }
    
    // Buscar posts por autor desde una fecha
    List<Post> findByAuthorId(Long authorId);
    
    // Método wrapper para compatibilidad
    default List<Post> findByUserId(String userId) {
        return findByAuthorId(Long.parseLong(userId));
    }
    
    // Contar posts de un autor desde una fecha
    long countByAuthorIdAndCreatedAtAfter(Long authorId, LocalDateTime createdAt);
    
    // Método wrapper para compatibilidad
    default long countByUserIdAndCreatedAtAfter(String userId, LocalDateTime createdAt) {
        return countByAuthorIdAndCreatedAtAfter(Long.parseLong(userId), createdAt);
    }
}
