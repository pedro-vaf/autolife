package autolife.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import autolife.repositories.UsuarioRepository;
import autolife.service.JWTokenService;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final JWTokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    public SecurityFilter(JWTokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var token = recuperarToken(request);

        if (token != null) {
            var login = tokenService.getSubject(token);
            var usuario = usuarioRepository.findByLogin(login);

            if (usuario != null) {
                var auth = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var token = request.getHeader("Authorization");

        boolean tokenVazioOuNaoAutorizacao = token == null || !token.startsWith("Bearer ");
        if (tokenVazioOuNaoAutorizacao)
            return null;

        return token.replace("Bearer ", "");
    }
}