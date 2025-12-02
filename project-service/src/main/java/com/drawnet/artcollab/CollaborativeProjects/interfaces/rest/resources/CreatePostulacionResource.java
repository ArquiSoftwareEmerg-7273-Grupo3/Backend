package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources;

import java.util.Date;
import java.util.List;
import java.util.Map;

public record CreatePostulacionResource(
    Date fecha,
    String coverLetter,
    String estimatedTime,
    Double proposedBudget,
    List<String> portfolioLinks,
    Map<String, String> answers,
    Boolean isPriority
) {
}
