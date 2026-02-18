package autolife.dto.paciente;

import autolife.dto.endereco.EnderecoCreateForm;
import autolife.entities.Paciente;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados de registro de paciente")
public record PacienteCreateForm(
        @Schema(description = "Nome do paciente", example = "João Santos")
        @NotBlank(message = "Nome deve ser fornecido")
        String nome,

        @Schema(description = "Endereço de e-mail do paciente", example = "joao@email.com")
        @NotBlank(message = "Endereço de e-mail deve ser fornecido")
        String email,

        @Schema(description = "Número de telefone do paciente", example = "71988888888")
        @NotBlank(message = "Número de telefone deve ser fornecido")
        String telefone,

        @Schema(description = "Número de CPF do paciente", example = "37597099070")
        @NotBlank(message = "Número de CPF deve ser fornecido")
        String cpf,

        @Schema(description = "Informações de endereço do paciente")
        @Valid
        @NotNull(message = "Informações de endereço devem ser fornecidas")
        EnderecoCreateForm endereco) {

    public Paciente toEntity() {
        return new Paciente(nome, email, telefone, cpf, endereco.toEntity());
    }
}