package com.drawnet.feed_service.application.internal.queryservices;

import com.drawnet.feed_service.domain.model.aggregates.Post;
import com.drawnet.feed_service.domain.model.querys.GetPostsQuery;
import com.drawnet.feed_service.infrastructure.persistence.jpa.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryService {

    private final PostRepository postRepository;

    public Page<Post> handle(GetPostsQuery query) {
        Pageable pageable = PageRequest.of(query.page(), query.size());
        
        // Feed personalizado de usuarios seguidos
        if (query.followingUserIds() != null && !query.followingUserIds().isEmpty()) {
            return postRepository.findPostsByFollowingUsers(query.followingUserIds(), pageable);
        }
        
        // Posts por autor específico
        if (query.authorId() != null) {
            return postRepository.findByAuthorIdAndActiveOrderByCreatedAtDesc(
                query.authorId(), true, pageable);
        }
        
        // Posts por tags
        if (query.tags() != null && !query.tags().isEmpty()) {
            return postRepository.findByTagsIn(query.tags(), pageable);
        }
        
        // Posts por contenido
        if (query.contentFilter() != null && !query.contentFilter().trim().isEmpty()) {
            return postRepository.findByContentContaining(query.contentFilter(), pageable);
        }
        
        // Posts recientes desde una fecha
        if (query.since() != null) {
            return postRepository.findRecentPosts(query.since(), pageable);
        }
        
        // Posts ordenados por criterio
        return switch (query.sortBy()) {
            case MOST_LIKED -> postRepository.findByActiveOrderByCommentsCountDescCreatedAtDesc(true, pageable);
            case MOST_COMMENTED -> postRepository.findByActiveOrderByCommentsCountDescCreatedAtDesc(true, pageable);
            case TRENDING -> postRepository.findTrendingPosts(LocalDateTime.now().minusDays(7), pageable);
            case OLDEST -> postRepository.findByActiveOrderByCreatedAtAsc(true, pageable);
            default -> postRepository.findByActiveOrderByCreatedAtDesc(true, pageable);
        };
    }

    public Optional<Post> findPostById(Long postId) {
        return postRepository.findById(postId)
                .filter(Post::isActive);
    }

    public Page<Post> getPostsByAuthor(Long authorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByAuthorIdAndActiveOrderByCreatedAtDesc(authorId, true, pageable);
    }

    public Page<Post> getTrendingPosts(int days, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return postRepository.findTrendingPosts(since, pageable);
    }

    public Page<Post> getPostsWithMedia(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findPostsWithMedia(pageable);
    }

    public long countPostsByAuthor(Long authorId) {
        return postRepository.countByAuthorIdAndActive(authorId, true);
    }

    public Page<Post> getFeedForUser(GetPostsQuery query) {
        // Incrementar vistas para los posts visualizados
        var posts = handle(query);
        posts.getContent().forEach(post -> {
            post.incrementViewsCount();
            postRepository.save(post);
        });
        return posts;
    }
}