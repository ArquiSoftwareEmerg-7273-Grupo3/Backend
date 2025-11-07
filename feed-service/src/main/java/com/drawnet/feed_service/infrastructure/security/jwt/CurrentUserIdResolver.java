package com.drawnet.feed_service.infrastructure.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrentUserIdResolver implements HandlerMethodArgumentResolver {

    private final JwtUtil jwtUtil;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUserId.class) 
                && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                   ModelAndViewContainer mavContainer,
                                   NativeWebRequest webRequest,
                                   WebDataBinderFactory binderFactory) {
        
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith(BEARER_PREFIX)) {
            String token = authHeader.substring(BEARER_PREFIX.length());
            
            // Primero intenta extraer userId del token
            Long userId = jwtUtil.extractUserId(token);
            
            if (userId != null) {
                log.debug("Extracted userId from JWT: {}", userId);
                return userId;
            }
            
            // Si no hay userId en el token, intenta obtener el username y hacer lookup
            String username = jwtUtil.extractUsername(token);
            if (username != null) {
                log.warn("Token does not contain userId claim, only username: {}", username);
                // Aquí podrías hacer un lookup al auth-service si es necesario
            }
        }

        // Fallback: intenta obtener de header X-User-Id (compatibilidad)
        String userIdHeader = request.getHeader("X-User-Id");
        if (StringUtils.hasText(userIdHeader)) {
            log.debug("Using X-User-Id header as fallback: {}", userIdHeader);
            return Long.valueOf(userIdHeader);
        }

        log.error("Could not extract userId from request. Make sure you're sending a valid JWT token with 'userId' claim in the Authorization header.");
        throw new IllegalArgumentException("User ID not found in request. Please login again to get a valid token with userId claim.");
    }
}
