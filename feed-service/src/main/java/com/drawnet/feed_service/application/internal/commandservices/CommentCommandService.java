package com.drawnet.feed_service.application.internal.commandservices;

import com.drawnet.feed_service.domain.model.commands.*;
import com.drawnet.feed_service.domain.model.entities.Comment;
import com.drawnet.feed_service.infrastructure.persistence.jpa.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentCommandService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

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