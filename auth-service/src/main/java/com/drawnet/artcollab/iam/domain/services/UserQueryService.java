package com.drawnet.artcollab.iam.domain.services;

import com.drawnet.artcollab.iam.domain.model.aggregates.User;
import com.drawnet.artcollab.iam.domain.model.queries.GetAllUsersQuery;
import com.drawnet.artcollab.iam.domain.model.queries.GetUserByIdAndRolQuery;
import com.drawnet.artcollab.iam.domain.model.queries.GetUserByIdQuery;
import com.drawnet.artcollab.iam.domain.model.queries.GetUserByIdWithProfilesQuery;
import com.drawnet.artcollab.iam.domain.model.queries.GetUserByUsernameQuery;

import java.util.List;
import java.util.Optional;

public interface UserQueryService {
    List<User> handle(GetAllUsersQuery query);
    Optional<User> handle(GetUserByIdQuery query);
    Optional<User> handle(GetUserByUsernameQuery query);
    Optional<User> handle(GetUserByIdAndRolQuery query);
    Optional<User> handle(GetUserByIdWithProfilesQuery query);
}
