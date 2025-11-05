package com.drawnet.feed_service.interfaces.rest;

import com.drawnet.feed_service.application.internal.commandservices.ReactionCommandService;
import com.drawnet.feed_service.domain.model.commands.*;
import com.drawnet.feed_service.domain.model.entities.ReactionType;
import com.drawnet.feed_service.infrastructure.persistence.jpa.repositories.ReactionRepository;
import com.drawnet.feed_service.interfaces.rest.resources.CreateReactionResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/posts/{postId}/reactions")
@RequiredArgsConstructor
@Tag(name = "Reactions", description = "Post reaction management")
public class ReactionController {

    private final ReactionCommandService reactionCommandService;
    private final ReactionRepository reactionRepository;

    @PostMapping
    @Operation(summary = "Add or update reaction to a post")
    public ResponseEntity<Long> addReaction(
            @PathVariable Long postId,
            @Valid @RequestBody CreateReactionResource resource,
            @RequestHeader("X-User-Id") Long userId) {
        
        var command = new CreateReactionCommand(postId, userId, resource.reactionType());
        return reactionCommandService.handle(command)
                .map(reactionId -> ResponseEntity.status(HttpStatus.CREATED).body(reactionId))
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping
    @Operation(summary = "Remove reaction from a post")
    public ResponseEntity<Void> removeReaction(
            @PathVariable Long postId,
            @RequestHeader("X-User-Id") Long userId) {
        
        var command = new RemoveReactionCommand(postId, userId);
        boolean removed = reactionCommandService.handle(command);
        
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/toggle/{reactionType}")
    @Operation(summary = "Toggle reaction (add if not exists, remove if same type, change if different)")
    public ResponseEntity<String> toggleReaction(
            @PathVariable Long postId,
            @PathVariable ReactionType reactionType,
            @RequestHeader("X-User-Id") Long userId) {
        
        var result = reactionCommandService.toggleReaction(postId, userId, reactionType);
        String message = result.isPresent() ? "Reaction added/updated" : "Reaction removed";
        
        return ResponseEntity.ok(message);
    }

    @GetMapping("/stats")
    @Operation(summary = "Get reaction statistics for a post")
    public ResponseEntity<Map<String, Long>> getReactionStats(@PathVariable Long postId) {
        var stats = reactionRepository.getReactionStatsByPost(postId);
        
        Map<String, Long> reactionCounts = stats.stream()
                .collect(java.util.stream.Collectors.toMap(
                    stat -> ((ReactionType) stat[0]).getDisplayName(),
                    stat -> (Long) stat[1]
                ));
        
        return ResponseEntity.ok(reactionCounts);
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "Get user's reaction to a post")
    public ResponseEntity<ReactionType> getUserReaction(
            @PathVariable Long postId,
            @PathVariable Long userId) {
        
        return reactionRepository.findByPostIdAndUserId(postId, userId)
                .map(reaction -> ResponseEntity.ok(reaction.getType()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/types")
    @Operation(summary = "Get all available reaction types")
    public ResponseEntity<List<Map<String, String>>> getReactionTypes() {
        var types = java.util.Arrays.stream(ReactionType.values())
                .map(type -> Map.of(
                    "name", type.getDisplayName(),
                    "emoji", type.getEmoji()
                ))
                .toList();
        
        return ResponseEntity.ok(types);
    }
}