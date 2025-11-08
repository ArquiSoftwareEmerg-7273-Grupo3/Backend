package com.drawnet.feed_service.interfaces.rest;

import com.drawnet.feed_service.application.internal.commandservices.CommentCommandService;
import com.drawnet.feed_service.application.internal.queryservices.CommentQueryService;
import com.drawnet.feed_service.domain.model.commands.*;
import com.drawnet.feed_service.domain.model.querys.GetCommentsQuery;
import com.drawnet.feed_service.infrastructure.security.jwt.CurrentUserId;
import com.drawnet.feed_service.interfaces.rest.resources.*;
import com.drawnet.feed_service.interfaces.rest.transform.CommentResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Comments", description = "Comment management operations")
public class CommentController {

    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;

    @PostMapping("/posts/{postId}/comments")
    @Operation(summary = "Create a comment on a post")
    public ResponseEntity<Long> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CreateCommentResource resource,
            @Parameter(hidden = true) @CurrentUserId Long userId) {
        
        var command = new CreateCommentCommand(postId, userId, resource.content(), resource.parentCommentId());
        return commentCommandService.handle(command)
                .map(commentId -> ResponseEntity.status(HttpStatus.CREATED).body(commentId))
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/posts/{postId}/comments")
    @Operation(summary = "Get comments for a post")
    public ResponseEntity<Page<CommentResource>> getCommentsForPost(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        var query = new GetCommentsQuery(postId, null, null, page, size);
        var comments = commentQueryService.handle(query);
        var commentResources = comments.map(CommentResourceFromEntityAssembler::toResourceFromEntity);
        
        return ResponseEntity.ok(commentResources);
    }

    @GetMapping("/comments/{commentId}")
    @Operation(summary = "Get comment by ID")
    public ResponseEntity<CommentResource> getCommentById(@PathVariable Long commentId) {
        return commentQueryService.findCommentById(commentId)
                .map(CommentResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/comments/{commentId}/replies")
    @Operation(summary = "Get replies for a comment")
    public ResponseEntity<List<CommentResource>> getRepliesForComment(@PathVariable Long commentId) {
        var replies = commentQueryService.getRepliesForComment(commentId);
        var replyResources = replies.stream()
                .map(CommentResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(replyResources);
    }

    

    @GetMapping("/users/{userId}/comments")
    @Operation(summary = "Get comments by user")
    public ResponseEntity<Page<CommentResource>> getCommentsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        var query = new GetCommentsQuery(null, null, userId, page, size);
        var comments = commentQueryService.handle(query);
        var commentResources = comments.map(CommentResourceFromEntityAssembler::toResourceFromEntity);
        
        return ResponseEntity.ok(commentResources);
    }
}