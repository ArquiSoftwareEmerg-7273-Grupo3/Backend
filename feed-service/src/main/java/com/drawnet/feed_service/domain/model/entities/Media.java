package com.drawnet.feed_service.domain.model.entities;

import com.drawnet.feed_service.domain.model.aggregates.Post;
import com.drawnet.feed_service.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "media")
@Getter
@NoArgsConstructor
public class Media extends AuditableModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(max = 500)
    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;
    
    @NotBlank
    @Size(max = 255)
    @Column(name = "original_filename", nullable = false)
    private String originalFilename;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "media_type", nullable = false)
    private MediaType mediaType;
    
    @Column(name = "file_size")
    private Long fileSize; // en bytes
    
    @Size(max = 10)
    @Column(name = "file_extension", length = 10)
    private String fileExtension;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    
    @Column(name = "upload_date", nullable = false)
    private LocalDateTime uploadDate;
    
    @Column(name = "alt_text", length = 255)
    private String altText; // Para accesibilidad
    
    // Constructors
    public Media(String fileUrl, String originalFilename, MediaType mediaType, 
                Long fileSize, String fileExtension) {
        this.fileUrl = fileUrl;
        this.originalFilename = originalFilename;
        this.mediaType = mediaType;
        this.fileSize = fileSize;
        this.fileExtension = fileExtension;
        this.uploadDate = LocalDateTime.now();
    }
    
    // Lifecycle methods
    @PrePersist
    protected void onCreate() {
        if (uploadDate == null) {
            uploadDate = LocalDateTime.now();
        }
    }
    
    // Setters
    public void setPost(Post post) { 
        this.post = post; 
    }
    
    public void setAltText(String altText) { 
        this.altText = altText; 
    }
    
    public void updateFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
    
    // Business methods
    public boolean isImage() {
        return MediaType.IMAGE.equals(this.mediaType);
    }
    
    public boolean isVideo() {
        return MediaType.VIDEO.equals(this.mediaType);
    }
    
    public boolean isDocument() {
        return MediaType.DOCUMENT.equals(this.mediaType);
    }
    
    public String getFormattedFileSize() {
        if (fileSize == null) return "Unknown";
        
        double bytes = fileSize.doubleValue();
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024 * 1024));
        return String.format("%.1f GB", bytes / (1024 * 1024 * 1024));
    }
    
    public boolean isValidSize(long maxSizeInBytes) {
        return fileSize != null && fileSize <= maxSizeInBytes;
    }
    
    public boolean hasValidExtension() {
        return mediaType.isValidExtension(fileExtension);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Media)) return false;
        Media media = (Media) o;
        return Objects.equals(id, media.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Media{" +
                "id=" + id +
                ", originalFilename='" + originalFilename + '\'' +
                ", mediaType=" + mediaType +
                ", fileSize=" + getFormattedFileSize() +
                ", fileExtension='" + fileExtension + '\'' +
                '}';
    }
}