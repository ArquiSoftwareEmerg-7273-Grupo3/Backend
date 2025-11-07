# Autenticación JWT Automática en Feed Service

## Resumen

Se ha implementado un sistema de extracción automática del `userId` desde el token JWT, eliminando la necesidad de pasar manualmente el header `X-User-Id` en cada petición.

## Componentes Implementados

### 1. **JwtUtil** (`infrastructure/security/jwt/JwtUtil.java`)
Utilidad para extraer información del token JWT:
- `extractUsername(token)`: Extrae el username del subject
- `extractUserId(token)`: Extrae el userId del claim personalizado
- `validateToken(token)`: Valida la firma y expiración del token

### 2. **@CurrentUserId** (`infrastructure/security/jwt/CurrentUserId.java`)
Anotación personalizada para inyectar automáticamente el userId en los parámetros del controlador.

### 3. **CurrentUserIdResolver** (`infrastructure/security/jwt/CurrentUserIdResolver.java`)
Resolver que intercepta las peticiones y extrae el userId del token JWT automáticamente:
- Lee el header `Authorization: Bearer <token>`
- Extrae el userId del claim `userId` en el token
- Fallback al header `X-User-Id` para compatibilidad con versiones anteriores
- Lanza excepción si no se encuentra el userId

### 4. **WebMvcConfig** (`infrastructure/config/WebMvcConfig.java`)
Configuración que registra el resolver personalizado en Spring MVC.

## Uso en Controladores

### Antes:
```java
@PostMapping
public ResponseEntity<Long> createPost(
        @Valid @RequestBody CreatePostResource resource,
        @RequestHeader("X-User-Id") Long userId) {
    // ...
}
```

### Ahora:
```java
@PostMapping
public ResponseEntity<Long> createPost(
        @Valid @RequestBody CreatePostResource resource,
        @CurrentUserId Long userId) {
    // ...
}
```

## Controladores Actualizados

Todos los endpoints que requieren autenticación han sido actualizados:
- ✅ `PostController` - Crear, actualizar, eliminar posts
- ✅ `CommentController` - Crear, eliminar comentarios
- ✅ `ReactionController` - Agregar, remover, toggle reacciones
- ✅ `RepostController` - Crear, remover, toggle reposts

## Cambios en Auth Service

El servicio de autenticación ahora incluye el `userId` en el token JWT:

```java
// TokenServiceImpl.java
private String buildTokenWithDefaultParameters(String username, Long userId) {
    return Jwts.builder()
            .subject(username)
            .claim("userId", userId)  // ← Nuevo claim
            .issuedAt(issuedAt)
            .expiration(expiration)
            .signWith(key)
            .compact();
}
```

## Configuración

Asegúrate de tener la misma clave secreta en ambos servicios:

**auth-service/application.properties:**
```properties
authorization.jwt.secret=ThisIsASecretKeyForJWTTokenGenerationAndValidationPleaseChangeInProduction
authorization.jwt.expiration.days=7
```

**feed-service/application.properties:**
```properties
authorization.jwt.secret=ThisIsASecretKeyForJWTTokenGenerationAndValidationPleaseChangeInProduction
authorization.jwt.expiration.days=7
```

⚠️ **IMPORTANTE**: Cambia el secret en producción por uno seguro y único.

## Ejemplo de Petición

```bash
# Login para obtener token
curl -X POST http://localhost:8080/api/v1/auth/sign-in \
  -H "Content-Type: application/json" \
  -d '{"username": "usuario", "password": "password"}'

# Respuesta:
# {"token": "eyJhbGciOiJIUzI1NiJ9..."}

# Crear post usando el token
curl -X POST http://localhost:8087/api/v1/posts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
  -d '{"content": "Mi primer post", "tags": ["arte", "dibujo"]}'
```

## Ventajas

1. **Seguridad mejorada**: El userId viene del token firmado, no de un header manipulable
2. **Menos código**: No necesitas pasar manualmente el userId en cada petición
3. **Consistencia**: Todos los endpoints usan el mismo mecanismo
4. **Compatibilidad**: Mantiene soporte para `X-User-Id` como fallback

## Manejo de Errores

Si el token no contiene userId o es inválido:
```json
{
  "error": "User ID not found in request",
  "status": 400
}
```

## Próximos Pasos

1. Implementar el mismo patrón en otros microservicios (chat-service, portafolio-service, etc.)
2. Agregar más claims al token si es necesario (roles, permisos, etc.)
3. Implementar refresh tokens para mejorar la seguridad
4. Considerar usar Spring Security para una solución más robusta
