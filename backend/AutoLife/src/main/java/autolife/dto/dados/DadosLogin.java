package autolife.dto.dados;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados de login de usuário administrador na clínica")
public record DadosLogin(
        @Schema(description = "Login do usuário", example = "José Almeida")
        @NotBlank(message = "Não existe usuário com login vazio")
        String login,

        @Schema(description = "Senha do usuário", example = "senhasegura123")
        @NotBlank(message = "Não existe usuário com senha vazia")
        String senha) {
}