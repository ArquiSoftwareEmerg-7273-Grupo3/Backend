package com.drawnet.feed_service.domain.model.entities;

import com.drawnet.feed_service.domain.model.aggregates.Post;
import com.drawnet.feed_service.shared.domain.model.entities.AuditableModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "reactions", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"post_id", "user_id"})
})
@Getter
@NoArgsConstructor
public class Reaction extends AuditableModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type", nullable = false)
    private ReactionType type;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    
    // Constructors
    public Reaction(Long userId, ReactionType type, Post post) {
        this.userId = userId;
        this.type = type;
        this.post = post;
    }
    
    // Lifecycle methods no necesarios - heredados de AuditableModel
    
    // Setters
    public void setPost(Post post) { 
        this.post = post; 
    }
    
    public void changeType(ReactionType type) { 
        this.type = type; 
    }
    
    // Business methods
    public boolean isLike() {
        return ReactionType.LIKE.equals(this.type);
    }
    
    public boolean isDislike() {
        return ReactionType.DISLIKE.equals(this.type);
    }
    
    public boolean isLove() {
        return ReactionType.LOVE.equals(this.type);
    }
    
    public String getEmoji() {
        return type.getEmoji();
    }
    
    public String getDisplayName() {
        return type.getDisplayName();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reaction)) return false;
        Reaction reaction = (Reaction) o;
        return userId.equals(reaction.userId) && 
               post.getId().equals(reaction.post.getId());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(userId, post.getId());
    }
    
    @Override
    public String toString() {
        return "Reaction{" +
                "id=" + id +
                ", userId=" + userId +
                ", type=" + type +
                ", postId=" + (post != null ? post.getId() : null) +
                '}';
    }
}