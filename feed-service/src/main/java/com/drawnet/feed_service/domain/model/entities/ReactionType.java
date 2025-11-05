package com.drawnet.feed_service.domain.model.entities;

public enum ReactionType {
    LIKE("👍"),
    DISLIKE("👎"), 
    LOVE("❤️"),
    LAUGH("😂"),
    ANGRY("😡"),
    SAD("😢");
    
    private final String emoji;
    
    ReactionType(String emoji) {
        this.emoji = emoji;
    }
    
    public String getEmoji() {
        return emoji;
    }
    
    public String getDisplayName() {
        return this.name().toLowerCase();
    }
}