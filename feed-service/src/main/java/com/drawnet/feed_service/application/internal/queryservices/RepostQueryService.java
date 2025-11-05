package com.drawnet.feed_service.application.internal.queryservices;

import com.drawnet.feed_service.domain.model.entities.Repost;
import com.drawnet.feed_service.infrastructure.persistence.jpa.repositories.RepostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RepostQueryService {

    private final RepostRepository repostRepository;

    public Page<Repost> getRepostsByUser(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repostRepository.findByUserIdAndActiveOrderByRepostDateDesc(userId, true, pageable);
    }

    public Page<Repost> getRepostsForPost(Long postId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repostRepository.findByOriginalPostIdAndActiveOrderByRepostDateDesc(postId, true, pageable);
    }

    public long countRepostsForPost(Long postId) {
        return repostRepository.countByOriginalPostIdAndActive(postId, true);
    }

    public boolean hasUserReposted(Long postId, Long userId) {
        return repostRepository.existsByOriginalPostIdAndUserIdAndActive(postId, userId, true);
    }

    public Optional<Repost> getUserRepost(Long postId, Long userId) {
        return repostRepository.findByOriginalPostIdAndUserIdAndActive(postId, userId, true);
    }

    public Page<Repost> getRecentReposts(int days, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return repostRepository.findRecentReposts(since, pageable);
    }

    public Page<Repost> getRepostsWithComment(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repostRepository.findRepostsWithComment(pageable);
    }
}