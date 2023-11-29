package com.msmeli.configuration.security.filter;

import com.msmeli.configuration.security.service.JwtService;
import com.msmeli.configuration.security.service.UserEntityUserDetailsService;
import com.msmeli.exception.AppExceptionHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserEntityUserDetailsService userEntityUserDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final AppExceptionHandler appExceptionHandler;

    public JwtAuthFilter(JwtService jwtService, UserEntityUserDetailsService userEntityUserDetailsService, HandlerExceptionResolver handlerExceptionResolver, AppExceptionHandler appExceptionHandler) {
        this.jwtService = jwtService;
        this.userEntityUserDetailsService = userEntityUserDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.appExceptionHandler = appExceptionHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                username = jwtService.extractUsername(token);
            }
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userEntityUserDetailsService.loadUserByUsername(username);
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            ProblemDetail problemDetail = appExceptionHandler.handleSecurityException(ex);
            response.setStatus(problemDetail.getStatus());
            response.setContentType("application/problem+json");
            response.getWriter().write(problemDetail.toString());
            response.getWriter().flush();
            response.getWriter().close();
        }
    }
}
