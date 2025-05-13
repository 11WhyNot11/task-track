package com.arthur.tasktrackerapi.security.filter;

import com.arthur.tasktrackerapi.security.service.JwtService;
import com.arthur.tasktrackerapi.security.service.UserDetailsServiceImpl;
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
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        System.out.println("PATH: " + request.getServletPath());
        System.out.println("AUTH BEFORE: " + SecurityContextHolder.getContext().getAuthentication());

        final String path = request.getServletPath();

        if (path.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);
        String email = jwtService.extractUsername(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            var userDetails = userDetailsService.loadUserByUsername(email);

            if (jwtService.isTokenValid(token, userDetails)) {

                var user = ((UserDetailsServiceImpl) userDetailsService).loadDomainUserByUsername(email);

                System.out.println("EMAIL: " + user.getEmail());
                System.out.println("ROLE: " + user.getRole());
                System.out.println("AUTHORITIES: " + user.getAuthorities());
                System.out.println("AUTH: " + SecurityContextHolder.getContext().getAuthentication());
                System.out.println("PATH: " + request.getRequestURI());
                var authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );


                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("Setting auth: " + authentication);
            }
        }

        filterChain.doFilter(request, response);
    }


}

