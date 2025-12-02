package com.drawnet.feed_service.application.internal.commandservices;

import com.drawnet.feed_service.domain.model.commands.*;
import com.drawnet.feed_service.domain.model.entities.Comment;
import com.drawnet.feed_service.infrastructure.clients.NotificationClient;
import com.drawnet.feed_service.infrastructure.clients.dto.CreateNotificationFromCommentRequest;
import com.drawnet.feed_service.infrastructure.persistence.jpa.repositories.*;
import com.drawnet.feed_service.application.services.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentCommandService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final WebSocketService webSocketService;
    private final NotificationClient notificationClient;

    public Optional<Long> handle(CreateCommentCommand command) {
        return postRepository.findById(command.postId())
                .filter(post -> post.isActive())
                .map(post -> {
                    Comment comment;
                    
                    if (command.parentCommentId() != null) {
                        // Es una respuesta a otro comentario
                        var parentComment = commentRepository.findById(command.parentCommentId())
                                .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
                        
                        comment = new Comment(command.userId(), command.content(), post, parentComment);
                        parentComment.addReply(comment);
                    } else {
                        // Es un comentario principal
                        comment = new Comment(command.userId(), command.content(), post);
                    }
                    
                    post.addComment(comment);
                    var savedComment = commentRepository.save(comment);
                    postRepository.save(post);
                    
                    // Enviar evento WebSocket para actualización en tiempo real
                    webSocketService.sendCommentCreatedEvent(command.postId(), savedComment);
                    
                    // Crear notificación (solo si el comentador NO es el autor del post)
                    if (!post.getUserId().equals(command.userId().toString())) {
                        try {
                            CreateNotificationFromCommentRequest notificationRequest = 
                                new CreateNotificationFromCommentRequest(
                                    savedComment.getId(),
                                    command.postId(),
                                    command.userId(),
                                    command.content()
                                );
                            
                            notificationClient.createNotificationFromComment(
                                Long.parseLong(post.getUserId()),
                                notificationRequest
                            );
                            
                            log.info("Notification created for comment {} on post {}", 
                                savedComment.getId(), command.postId());
                        } catch (Exception e) {
                            // Log pero no fallar la operación principal
                            log.error("Failed to create notification for comment {} on post {}: {}", 
                                savedComment.getId(), command.postId(), e.getMessage());
                        }
                    }
                    
                    return savedComment.getId();
                });
    }

    public boolean handle(DeleteCommentCommand command) {
        return commentRepository.findById(command.commentId())
                .filter(comment -> comment.getUserId().equals(command.userId()))
                .map(comment -> {
                    comment.deactivate();
                    commentRepository.save(comment);
                    
                    // Actualizar contador en el post
                    var post = comment.getPost();
                    post.removeComment(comment);
                    postRepository.save(post);
                    
                    return true;
                })
                .orElse(false);
    }

    public Optional<Comment> updateComment(Long commentId, Long userId, String newContent) {
        return commentRepository.findById(commentId)
                .filter(comment -> comment.getUserId().equals(userId) && comment.isActive())
                .map(comment -> {
                    comment.updateContent(newContent);
                    return commentRepository.save(comment);
                });
    }
}