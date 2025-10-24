package com.drawnet.artcollab.iam.interfaces.rest.transform;


import com.drawnet.artcollab.iam.domain.model.commands.SignInCommand;
import com.drawnet.artcollab.iam.interfaces.rest.resources.SignInResource;

public class SignInCommandFromResourceAssembler {
    public static SignInCommand toCommandFromResource(SignInResource resource) {
        return new SignInCommand(resource.username(), resource.password());
    }
}
