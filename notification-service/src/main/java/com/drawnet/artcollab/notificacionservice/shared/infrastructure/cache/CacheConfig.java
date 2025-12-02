package com.drawnet.artcollab.notificacionservice.shared.infrastructure.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de caché simple usando ConcurrentMapCache (en memoria)
 * No requiere dependencias externas como Caffeine
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        // Usar ConcurrentMapCache que viene incluido en Spring
        // Es más simple pero sin TTL automático
        return new ConcurrentMapCacheManager("notifications", "unreadNotifications");
    }
}
