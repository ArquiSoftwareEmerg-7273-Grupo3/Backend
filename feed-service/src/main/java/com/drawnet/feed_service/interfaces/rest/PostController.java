package com.drawnet.feed_service.interfaces.rest;

import com.drawnet.feed_service.application.internal.commandservices.PostCommandService;
import com.drawnet.feed_service.application.internal.queryservices.PostQueryService;
import com.drawnet.feed_service.domain.model.commands.*;
import com.drawnet.feed_service.domain.model.querys.GetPostsQuery;
import com.drawnet.feed_service.interfaces.rest.resources.*;
import com.drawnet.feed_service.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Tag(name = "Posts", description = "Post management operations")
public class PostController {

    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;

    @PostMapping
    @Operation(summary = "Create a new post")
    public ResponseEntity<Long> createPost(
            @Valid @RequestBody CreatePostResource resource,
            @RequestHeader("X-User-Id") Long userId) {
        
        var command = new CreatePostCommand(userId, resource.content(), resource.tags());
        var postId = postCommandService.handle(command);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(postId);
    }

    @GetMapping
    @Operation(summary = "Get posts with filtering and pagination")
    public ResponseEntity<Page<PostResource>> getPosts(
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) List<Long> followingUserIds,
            @RequestParam(required = false) String since,
            @RequestParam(defaultValue = "NEWEST") GetPostsQuery.PostSortBy sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        LocalDateTime sinceDate = null;
        if (since != null && !since.isEmpty()) {
            sinceDate = LocalDateTime.parse(since);
        }

        var query = new GetPostsQuery(authorId, tags, content, followingUserIds, 
                                    sinceDate, sortBy, page, size);
        var posts = postQueryService.handle(query);
        var postResources = posts.map(PostResourceFromEntityAssembler::toResourceFromEntity);
        
        return ResponseEntity.ok(postResources);
    }

    @GetMapping("/{postId}")
    @Operation(summary = "Get post by ID")
    public ResponseEntity<PostResource> getPostById(@PathVariable Long postId) {
        return postQueryService.findPostById(postId)
                .map(PostResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{postId}")
    @Operation(summary = "Update a post")
    public ResponseEntity<PostResource> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody CreatePostResource resource,
            @RequestHeader("X-User-Id") Long userId) {
        
        var command = new UpdatePostCommand(postId, resource.content(), resource.tags());
        return postCommandService.handle(command)
                .map(PostResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "Delete a post")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @RequestHeader("X-User-Id") Long userId) {
        
        var command = new DeletePostCommand(postId, userId);
        boolean deleted = postCommandService.handle(command);
        
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/trending")
    @Operation(summary = "Get trending posts")
    public ResponseEntity<Page<PostResource>> getTrendingPosts(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        var posts = postQueryService.getTrendingPosts(days, page, size);
        var postResources = posts.map(PostResourceFromEntityAssembler::toResourceFromEntity);
        
        return ResponseEntity.ok(postResources);
    }

    @GetMapping("/author/{authorId}")
    @Operation(summary = "Get posts by author")
    public ResponseEntity<Page<PostResource>> getPostsByAuthor(
            @PathVariable Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        var posts = postQueryService.getPostsByAuthor(authorId, page, size);
        var postResources = posts.map(PostResourceFromEntityAssembler::toResourceFromEntity);
        
        return ResponseEntity.ok(postResources);
    }

    @GetMapping("/feed")
    @Operation(summary = "Get personalized feed for user")
    public ResponseEntity<Page<PostResource>> getUserFeed(
            @RequestParam List<Long> followingUserIds,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        var query = new GetPostsQuery(null, null, null, followingUserIds, 
                                    null, GetPostsQuery.PostSortBy.NEWEST, page, size);
        var posts = postQueryService.getFeedForUser(query);
        var postResources = posts.map(PostResourceFromEntityAssembler::toResourceFromEntity);
        
        return ResponseEntity.ok(postResources);
    }

    @PostMapping("/{postId}/view")
    @Operation(summary = "Increment post view count")
    public ResponseEntity<Void> incrementViews(@PathVariable Long postId) {
        postCommandService.incrementViews(postId);
        return ResponseEntity.ok().build();
    }
}