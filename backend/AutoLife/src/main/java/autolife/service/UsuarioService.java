package autolife.service;

import autolife.dto.usuario.UsuarioCreateForm;
import autolife.dto.usuario.UsuarioDTO;
import autolife.entities.Usuario;
import autolife.exception.AutenticacaoException;
import autolife.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioDTO createUsuario(UsuarioCreateForm form) throws AutenticacaoException {
        if (this.usuarioRepository.findByLogin(form.login()) != null)
            throw new AutenticacaoException("Usuário já cadastrado no sistema");

        String senhaEncriptada = this.passwordEncoder.encode(form.senha());

        var usuario = new Usuario(form.login(), senhaEncriptada);
        this.usuarioRepository.save(usuario);
        return new UsuarioDTO(usuario);
    }
}