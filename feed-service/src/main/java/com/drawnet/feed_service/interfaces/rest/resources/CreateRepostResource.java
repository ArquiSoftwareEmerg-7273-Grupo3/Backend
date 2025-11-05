package com.drawnet.feed_service.interfaces.rest.resources;

import jakarta.validation.constraints.Size;

public record CreateRepostResource(
    @Size(max = 500, message = "Comment cannot exceed 500 characters")
    String comment
) {}