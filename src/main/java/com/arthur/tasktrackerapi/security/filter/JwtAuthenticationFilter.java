package com.arthur.tasktrackerapi.security.filter;

import com.arthur.tasktrackerapi.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String path = request.getServletPath();

        if (path.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }


        var authorization = request.getHeader("Authorization");

        if(authorization == null || !authorization.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        var token = authorization.substring(7);
        var email = jwtService.extractUsername(token);

        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = userDetailsService.loadUserByUsername(email);
            if(jwtService.isTokenValid(token, userDetails)){
                String role = jwtService.extractClaim(token, claims -> claims.get("role", String.class));
                var authorities = List.of(new SimpleGrantedAuthority(role));

                var auth =  new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);


    }
}

