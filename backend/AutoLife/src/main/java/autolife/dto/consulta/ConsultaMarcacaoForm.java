package autolife.dto.consulta;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "Dados para marcação de consulta")
public record ConsultaMarcacaoForm(
        @Schema(description = "Id do paciente", example = "1")
        @NotNull(message = "O Id do paciente deve ser fornecido")
        Long pacienteId,

        @Schema(description = "Id do médico", example = "1")
        Long medicoId,

        @Schema(description = "Data e hora do início da consulta no formato yyyy-mm-ddThh:mm",
                example = "2026-02-01T11:56")
        @NotNull(message = "Data e hora de início da consulta devem ser fornecidos")
        LocalDateTime inicioConsulta) {
}