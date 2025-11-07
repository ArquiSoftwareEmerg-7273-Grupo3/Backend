package com.drawnet.feed_service.domain.services;

import com.drawnet.feed_service.domain.model.aggregates.Post;
import com.drawnet.feed_service.domain.model.entities.Repost;
import com.drawnet.feed_service.infrastructure.persistence.jpa.repositories.PostRepository;
import com.drawnet.feed_service.infrastructure.persistence.jpa.repositories.CommentRepository;
import com.drawnet.feed_service.infrastructure.persistence.jpa.repositories.ReactionRepository;
import com.drawnet.feed_service.infrastructure.persistence.jpa.repositories.RepostRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio de dominio para operaciones complejas del feed
 * Maneja lógica de negocio que involucra múltiples agregados
 */
@Service
@Transactional(readOnly = true)
public class FeedDomainService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReactionRepository reactionRepository;
    private final RepostRepository repostRepository;

    public FeedDomainService(PostRepository postRepository,
                            CommentRepository commentRepository,
                            ReactionRepository reactionRepository,
                            RepostRepository repostRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.reactionRepository = reactionRepository;
        this.repostRepository = repostRepository;
    }

    /**
     * Generar feed personalizado para un usuario
     * Combina posts originales, reposts y algoritmo de relevancia
     */
    public Page<Post> generatePersonalizedFeed(String userId, List<String> followingUserIds, Pageable pageable) {
        // 1. Obtener posts de usuarios seguidos
        List<Post> followingPosts = postRepository.findByUserIdIn(followingUserIds);
        
        // 2. Obtener reposts de usuarios seguidos
        List<Long> followingUserIdsLong = followingUserIds.stream()
                .map(Long::parseLong)
                .toList();
        List<Repost> followingReposts = repostRepository.findByUserIdIn(followingUserIdsLong);
        
        // 3. Combinar y ordenar por algoritmo de relevancia
        List<Post> combinedPosts = new ArrayList<>();
        combinedPosts.addAll(followingPosts);
        combinedPosts.addAll(followingReposts.stream()
                .map(Repost::getOriginalPost)
                .collect(Collectors.toList()));

        // 4. Eliminar duplicados y aplicar scoring
        List<Post> scoredPosts = combinedPosts.stream()
                .distinct()
                .filter(post -> !post.getAuthorId().toString().equals(userId)) // No mostrar propios posts
                .sorted((p1, p2) -> Double.compare(calculateRelevanceScore(p2, userId), calculateRelevanceScore(p1, userId)))
                .collect(Collectors.toList());

        return convertToPage(scoredPosts, pageable);
    }

    /**
     * Calcular score de relevancia para un post
     * Considera engagement, recencia y relación con el usuario
     */
    public double calculateRelevanceScore(Post post, String userId) {
        double score = 0.0;
        
        // Factor de tiempo (posts más recientes tienen mayor score)
        long hoursOld = java.time.Duration.between(post.getCreatedAt(), LocalDateTime.now()).toHours();
        double timeScore = Math.max(0, 100 - (hoursOld * 0.5)); // Decae 0.5 puntos por hora
        
        // Factor de engagement
        double engagementScore = (post.getLikesCount() * 2) + 
                               (post.getCommentsCount() * 3) + 
                               (post.getRepostsCount() * 5);
        
        // Factor de ratio engagement/tiempo
        double engagementRate = engagementScore / Math.max(1, hoursOld);
        
        // Combinación final
        score = (timeScore * 0.3) + (engagementScore * 0.4) + (engagementRate * 0.3);
        
        return score;
    }

    /**
     * Detectar contenido trending en tiempo real
     */
    public List<Post> detectTrendingContent(int hours, int limit) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        
        return postRepository.findAll().stream()
                .filter(post -> post.getCreatedAt().isAfter(since))
                .sorted((p1, p2) -> {
                    double score1 = calculateTrendingScore(p1, hours);
                    double score2 = calculateTrendingScore(p2, hours);
                    return Double.compare(score2, score1);
                })
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Calcular score de trending basado en velocidad de engagement
     */
    private double calculateTrendingScore(Post post, int timeWindowHours) {
        long hoursOld = java.time.Duration.between(post.getCreatedAt(), LocalDateTime.now()).toHours();
        if (hoursOld == 0) hoursOld = 1;
        
        double totalEngagement = post.getLikesCount() + post.getCommentsCount() + post.getRepostsCount();
        double velocityScore = totalEngagement / hoursOld;
        
        // Boost para posts muy recientes
        double recencyBoost = hoursOld <= 1 ? 2.0 : (hoursOld <= 6 ? 1.5 : 1.0);
        
        return velocityScore * recencyBoost;
    }

    /**
     * Análisis de contenido viral
     */
    public Map<String, Object> analyzeViralContent(Post post) {
        Map<String, Object> analysis = new HashMap<>();
        
        // Métricas básicas
        analysis.put("totalEngagement", post.getLikesCount() + post.getCommentsCount() + post.getRepostsCount());
        analysis.put("engagementRate", calculateEngagementRate(post));
        analysis.put("viralityScore", calculateViralityScore(post));
        
        // Análisis temporal
        long hoursOld = java.time.Duration.between(post.getCreatedAt(), LocalDateTime.now()).toHours();
        analysis.put("hoursOld", hoursOld);
        analysis.put("avgEngagementPerHour", 
                    (double)(post.getLikesCount() + post.getCommentsCount() + post.getRepostsCount()) / Math.max(1, hoursOld));
        
        // Distribución de engagement
        Map<String, Integer> engagementBreakdown = new HashMap<>();
        engagementBreakdown.put("likes", post.getLikesCount());
        engagementBreakdown.put("comments", post.getCommentsCount());
        engagementBreakdown.put("reposts", post.getRepostsCount());
        analysis.put("engagementBreakdown", engagementBreakdown);
        
        return analysis;
    }

    /**
     * Detectar spam o contenido inapropiado
     */
    public ContentModerationResult moderateContent(Post post) {
        ContentModerationResult result = new ContentModerationResult();
        
        // Verificar contenido duplicado reciente
        if (isDuplicateContent(post)) {
            result.addFlag("DUPLICATE_CONTENT", "Contenido duplicado detectado");
        }
        
        // Verificar spam por frecuencia
        if (isSpamByFrequency(post.getAuthorId().toString())) {
            result.addFlag("SPAM_FREQUENCY", "Usuario publicando demasiado frecuentemente");
        }
        
        // Verificar patrones de engagement sospechosos
        if (hasSuspiciousEngagement(post)) {
            result.addFlag("SUSPICIOUS_ENGAGEMENT", "Patrones de engagement anómalos");
        }
        
        return result;
    }

    /**
     * Recomendar contenido basado en interacciones del usuario
     */
    public List<Post> recommendContent(String userId, int limit) {
        // Obtener posts con los que el usuario ha interactuado
        Long userIdLong = Long.parseLong(userId);
        List<String> interactedPostIds = reactionRepository.findByUserId(userIdLong)
                .stream()
                .map(reaction -> reaction.getPost().getId().toString())
                .collect(Collectors.toList());
        
        List<String> commentedPostIds = commentRepository.findByUserId(userIdLong)
                .stream()
                .map(comment -> comment.getPost().getId().toString())
                .collect(Collectors.toList());
        
        Set<String> allInteractedPostIds = new HashSet<>();
        allInteractedPostIds.addAll(interactedPostIds);
        allInteractedPostIds.addAll(commentedPostIds);
        
        if (allInteractedPostIds.isEmpty()) {
            // Si no hay interacciones, recomendar trending
            return detectTrendingContent(24, limit);
        }
        
        // Encontrar usuarios similares basado en interacciones comunes
        List<String> similarUsers = findSimilarUsers(userId, allInteractedPostIds);
        
        // Recomendar posts de usuarios similares que el usuario no ha visto
        return postRepository.findByUserIdIn(similarUsers)
                .stream()
                .filter(post -> !post.getAuthorId().toString().equals(userId))
                .filter(post -> !allInteractedPostIds.contains(post.getId().toString()))
                .sorted((p1, p2) -> Double.compare(calculateRelevanceScore(p2, userId), calculateRelevanceScore(p1, userId)))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Métodos auxiliares privados
    
    private double calculateEngagementRate(Post post) {
        // Asumir que las vistas están en algún lugar o usar una fórmula alternativa
        int totalInteractions = post.getLikesCount() + post.getCommentsCount() + post.getRepostsCount();
        // Como no tenemos vistas, usar una heurística basada en followers o tiempo
        return totalInteractions > 0 ? totalInteractions / 100.0 : 0.0;
    }
    
    private double calculateViralityScore(Post post) {
        long hoursOld = java.time.Duration.between(post.getCreatedAt(), LocalDateTime.now()).toHours();
        double totalEngagement = post.getLikesCount() + post.getCommentsCount() + post.getRepostsCount();
        
        if (hoursOld <= 0) hoursOld = 1;
        
        double velocity = totalEngagement / hoursOld;
        double shareRatio = post.getRepostsCount() > 0 ? 
                          (double) post.getRepostsCount() / totalEngagement : 0;
        
        return velocity * (1 + shareRatio * 2); // Los shares tienen peso extra
    }
    
    private boolean isDuplicateContent(Post post) {
        // Buscar posts similares en las últimas 24 horas
        LocalDateTime since = LocalDateTime.now().minusHours(24);
        return postRepository.findByUserIdAndCreatedAtAfter(post.getAuthorId(), since)
                .stream()
                .anyMatch(existingPost -> 
                    !existingPost.getId().equals(post.getId()) && 
                    existingPost.getContent().equals(post.getContent()));
    }
    
    private boolean isSpamByFrequency(String userId) {
        LocalDateTime since = LocalDateTime.now().minusHours(1);
        long recentPosts = postRepository.countByUserIdAndCreatedAtAfter(userId, since);
        return recentPosts > 10; // Más de 10 posts por hora es sospechoso
    }
    
    private boolean hasSuspiciousEngagement(Post post) {
        long hoursOld = java.time.Duration.between(post.getCreatedAt(), LocalDateTime.now()).toHours();
        if (hoursOld <= 0) return false;
        
        double engagementVelocity = (double)(post.getLikesCount() + post.getCommentsCount()) / hoursOld;
        return engagementVelocity > 1000; // Más de 1000 interacciones por hora es sospechoso
    }
    
    private List<String> findSimilarUsers(String userId, Set<String> userInteractedPosts) {
        // Encontrar usuarios que han interactuado con posts similares
        Map<String, Integer> userSimilarityScore = new HashMap<>();
        
        for (String postId : userInteractedPosts) {
            Long postIdLong = Long.parseLong(postId);
            List<String> usersWhoInteracted = reactionRepository.findByPostId(postIdLong)
                    .stream()
                    .map(reaction -> reaction.getUserId().toString())
                    .filter(uid -> !uid.equals(userId))
                    .collect(Collectors.toList());
            
            for (String similarUserId : usersWhoInteracted) {
                userSimilarityScore.merge(similarUserId, 1, (a, b) -> a + b);
            }
        }
        
        return userSimilarityScore.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(20)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
    
    private Page<Post> convertToPage(List<Post> posts, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), posts.size());
        List<Post> pageContent = posts.subList(start, end);
        
        return new org.springframework.data.domain.PageImpl<>(pageContent, pageable, posts.size());
    }

    // Clase interna para resultados de moderación
    public static class ContentModerationResult {
        private final Map<String, String> flags = new HashMap<>();
        private boolean requiresReview = false;
        
        public void addFlag(String type, String description) {
            flags.put(type, description);
            requiresReview = true;
        }
        
        public Map<String, String> getFlags() { return flags; }
        public boolean requiresReview() { return requiresReview; }
        public boolean isClean() { return flags.isEmpty(); }
    }
}