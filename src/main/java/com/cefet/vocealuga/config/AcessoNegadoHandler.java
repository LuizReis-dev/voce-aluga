package com.cefet.vocealuga.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AcessoNegadoHandler  implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        // Redireciona para o login do admin se for tentativa de acessar /admin/**
        if (request.getRequestURI().startsWith("/admin")) {
            response.sendRedirect("/admin/login?denied");
        } else {
            // Redireciona genérico ou para página 403 customizada
            response.sendRedirect("/login?denied");
        }
    }
}
