package com.drawnet.feed_service.interfaces.rest;

import com.drawnet.feed_service.application.internal.commandservices.RepostCommandService;
import com.drawnet.feed_service.application.internal.queryservices.RepostQueryService;
import com.drawnet.feed_service.domain.model.commands.CreateRepostCommand;
import com.drawnet.feed_service.interfaces.rest.resources.*;
import com.drawnet.feed_service.interfaces.rest.transform.RepostResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Reposts", description = "Post repost management")
public class RepostController {

    private final RepostCommandService repostCommandService;
    private final RepostQueryService repostQueryService;

    @PostMapping("/posts/{postId}/reposts")
    @Operation(summary = "Repost a post")
    public ResponseEntity<Long> createRepost(
            @PathVariable Long postId,
            @Valid @RequestBody CreateRepostResource resource,
            @RequestHeader("X-User-Id") Long userId) {
        
        var command = new CreateRepostCommand(postId, userId, resource.comment());
        return repostCommandService.handle(command)
                .map(repostId -> ResponseEntity.status(HttpStatus.CREATED).body(repostId))
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/posts/{postId}/reposts")
    @Operation(summary = "Remove repost")
    public ResponseEntity<Void> removeRepost(
            @PathVariable Long postId,
            @RequestHeader("X-User-Id") Long userId) {
        
        boolean removed = repostCommandService.removeRepost(postId, userId);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/posts/{postId}/reposts/toggle")
    @Operation(summary = "Toggle repost (add if not exists, remove if exists)")
    public ResponseEntity<String> toggleRepost(
            @PathVariable Long postId,
            @RequestBody(required = false) CreateRepostResource resource,
            @RequestHeader("X-User-Id") Long userId) {
        
        String comment = resource != null ? resource.comment() : null;
        boolean result = repostCommandService.toggleRepost(postId, userId, comment);
        String message = result ? "Repost toggled successfully" : "Failed to toggle repost";
        
        return ResponseEntity.ok(message);
    }

    @GetMapping("/posts/{postId}/reposts")
    @Operation(summary = "Get reposts for a post")
    public ResponseEntity<Page<RepostResource>> getRepostsForPost(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        var reposts = repostQueryService.getRepostsForPost(postId, page, size);
        var repostResources = reposts.map(RepostResourceFromEntityAssembler::toResourceFromEntity);
        
        return ResponseEntity.ok(repostResources);
    }

    @GetMapping("/users/{userId}/reposts")
    @Operation(summary = "Get reposts by user")
    public ResponseEntity<Page<RepostResource>> getRepostsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        var reposts = repostQueryService.getRepostsByUser(userId, page, size);
        var repostResources = reposts.map(RepostResourceFromEntityAssembler::toResourceFromEntity);
        
        return ResponseEntity.ok(repostResources);
    }

    @GetMapping("/posts/{postId}/reposts/count")
    @Operation(summary = "Get repost count for a post")
    public ResponseEntity<Long> getRepostCount(@PathVariable Long postId) {
        long count = repostQueryService.countRepostsForPost(postId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/posts/{postId}/reposts/status")
    @Operation(summary = "Check if user has reposted a post")
    public ResponseEntity<Boolean> hasUserReposted(
            @PathVariable Long postId,
            @RequestHeader("X-User-Id") Long userId) {
        
        boolean hasReposted = repostQueryService.hasUserReposted(postId, userId);
        return ResponseEntity.ok(hasReposted);
    }

    @GetMapping("/reposts/recent")
    @Operation(summary = "Get recent reposts")
    public ResponseEntity<Page<RepostResource>> getRecentReposts(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        var reposts = repostQueryService.getRecentReposts(days, page, size);
        var repostResources = reposts.map(RepostResourceFromEntityAssembler::toResourceFromEntity);
        
        return ResponseEntity.ok(repostResources);
    }
}