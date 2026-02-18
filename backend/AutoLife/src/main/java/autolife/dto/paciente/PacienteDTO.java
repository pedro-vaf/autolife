package autolife.dto.paciente;

import autolife.entities.Paciente;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados do paciente")
public record PacienteDTO(
        @Schema(description = "id do médico", example = "1")
        Long id,

        @Schema(description = "Nome do paciente", example = "João Santos")
        String nome,

        @Schema(description = "Endereço de e-mail do paciente", example = "Maria da Silva")
        String email,

        @Schema(description = "Número de CPF do paciente", example = "37597099070")
        String cpf) {

    public PacienteDTO(Paciente paciente) {
        this(paciente.getId(), paciente.getNome(), paciente.getEmail(), paciente.getCpf());
    }
}