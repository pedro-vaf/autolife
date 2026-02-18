package autolife.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para registro de novo usuário administrador")
public record UsuarioCreateForm(
        @Schema(description = "Login do novo usuário", example = "joseadmin")
        @NotNull(message = "Login não pode ser vazio")
        String login,

        @Schema(description = "Senha do novo usuário", example = "zezin123")
        @NotNull(message = "Senha não pode ser vazia")
        String senha){
}