package autolife.dto.usuario;

import autolife.entities.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados públicos do usuário")
public record UsuarioDTO(
        @Schema(example = "pedroadmin")
        String login) {
    public UsuarioDTO(Usuario usuario) {
        this(usuario.getLogin());
    }
}