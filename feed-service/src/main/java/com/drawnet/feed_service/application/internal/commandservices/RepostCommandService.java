package com.drawnet.feed_service.application.internal.commandservices;

import com.drawnet.feed_service.domain.model.commands.CreateRepostCommand;
import com.drawnet.feed_service.domain.model.entities.Repost;
import com.drawnet.feed_service.infrastructure.persistence.jpa.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RepostCommandService {

    private final RepostRepository repostRepository;
    private final PostRepository postRepository;

    public Optional<Long> handle(CreateRepostCommand command) {
        // Verificar que el post existe y está activo
        return postRepository.findById(command.originalPostId())
                .filter(post -> post.isActive())
                .filter(post -> !post.getAuthorId().equals(command.userId())) // No repostear propio post
                .map(post -> {
                    // Verificar si ya existe un repost activo
                    var existingRepost = repostRepository.findByOriginalPostIdAndUserIdAndActive(
                            command.originalPostId(), command.userId(), true);
                    
                    if (existingRepost.isPresent()) {
                        // Actualizar comentario si ya existe
                        var repost = existingRepost.get();
                        repost.updateComment(command.comment());
                        var savedRepost = repostRepository.save(repost);
                        return savedRepost.getId();
                    } else {
                        // Crear nuevo repost
                        var repost = new Repost(command.userId(), post, command.comment());
                        post.addRepost(repost);
                        var savedRepost = repostRepository.save(repost);
                        postRepository.save(post);
                        return savedRepost.getId();
                    }
                });
    }

    public boolean removeRepost(Long originalPostId, Long userId) {
        var existingRepost = repostRepository.findByOriginalPostIdAndUserIdAndActive(
                originalPostId, userId, true);
        
        if (existingRepost.isPresent()) {
            var repost = existingRepost.get();
            var post = repost.getOriginalPost();
            
            repost.deactivate();
            post.removeRepost(repost);
            
            repostRepository.save(repost);
            postRepository.save(post);
            
            return true;
        }
        
        return false;
    }

    public boolean toggleRepost(Long originalPostId, Long userId, String comment) {
        var existingRepost = repostRepository.findByOriginalPostIdAndUserIdAndActive(
                originalPostId, userId, true);
        
        if (existingRepost.isPresent()) {
            // Remover repost existente
            return removeRepost(originalPostId, userId);
        } else {
            // Crear nuevo repost
            var command = new CreateRepostCommand(originalPostId, userId, comment);
            return handle(command).isPresent();
        }
    }
}