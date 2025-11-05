package com.drawnet.feed_service.domain.model.entities;

import com.drawnet.feed_service.domain.model.aggregates.Post;
import com.drawnet.feed_service.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "reposts", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"original_post_id", "user_id"})
})
@Getter
@NoArgsConstructor
public class Repost extends AuditableModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId; // Usuario que hace el repost
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_post_id", nullable = false)
    private Post originalPost;
    
    @Size(max = 500)
    @Column(name = "comment", length = 500)
    private String comment; // Comentario opcional al repostear
    
    @Column(name = "repost_date", nullable = false)
    private LocalDateTime repostDate;
    
    @Column(name = "active", nullable = false)
    private boolean active = true;
    
    // Constructors
    public Repost(Long userId, Post originalPost) {
        this.userId = userId;
        this.originalPost = originalPost;
        this.repostDate = LocalDateTime.now();
    }
    
    public Repost(Long userId, Post originalPost, String comment) {
        this(userId, originalPost);
        this.comment = comment;
    }
    
    // Lifecycle methods
    @PrePersist
    protected void onCreate() {
        if (repostDate == null) {
            repostDate = LocalDateTime.now();
        }
    }
    
    // Setters
    public void updateComment(String comment) { 
        this.comment = comment; 
    }
    
    public void setActive(boolean active) { 
        this.active = active; 
    }
    
    // Business methods
    public void deactivate() {
        this.active = false;
    }
    
    public void activate() {
        this.active = true;
    }
    
    public boolean hasComment() {
        return comment != null && !comment.trim().isEmpty();
    }
    
    public String getDisplayComment() {
        return hasComment() ? comment : "Shared this post";
    }
    
    public boolean isOwnPost(Long userId) {
        return originalPost.getAuthorId().equals(userId);
    }
    
    public boolean canRepost(Long userId) {
        // Un usuario no puede repostear su propio post
        return !isOwnPost(userId) && originalPost.isActive();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Repost)) return false;
        Repost repost = (Repost) o;
        return userId.equals(repost.userId) && 
               originalPost.getId().equals(repost.originalPost.getId());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(userId, originalPost.getId());
    }
    
    @Override
    public String toString() {
        return "Repost{" +
                "id=" + id +
                ", userId=" + userId +
                ", originalPostId=" + (originalPost != null ? originalPost.getId() : null) +
                ", hasComment=" + hasComment() +
                ", active=" + active +
                '}';
    }
}