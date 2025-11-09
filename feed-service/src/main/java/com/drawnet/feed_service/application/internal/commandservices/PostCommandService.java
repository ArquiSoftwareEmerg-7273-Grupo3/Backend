package com.drawnet.feed_service.application.internal.commandservices;

import com.drawnet.feed_service.domain.model.aggregates.Post;
import com.drawnet.feed_service.domain.model.commands.*;
import com.drawnet.feed_service.domain.model.entities.*;
import com.drawnet.feed_service.infrastructure.persistence.jpa.repositories.*;
import com.drawnet.feed_service.application.services.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostCommandService {

    private final PostRepository postRepository;
    private final MediaRepository mediaRepository;
    private final WebSocketService webSocketService;

    public Long handle(CreatePostCommand command) {
        var post = new Post(command.authorId(), command.content(), command.tags());
        var savedPost = postRepository.save(post);
        
        // Enviar evento WebSocket para actualización en tiempo real
        webSocketService.sendPostCreatedEvent(savedPost);
        
        return savedPost.getId();
    }

    public Optional<Post> handle(UpdatePostCommand command) {
        return postRepository.findById(command.postId())
                .map(post -> {
                    post.updateContent(command.content());
                    post.clearTags();
                    if (command.tags() != null) {
                        command.tags().forEach(post::addTag);
                    }
                    var updatedPost = postRepository.save(post);
                    
                    // Enviar evento WebSocket para actualización en tiempo real
                    webSocketService.sendPostUpdatedEvent(updatedPost);
                    
                    return updatedPost;
                });
    }

    public boolean handle(DeletePostCommand command) {
        return postRepository.findById(command.postId())
                .filter(post -> post.getAuthorId().equals(command.userId()))
                .map(post -> {
                    post.deactivate();
                    postRepository.save(post);
                    
                    // Enviar evento WebSocket para actualización en tiempo real
                    webSocketService.sendPostDeletedEvent(command.postId(), command.userId());
                    
                    return true;
                })
                .orElse(false);
    }

    public Optional<Long> handle(UploadMediaCommand command) {
        return postRepository.findById(command.postId())
                .map(post -> {
                    var media = new Media(
                            command.fileUrl(),
                            command.originalFilename(),
                            command.mediaType(),
                            command.fileSize(),
                            command.fileExtension()
                    );
                    
                    if (command.altText() != null) {
                        media.setAltText(command.altText());
                    }
                    
                    post.addMediaFile(media);
                    var savedMedia = mediaRepository.save(media);
                    postRepository.save(post);
                    return savedMedia.getId();
                });
    }

    public void incrementViews(Long postId) {
        postRepository.findById(postId)
                .ifPresent(post -> {
                    post.incrementViewsCount();
                    postRepository.save(post);
                });
    }
}