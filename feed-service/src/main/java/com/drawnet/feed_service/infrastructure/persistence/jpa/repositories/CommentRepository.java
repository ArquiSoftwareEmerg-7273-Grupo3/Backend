package com.drawnet.feed_service.infrastructure.persistence.jpa.repositories;

import com.drawnet.feed_service.domain.model.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    // Comentarios de un post (sin respuestas)
    Page<Comment> findByPostIdAndParentCommentIsNullAndActiveOrderByCreatedAtDesc(
        Long postId, boolean active, Pageable pageable);
    
    // Respuestas a un comentario
    List<Comment> findByParentCommentIdAndActiveOrderByCreatedAtAsc(Long parentCommentId, boolean active);
    
    // Comentarios por usuario
    Page<Comment> findByUserIdAndActiveOrderByCreatedAtDesc(Long userId, boolean active, Pageable pageable);
    
    // Contar comentarios de un post
    long countByPostIdAndActive(Long postId, boolean active);
    
    // Contar respuestas de un comentario
    long countByParentCommentIdAndActive(Long parentCommentId, boolean active);
    
    // Comentarios recientes de un usuario
    @Query("SELECT c FROM Comment c WHERE c.userId = :userId AND c.active = true " +
           "ORDER BY c.createdAt DESC")
    List<Comment> findRecentCommentsByUser(@Param("userId") Long userId, Pageable pageable);
    
    // Buscar comentarios por contenido
    @Query("SELECT c FROM Comment c WHERE c.active = true AND c.content LIKE %:content% " +
           "ORDER BY c.createdAt DESC")
    Page<Comment> findByContentContaining(@Param("content") String content, Pageable pageable);
    
    // Buscar comentarios por post ID
    List<Comment> findByPostId(Long postId);
    
    // Buscar comentarios por usuario ID
    List<Comment> findByUserId(Long userId);
}

