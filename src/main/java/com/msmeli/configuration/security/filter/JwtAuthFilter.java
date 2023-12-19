package com.msmeli.configuration.security.filter;

import com.msmeli.configuration.security.service.JwtService;
import com.msmeli.configuration.security.service.UserEntityUserDetailsService;
import com.msmeli.exception.SecurityException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserEntityUserDetailsService userEntityUserDetailsService;

    private final SecurityException securityException;

    public JwtAuthFilter(JwtService jwtService, UserEntityUserDetailsService userEntityUserDetailsService, SecurityException securityException) {
        this.jwtService = jwtService;
        this.userEntityUserDetailsService = userEntityUserDetailsService;
        this.securityException = securityException;
    }
//TODO FALTA RESPOSE CUANDO NO SE ENVIA TOKEN DEVUELVE 403
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, JwtException  {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        try {
            validateAuthHeader(authHeader);
                token = authHeader.substring(7);
                username = jwtService.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userEntityUserDetailsService.loadUserByUsername(username);
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }catch (Exception ex) {
            throw new SecurityException("Error en la Autenticacion: " + ex.getMessage(),"JWT", 000);
        }

        filterChain.doFilter(request, response);
    }

    private void validateAuthHeader(String authHeader){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new SecurityException("El encabezado de autenticación es nulo o no tiene el formato esperado","JWT TOKEN",000);
        }
    }
}
