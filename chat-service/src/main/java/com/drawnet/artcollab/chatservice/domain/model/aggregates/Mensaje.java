package com.drawnet.artcollab.chatservice.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes")
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chat", nullable = false)
    private Chat chat;

    @Column(name = "id_remitente", nullable = false)
    private Long remitenteId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String texto;

    public Mensaje() {}

    public Mensaje(Chat chat, Long remitenteId, String texto) {
        this.chat = chat;
        this.remitenteId = remitenteId;
        this.texto = texto;
    }

    public Long getId() {return id;}
    public Chat getChat() {return chat;}
    public Long getRemitenteId() {return remitenteId;}
    public String getTexto() {return texto;}

}
