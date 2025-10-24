package com.drawnet.artcollab.iam.interfaces.rest.resources;

public record AuthenticatedUserResource(Long id, String username, String token, String role) {
}
