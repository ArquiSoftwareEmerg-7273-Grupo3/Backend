package com.drawnet.artcollab.iam.interfaces.rest.transform;


import com.drawnet.artcollab.iam.domain.model.aggregates.User;
import com.drawnet.artcollab.iam.interfaces.rest.resources.UserResource;

public class UserResourceFromEntityAssembler {
    public static UserResource toResourceFromEntity(User entity) {
        return new UserResource(entity.getId(), entity.getUsername(), entity.getRole().getStringName());
    }
}
