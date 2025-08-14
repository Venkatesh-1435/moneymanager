package com.money.manager.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.money.manager.util.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	@Autowired
    private final UserDetailsService detailsService;

    @Autowired
    private final JwtUtils jwtUtills; // ✅ Inject JwtUtills bean instead of using static

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                String email = jwtUtills.getClaims(token).getSubject();  // ✅ use bean method

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails details = detailsService.loadUserByUsername(email);

                    if (jwtUtills.validateToken(token, details.getUsername())) {  // ✅ use bean method
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }

            } catch (Exception e) {
                System.out.println("JWT parsing or validation failed: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
