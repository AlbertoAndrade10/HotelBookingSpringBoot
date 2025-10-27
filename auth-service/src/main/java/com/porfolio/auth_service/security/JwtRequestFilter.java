package com.porfolio.auth_service.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; // Importar SLF4J
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class); // Logger

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public JwtRequestFilter(UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    private static final List<String> WHITE_LIST = Arrays.asList(
        "/auth/register",
        "/auth/login"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        logger.info("JwtRequestFilter: Procesando petición a -> {}", requestURI); // Log URI

        if (WHITE_LIST.stream().anyMatch(uri -> requestURI.startsWith(uri))) {
            logger.info("JwtRequestFilter: URI {} está en la lista blanca. Continuando sin procesar token.", requestURI);
            chain.doFilter(request, response);
            return;
        }

        logger.info("JwtRequestFilter: URI {} NO está en la lista blanca. Procesando token...", requestURI);

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtUtil.getUsernameFromToken(jwtToken);
                logger.info("JwtRequestFilter: Token JWT válido encontrado. Usuario: {}", username);
            } catch (IllegalArgumentException e) {
                logger.error("JwtRequestFilter: No se puede obtener el JWT Token", e);
            } catch (ExpiredJwtException e) {
                logger.error("JwtRequestFilter: JWT Token ha expirado", e);
            }
        } else {
            logger.warn("JwtRequestFilter: No se encontró el encabezado de Autorización o no comienza con Bearer");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.info("JwtRequestFilter: Usuario en token es distinto de null y no hay autenticación en contexto. Cargando detalles...");

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("JwtRequestFilter: Autenticación establecida en contexto para usuario: {}", username);
            } else {
                logger.warn("JwtRequestFilter: Token JWT no es válido para el usuario: {}", username);
            }
        } else {
            logger.debug("JwtRequestFilter: Usuario es null o ya hay autenticación en contexto.");
        }

        chain.doFilter(request, response);
    }
}