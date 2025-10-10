package com.ifsc.tarefas.auth;

import java.io.IOException;
import java.util.Set;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filtro simples que valida o token vindo do cookie "AUTH_TOKEN" ou do header
 * Authorization: Bearer <token>.
 *
 * - Libera rotas públicas (login, estáticos, h2-console, etc.)
 * - Exige token válido nas rotas protegidas (por padrão, tudo em /templates e /tarefas/*)
 */
@Component
public class AuthFilter extends OncePerRequestFilter {

    private final AuthRepository authRepository;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // Endpoints públicos (sem autenticação)
    private static final Set<String> PUBLIC_PATTERNS = Set.of(
        "/login", 
        "/login/**",
        "/register",
        "/register/**",
        "/style.css",
        "/css/**",
        "/js/**",
        "/images/**",
        "/webjars/**",
        "/h2-console/**",
        "/",
        "/error"
    );

    private final JwtUtil jwtUtil;

    public AuthFilter(AuthRepository authRepository, JwtUtil jwtUtil) {
        this.authRepository = authRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // Se bate com algum padrão público, não filtra
        for (String pattern : PUBLIC_PATTERNS) {
            if (pathMatcher.match(pattern, path)) {
                return true;
            }
        }
        // Permite métodos OPTIONS (CORS preflight)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Tenta extrair token do cookie
        String token = extractTokenFromCookie(request, "AUTH_TOKEN");

        // Se não veio cookie, tenta no header Authorization: Bearer <token>
        if (token == null || token.isBlank()) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
        }

        var maybeUser = authRepository.validate(token);
        if (maybeUser.isEmpty()) {
            // Se a requisição espera HTML, redireciona para login com retorno
            String accept = request.getHeader("Accept");
            if (accept != null && accept.contains("text/html")) {
                String redirectTo = request.getRequestURI();
                response.sendRedirect("/login?redirect=" + redirectTo);
                return;
            }
            // Caso contrário, responde 401 JSON simples
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Unauthorized\"}");
            return;
        }
        // Disponibiliza o usuário autenticado e sua role para os controllers
        String username = maybeUser.get();
        request.setAttribute("AUTH_USER", username);
        try {
            String role = jwtUtil.getRole(token);
            request.setAttribute("AUTH_ROLE", role != null ? role : authRepository.getRoleByUsername(username));
        } catch (Exception e) {
            request.setAttribute("AUTH_ROLE", authRepository.getRoleByUsername(username));
        }
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie c : cookies) {
            if (name.equals(c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }
}


