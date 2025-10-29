package com.drawnet.artcollab.iam.interfaces.rest.transform;


import com.drawnet.artcollab.iam.domain.model.entities.Role;
import com.drawnet.artcollab.iam.interfaces.rest.resources.RoleResource;

public class RoleResourceFromEntityAssembler {
    public static RoleResource toResourceFromEntity(Role entity) {
        return new RoleResource(entity.getId(), entity.getStringName());
    }
}
