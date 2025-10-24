package com.drawnet.artcollab.iam.domain.services;

import com.drawnet.artcollab.iam.domain.model.commands.SeedRolesCommand;

public interface RoleCommandService {
    void handle(SeedRolesCommand command);
}
