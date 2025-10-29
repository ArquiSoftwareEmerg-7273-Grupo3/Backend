package com.drawnet.artcollab.chatservice.interfaces.rest.transform;

import com.drawnet.artcollab.chatservice.domain.model.aggregates.Chat;
import com.drawnet.artcollab.chatservice.interfaces.rest.resources.ChatResource;

public class ChatResourceFromEntityAssembler {
    public static ChatResource toResourceFromEntity(Chat chat) {
        return new ChatResource(
                chat.getId(),
                chat.getUsuario1Id(),
                chat.getUsuario2Id(),
                chat.isActivo()
        );
    }
}
