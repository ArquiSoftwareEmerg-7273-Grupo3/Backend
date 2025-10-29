package com.drawnet.artcollab.chatservice.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Entity
@Table(name = "chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_usuario_1", nullable = false)
    private Long usuario1id;

    @Column(name = "id_usuario_2", nullable = false)
    private Long usuario2id;

    @Column(nullable = false)
    private Boolean estado;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mensaje> mensajes = new ArrayList<>();

    public Chat() {}

    public Chat(Long usuario1id, Long usuario2id) {
        this.usuario1id = usuario1id;
        this.usuario2id = usuario2id;
        this.estado = true;
    }

    public void agregarMensaje(Long remitenteId, String texto) {
        if (!estado) {
            throw new IllegalStateException("No se puede enviar mensajes: el chat está inactivo.");
        }
        this.mensajes.add(new Mensaje(this, remitenteId, texto));
    }

    public void desactivar() {
        this.estado = false;
    }

    public boolean isActivo() {
        return estado;
    }

    public List<Mensaje> getMensajes() {
        return Collections.unmodifiableList(mensajes);
    }


    public Long getUsuario1Id() {
        return usuario1id;
    }

    public Long getUsuario2Id() {
        return usuario2id;
    }

    public Long getId() {
        return id;
    }

}
