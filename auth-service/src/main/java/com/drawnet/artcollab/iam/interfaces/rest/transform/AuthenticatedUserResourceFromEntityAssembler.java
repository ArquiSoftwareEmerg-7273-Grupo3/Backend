package com.drawnet.artcollab.iam.interfaces.rest.transform;

import com.drawnet.artcollab.iam.domain.model.aggregates.User;
import com.drawnet.artcollab.iam.interfaces.rest.resources.AuthenticatedUserResource;

public class AuthenticatedUserResourceFromEntityAssembler {
    public static AuthenticatedUserResource toResourceFromEntity(User user, String token) {
        return new AuthenticatedUserResource(user.getId(), user.getUsername(), token, user.getRole().getStringName());
    }
}
