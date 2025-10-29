package com.drawnet.artcollab.iam.application.internal.queryservices;


import com.drawnet.artcollab.iam.domain.model.entities.Role;
import com.drawnet.artcollab.iam.domain.model.queries.GetAllRolesQuery;
import com.drawnet.artcollab.iam.domain.model.queries.GetRoleByIdQuery;
import com.drawnet.artcollab.iam.domain.services.RoleQueryService;
import com.drawnet.artcollab.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleQueryServiceImpl implements RoleQueryService {
    private final RoleRepository roleRepository;

    public RoleQueryServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> handle(GetAllRolesQuery query) {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> handle(GetRoleByIdQuery query) {
        return roleRepository.findById(query.roleId());
    }
}
