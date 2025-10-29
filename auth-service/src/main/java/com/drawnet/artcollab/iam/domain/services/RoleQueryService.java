package com.drawnet.artcollab.iam.domain.services;

import com.drawnet.artcollab.iam.domain.model.entities.Role;
import com.drawnet.artcollab.iam.domain.model.queries.GetAllRolesQuery;
import com.drawnet.artcollab.iam.domain.model.queries.GetRoleByIdQuery;

import java.util.List;
import java.util.Optional;

public interface RoleQueryService {
    List<Role> handle(GetAllRolesQuery query);
    Optional<Role> handle(GetRoleByIdQuery query);
}
