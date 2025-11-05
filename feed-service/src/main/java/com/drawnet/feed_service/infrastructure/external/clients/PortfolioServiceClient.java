package com.drawnet.feed_service.infrastructure.external.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Cliente Feign para comunicación con el servicio de portafolios
 */
@FeignClient(
    name = "portafolio-service", 
    path = "/api/v1/portfolios",
    fallback = PortfolioServiceClientFallback.class
)
public interface PortfolioServiceClient {

    /**
     * Obtener información básica de una obra/artwork
     */
    @GetMapping("/artworks/{artworkId}")
    ArtworkDto getArtwork(@PathVariable("artworkId") String artworkId);

    /**
     * Obtener obras más recientes de un usuario
     */
    @GetMapping("/users/{userId}/artworks/recent")
    List<ArtworkDto> getUserRecentArtworks(
        @PathVariable("userId") String userId,
        @RequestParam(value = "limit", defaultValue = "10") int limit
    );

    /**
     * Obtener estadísticas del portafolio
     */
    @GetMapping("/users/{userId}/stats")
    PortfolioStatsDto getPortfolioStats(@PathVariable("userId") String userId);

    /**
     * Verificar si una obra existe
     */
    @GetMapping("/artworks/{artworkId}/exists")
    boolean artworkExists(@PathVariable("artworkId") String artworkId);

    /**
     * Obtener categorías/tags de obras populares
     */
    @GetMapping("/categories/trending")
    List<String> getTrendingCategories();

    /**
     * DTO para información de obra de arte
     */
    record ArtworkDto(
        String id,
        String title,
        String description,
        String thumbnailUrl,
        String category,
        List<String> tags,
        String userId,
        LocalDateTime createdAt,
        int likesCount,
        int viewsCount
    ) {}

    /**
     * DTO para estadísticas del portafolio
     */
    record PortfolioStatsDto(
        String userId,
        int totalArtworks,
        int totalLikes,
        int totalViews,
        String mostPopularCategory
    ) {}
}