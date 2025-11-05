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
    
    // Buscar posts activos ordenados por fecha
    Page<Post> findByActiveOrderByCreatedAtDesc(boolean active, Pageable pageable);
    
    // Buscar posts por contenido (búsqueda básica)
    @Query("SELECT p FROM Post p WHERE p.active = true AND p.content LIKE %:content% ORDER BY p.createdAt DESC")
    Page<Post> findByContentContaining(@Param("content") String content, Pageable pageable);
    
    // Buscar posts por tags
    @Query("SELECT DISTINCT p FROM Post p JOIN p.tags t WHERE p.active = true AND t IN :tags ORDER BY p.createdAt DESC")
    Page<Post> findByTagsIn(@Param("tags") List<String> tags, Pageable pageable);
    
    // Posts más populares (por likes)
    Page<Post> findByActiveOrderByLikesCountDescCreatedAtDesc(boolean active, Pageable pageable);
    
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
           "ORDER BY (p.likesCount + p.commentsCount + p.repostsCount) DESC, p.createdAt DESC")
    Page<Post> findTrendingPosts(@Param("since") LocalDateTime since, Pageable pageable);
}