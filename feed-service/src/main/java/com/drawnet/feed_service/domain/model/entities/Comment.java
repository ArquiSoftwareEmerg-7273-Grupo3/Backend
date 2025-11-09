package com.drawnet.feed_service.domain.model.entities;

import com.drawnet.feed_service.domain.model.aggregates.Post;
import com.drawnet.feed_service.shared.domain.model.entities.AuditableModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor
public class Comment extends AuditableModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @NotBlank
    @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
    @Column(name = "content", nullable = false, length = 1000)
    private String content;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    
    // Para comentarios anidados (respuestas a comentarios)
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;
    
    @JsonIgnore
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();
    
    @Column(name = "active", nullable = false)
    private boolean active = true;
    
    // Constructors
    public Comment(Long userId, String content, Post post) {
        this.userId = userId;
        this.content = content;
        this.post = post;
    }
    
    public Comment(Long userId, String content, Post post, Comment parentComment) {
        this(userId, content, post);
        this.parentComment = parentComment;
    }
    
    // Lifecycle methods no necesarios - heredados de AuditableModel
    
    // Setters
    public void setPost(Post post) { 
        this.post = post; 
    }
    
    public void updateContent(String content) { 
        this.content = content;
    }
    
    // Business methods
    public void addReply(Comment reply) {
        replies.add(reply);
        reply.parentComment = this;
    }
    
    public void removeReply(Comment reply) {
        replies.remove(reply);
        reply.parentComment = null;
    }
    
    public boolean isReply() {
        return parentComment != null;
    }
    
    public boolean hasReplies() {
        return !replies.isEmpty();
    }
    
    public int getRepliesCount() {
        return replies.size();
    }
    
    public void deactivate() {
        this.active = false;
    }
    
    public void activate() {
        this.active = true;
    }
    
    // Getters defensivos
    public List<Comment> getRepliesUnmodifiable() {
        return new ArrayList<>(replies);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", userId=" + userId +
                ", content='" + (content != null && content.length() > 30 ? 
                    content.substring(0, 30) + "..." : content) + '\'' +
                ", active=" + active +
                ", repliesCount=" + replies.size() +
                '}';
    }
}