package com.drawnet.feed_service.infrastructure.external.clients;

import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Collections;

/**
 * Fallback para PortfolioServiceClient
 */
@Component
public class PortfolioServiceClientFallback implements PortfolioServiceClient {

    @Override
    public ArtworkDto getArtwork(String artworkId) {
        return new ArtworkDto(
            artworkId,
            "Obra no disponible",
            "Información temporalmente no disponible",
            "/default-artwork.png",
            "general",
            Collections.emptyList(),
            "unknown",
            LocalDateTime.now(),
            0,
            0
        );
    }

    @Override
    public List<ArtworkDto> getUserRecentArtworks(String userId, int limit) {
        return Collections.emptyList();
    }

    @Override
    public PortfolioStatsDto getPortfolioStats(String userId) {
        return new PortfolioStatsDto(
            userId,
            0,
            0,
            0,
            "general"
        );
    }

    @Override
    public boolean artworkExists(String artworkId) {
        return false;
    }

    @Override
    public List<String> getTrendingCategories() {
        return List.of("arte", "digital", "tradicional", "diseño");
    }
}