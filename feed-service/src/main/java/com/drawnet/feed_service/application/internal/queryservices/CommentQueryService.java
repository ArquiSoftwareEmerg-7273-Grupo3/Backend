package com.drawnet.feed_service.application.internal.queryservices;

import com.drawnet.feed_service.domain.model.entities.Comment;
import com.drawnet.feed_service.domain.model.querys.GetCommentsQuery;
import com.drawnet.feed_service.infrastructure.persistence.jpa.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentQueryService {

    private final CommentRepository commentRepository;

    public Page<Comment> handle(GetCommentsQuery query) {
        Pageable pageable = PageRequest.of(query.page(), query.size());
        
        // Comentarios de un usuario específico
        if (query.userId() != null) {
            return commentRepository.findByUserIdAndActiveOrderByCreatedAtDesc(
                query.userId(), true, pageable);
        }
        
        // Respuestas a un comentario específico
        if (query.parentCommentId() != null) {
            List<Comment> replies = commentRepository.findByParentCommentIdAndActiveOrderByCreatedAtAsc(
                query.parentCommentId(), true);
            return new org.springframework.data.domain.PageImpl<>(replies, pageable, replies.size());
        }
        
        // Comentarios principales de un post
        if (query.postId() != null) {
            return commentRepository.findByPostIdAndParentCommentIsNullAndActiveOrderByCreatedAtDesc(
                query.postId(), true, pageable);
        }
        
        // Todos los comentarios activos
        return commentRepository.findAll(pageable);
    }

    public Optional<Comment> findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .filter(Comment::isActive);
    }

    public List<Comment> getRepliesForComment(Long commentId) {
        return commentRepository.findByParentCommentIdAndActiveOrderByCreatedAtAsc(commentId, true);
    }

    public Page<Comment> getCommentsForPost(Long postId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return commentRepository.findByPostIdAndParentCommentIsNullAndActiveOrderByCreatedAtDesc(
            postId, true, pageable);
    }

    public long countCommentsForPost(Long postId) {
        return commentRepository.countByPostIdAndActive(postId, true);
    }

    public long countRepliesForComment(Long commentId) {
        return commentRepository.countByParentCommentIdAndActive(commentId, true);
    }

    public Page<Comment> searchComments(String content, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return commentRepository.findByContentContaining(content, pageable);
    }
}