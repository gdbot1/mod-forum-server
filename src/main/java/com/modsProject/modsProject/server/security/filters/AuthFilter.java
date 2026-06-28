package com.modsProject.modsProject.server.security.filters;

import com.modsProject.modsProject.server.dto.AuthDto;
import com.modsProject.modsProject.server.security.services.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class AuthFilter extends OncePerRequestFilter {
    @Autowired
    private AuthService authService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/login")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.startsWith("/login/anonymous")
                || path.startsWith("/test")
                || path.startsWith("/register");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();

        String jwtToken = "";
        String sessionKey = "";

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                switch (cookie.getName()) {
                    case "jwt_token" -> jwtToken = cookie.getValue();
                    case "session_key" -> sessionKey = cookie.getValue();
                }
            }
        }

        AuthDto auth = authService.authenticate(jwtToken, sessionKey);

        if (auth == null) {
            if (sessionKey == null || sessionKey.isEmpty()) {//Если сессия не была указана
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("-1", null, Collections.emptyList());

                SecurityContextHolder.getContext().setAuthentication(authentication);

                filterChain.doFilter(request, response);
            }
            else {
                response.sendRedirect("/login?expired");

            }
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(auth.getCreatorId().toString(), null, Collections.emptyList());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        if (auth.hasNewJwt()) {
            Cookie jwtCookie = new Cookie("jwt_token", auth.getNewJwtToken());
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(7 * 24 * 60 * 60);
            jwtCookie.setHttpOnly(false);

            response.addCookie(jwtCookie);
        }

        filterChain.doFilter(request, response);
    }
}
