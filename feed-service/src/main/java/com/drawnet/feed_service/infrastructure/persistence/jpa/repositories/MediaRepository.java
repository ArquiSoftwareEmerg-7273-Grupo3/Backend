package com.drawnet.feed_service.infrastructure.persistence.jpa.repositories;

import com.drawnet.feed_service.domain.model.entities.Media;
import com.drawnet.feed_service.domain.model.entities.MediaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    
    // Media de un post específico
    List<Media> findByPostIdOrderByUploadDateAsc(Long postId);
    
    // Media por tipo
    List<Media> findByMediaTypeOrderByUploadDateDesc(MediaType mediaType);
    
    // Media de un post por tipo
    List<Media> findByPostIdAndMediaTypeOrderByUploadDateAsc(Long postId, MediaType mediaType);
    
    // Contar media por post
    long countByPostId(Long postId);
    
    // Media reciente
    @Query("SELECT m FROM Media m WHERE m.uploadDate >= :since ORDER BY m.uploadDate DESC")
    List<Media> findRecentMedia(@Param("since") LocalDateTime since);
    
    // Media por tamaño
    @Query("SELECT m FROM Media m WHERE m.fileSize <= :maxSize ORDER BY m.uploadDate DESC")
    List<Media> findByFileSizeLessThanEqual(@Param("maxSize") Long maxSize);
    
    // Estadísticas de uso de almacenamiento
    @Query("SELECT SUM(m.fileSize) FROM Media m WHERE m.mediaType = :type")
    Long getTotalSizeByMediaType(@Param("type") MediaType type);
    
    // Media huérfana (sin post asociado)
    List<Media> findByPostIsNull();
    
    // Media por extensión
    List<Media> findByFileExtensionOrderByUploadDateDesc(String extension);
}