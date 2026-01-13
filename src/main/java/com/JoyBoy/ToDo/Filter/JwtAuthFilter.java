package com.JoyBoy.ToDo.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.http.Cookie;


import com.JoyBoy.ToDo.service.*;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final static String JWT_COOKIE_NAME ="JWT";

    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Autowired
    public JwtAuthFilter(CustomUserDetailsService userDetailsService, JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        
        String path = request.getRequestURI();
        System.out.println("=== JWT Filter - Path: " + path);
        
        // Skip authentication for public endpoints
        if (path.startsWith("/api/auth/") 
            || path.startsWith("/auth/") 
            || path.equals("/error")
            || path.startsWith("/login")) {
            System.out.println("=== Skipping JWT validation for public endpoint");
            filterChain.doFilter(request, response);
            return;
        }

        
        String token = extractToken(request);
        String username = null;

        if(token !=null){
            try {
                username = jwtService.extractUsername(token);
                System.out.println("=== Username extracted: " + username);
            } catch (Exception e) {
                System.err.println("=== Error extracting username: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("=== No Bearer token found in request");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                System.out.println("=== User loaded: " + userDetails.getUsername());
                
                if (jwtService.validateToken(token, userDetails)) {
                    System.out.println("=== Token is valid, setting authentication");
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    System.out.println("=== Token validation failed");
                }
            } catch (Exception e) {
                System.err.println("=== Error during authentication: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        System.out.println("=== Authorization Header: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("=== Token extracted: " + token.substring(0, Math.min(20, token.length())) + "...");
            return token;
        }

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (JWT_COOKIE_NAME.equals(cookie.getName())) {
                    System.out.println("=== JWT found in cookie");
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}