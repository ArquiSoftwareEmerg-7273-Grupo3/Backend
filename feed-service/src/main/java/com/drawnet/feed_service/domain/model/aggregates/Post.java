package com.drawnet.feed_service.domain.model.aggregates;

import com.drawnet.feed_service.domain.model.entities.*;
import com.drawnet.feed_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor
public class Post extends AuditableAbstractAggregateRoot<Post> {

    @NotNull(message = "Author ID cannot be null")
    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @NotBlank(message = "Content cannot be empty")
    @Size(max = 2000, message = "Content cannot exceed 2000 characters")
    @Column(name = "content", nullable = false, length = 2000)
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Reaction> reactions = new ArrayList<>();
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Media> mediaFiles = new ArrayList<>();
    
    @OneToMany(mappedBy = "originalPost", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Repost> reposts = new ArrayList<>();
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "tag", length = 50)
    private Set<String> tags = new HashSet<>();

    // Métricas de engagement
    @Column(name = "reactions_count", nullable = false)
    private Integer reactionsCount = 0;
    
    @Column(name = "comments_count", nullable = false)
    private Integer commentsCount = 0;
    
    @Column(name = "reposts_count", nullable = false)
    private Integer repostsCount = 0;
    
    @Column(name = "views_count", nullable = false)
    private Integer viewsCount = 0;

    // Constructores
    public Post(Long authorId, String content) {
        this.authorId = authorId;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Post(Long authorId, String content, Set<String> tags) {
        this(authorId, content);
        this.tags = tags != null ? new HashSet<>(tags) : new HashSet<>();
    }

    // Lifecycle methods
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // === MÉTODOS DE NEGOCIO ===

    // Setters básicos
    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    public void setActive(boolean active) {
        this.active = active;
        this.updatedAt = LocalDateTime.now();
    }

    // Gestión de comentarios
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
        incrementCommentsCount();
    }

    public void removeComment(Comment comment) {
        if (comments.remove(comment)) {
            comment.setPost(null);
            decrementCommentsCount();
        }
    }

    // Gestión de reacciones
    public void addReaction(Reaction reaction) {
        // Verificar si el usuario ya tiene una reacción
        removeExistingReactionByUser(reaction.getUserId());
        reactions.add(reaction);
        reaction.setPost(this);
        
        // Contar todas las reacciones, no solo LIKE
        incrementLikesCount();
    }

    public void removeReaction(Reaction reaction) {
        if (reactions.remove(reaction)) {
            reaction.setPost(null);
            // Decrementar contador para cualquier tipo de reacción
            decrementLikesCount();
        }
    }
    
    private void removeExistingReactionByUser(Long userId) {
        reactions.removeIf(reaction -> {
            if (reaction.getUserId().equals(userId)) {
                // Decrementar contador para cualquier tipo de reacción
                decrementLikesCount();
                return true;
            }
            return false;
        });
    }

    // Gestión de media
    public void addMediaFile(Media media) {
        mediaFiles.add(media);
        media.setPost(this);
    }

    public void removeMediaFile(Media media) {
        if (mediaFiles.remove(media)) {
            media.setPost(null);
        }
    }

    // Gestión de reposts
    public void addRepost(Repost repost) {
        reposts.add(repost);
        incrementRepostsCount();
    }

    public void removeRepost(Repost repost) {
        if (reposts.remove(repost)) {
            decrementRepostsCount();
        }
    }

    // Gestión de tags
    public void addTag(String tag) {
        if (tag != null && !tag.trim().isEmpty()) {
            this.tags.add(tag.trim().toLowerCase());
        }
    }

    public void removeTag(String tag) {
        this.tags.remove(tag);
    }

    public void clearTags() {
        this.tags.clear();
    }

    // Métricas y contadores
    private void incrementLikesCount() {
        this.reactionsCount++;
    }
    
    private void decrementLikesCount() {
        this.reactionsCount = Math.max(0, this.reactionsCount - 1);
    }
    
    private void incrementCommentsCount() {
        this.commentsCount++;
    }
    
    private void decrementCommentsCount() {
        this.commentsCount = Math.max(0, this.commentsCount - 1);
    }
    
    private void incrementRepostsCount() {
        this.repostsCount++;
    }
    
    private void decrementRepostsCount() {
        this.repostsCount = Math.max(0, this.repostsCount - 1);
    }

    public void incrementViewsCount() {
        this.viewsCount++;
    }

    // Métodos de consulta
    public boolean hasMedia() {
        return !mediaFiles.isEmpty();
    }
    
    public boolean hasImages() {
        return mediaFiles.stream().anyMatch(Media::isImage);
    }
    
    public boolean hasVideos() {
        return mediaFiles.stream().anyMatch(Media::isVideo);
    }

    public boolean hasReactionFromUser(Long userId) {
        return reactions.stream().anyMatch(r -> r.getUserId().equals(userId));
    }
    
    public Optional<Reaction> getReactionByUser(Long userId) {
        return reactions.stream()
                .filter(r -> r.getUserId().equals(userId))
                .findFirst();
    }

    public boolean isRepostedByUser(Long userId) {
        return reposts.stream().anyMatch(r -> r.getUserId().equals(userId) && r.isActive());
    }

    // Soft delete
    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }

    // Cálculo de engagement
    public double getEngagementRate() {
        if (viewsCount == 0) return 0.0;
        int totalEngagements = reactionsCount + commentsCount + repostsCount;
        return (double) totalEngagements / viewsCount * 100;
    }

    // Getters defensivos para colecciones
    public List<Comment> getCommentsUnmodifiable() {
        return Collections.unmodifiableList(comments);
    }

    public List<Reaction> getReactionsUnmodifiable() {
        return Collections.unmodifiableList(reactions);
    }
    
    public List<Media> getMediaFilesUnmodifiable() {
        return Collections.unmodifiableList(mediaFiles);
    }
    
    public List<Repost> getRepostsUnmodifiable() {
        return Collections.unmodifiableList(reposts);
    }

    public Set<String> getTagsUnmodifiable() {
        return Collections.unmodifiableSet(tags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
        Post post = (Post) o;
        return Objects.equals(getId(), post.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + getId() +
                ", authorId=" + authorId +
                ", content='" + (content != null && content.length() > 50 ? 
                    content.substring(0, 50) + "..." : content) + '\'' +
                ", active=" + active +
                ", reactionsCount=" + reactionsCount +
                ", commentsCount=" + commentsCount +
                ", repostsCount=" + repostsCount +
                ", viewsCount=" + viewsCount +
                '}';
    }
}