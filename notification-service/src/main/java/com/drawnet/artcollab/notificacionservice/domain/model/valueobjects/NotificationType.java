package com.drawnet.artcollab.notificacionservice.domain.model.valueobjects;

/**
 * Tipos de notificaciones en el sistema
 */
public enum NotificationType {
    // Notificaciones de posts y comentarios
    NEW_COMMENT("Nuevo comentario en tu publicación"),
    NEW_LIKE("Le gustó tu publicación"),
    NEW_SHARE("Compartió tu publicación"),
    
    // Notificaciones de proyectos
    PROJECT_CREATED("Nuevo proyecto creado"),
    PROJECT_STATUS_CHANGED("Estado del proyecto actualizado"),
    PROJECT_APPLICATION("Nueva postulación a tu proyecto"),
    APPLICATION_ACCEPTED("Tu postulación fue aceptada"),
    APPLICATION_REJECTED("Tu postulación fue rechazada"),
    
    // Notificaciones de colaboración
    COLLABORATION_INVITE("Invitación a colaborar"),
    COLLABORATION_ACCEPTED("Colaboración aceptada"),
    COLLABORATION_REJECTED("Colaboración rechazada"),
    
    // Notificaciones de suscripción
    SUBSCRIPTION_ACTIVATED("Suscripción activada"),
    SUBSCRIPTION_EXPIRED("Suscripción expirada"),
    SUBSCRIPTION_RENEWED("Suscripción renovada"),
    PAYMENT_SUCCESSFUL("Pago procesado exitosamente"),
    PAYMENT_FAILED("Error en el procesamiento del pago"),
    
    // Notificaciones de seguimiento
    NEW_FOLLOWER("Nuevo seguidor"),
    
    // Notificaciones del sistema
    SYSTEM_ANNOUNCEMENT("Anuncio del sistema"),
    ACCOUNT_VERIFIED("Cuenta verificada"),
    PASSWORD_CHANGED("Contraseña cambiada");
    
    private final String defaultMessage;
    
    NotificationType(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }
    
    public String getDefaultMessage() {
        return defaultMessage;
    }
}
