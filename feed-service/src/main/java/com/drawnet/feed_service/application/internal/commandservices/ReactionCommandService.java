package com.drawnet.feed_service.application.internal.commandservices;

import com.drawnet.feed_service.domain.model.commands.*;
import com.drawnet.feed_service.domain.model.entities.Reaction;
import com.drawnet.feed_service.infrastructure.persistence.jpa.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReactionCommandService {

    private final ReactionRepository reactionRepository;
    private final PostRepository postRepository;

    public Optional<Long> handle(CreateReactionCommand command) {
        return postRepository.findById(command.postId())
                .filter(post -> post.isActive())
                .map(post -> {
                    // Verificar si ya existe una reacción del usuario
                    var existingReaction = reactionRepository.findByPostIdAndUserId(
                            command.postId(), command.userId());
                    
                    if (existingReaction.isPresent()) {
                        // Actualizar tipo de reacción existente
                        var reaction = existingReaction.get();
                        reaction.changeType(command.reactionType());
                        var savedReaction = reactionRepository.save(reaction);
                        return savedReaction.getId();
                    } else {
                        // Crear nueva reacción
                        var reaction = new Reaction(command.userId(), command.reactionType(), post);
                        post.addReaction(reaction);
                        var savedReaction = reactionRepository.save(reaction);
                        postRepository.save(post);
                        return savedReaction.getId();
                    }
                });
    }

    public boolean handle(RemoveReactionCommand command) {
        var existingReaction = reactionRepository.findByPostIdAndUserId(
                command.postId(), command.userId());
        
        if (existingReaction.isPresent()) {
            var reaction = existingReaction.get();
            var post = reaction.getPost();
            
            post.removeReaction(reaction);
            reactionRepository.delete(reaction);
            postRepository.save(post);
            
            return true;
        }
        
        return false;
    }

    public Optional<Reaction> toggleReaction(Long postId, Long userId, 
                                           com.drawnet.feed_service.domain.model.entities.ReactionType reactionType) {
        var existingReaction = reactionRepository.findByPostIdAndUserId(postId, userId);
        
        if (existingReaction.isPresent()) {
            var reaction = existingReaction.get();
            if (reaction.getType() == reactionType) {
                // Remover reacción si es del mismo tipo
                handle(new RemoveReactionCommand(postId, userId));
                return Optional.empty();
            } else {
                // Cambiar tipo de reacción
                return handle(new CreateReactionCommand(postId, userId, reactionType))
                        .flatMap(id -> reactionRepository.findById(id));
            }
        } else {
            // Crear nueva reacción
            return handle(new CreateReactionCommand(postId, userId, reactionType))
                    .flatMap(id -> reactionRepository.findById(id));
        }
    }
}