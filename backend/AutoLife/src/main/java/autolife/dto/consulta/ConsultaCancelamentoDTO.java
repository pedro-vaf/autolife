package autolife.dto.consulta;

import autolife.entities.enums.ConsultaCancelamentoMotivo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Dados do cancelamento de marcação de consulta")
public record ConsultaCancelamentoDTO(
        @Schema(description = "Nome do paciente", example = "João Santos")
        String nomePaciente,

        @Schema(description = "Enumeração de motivo do cancelamento da consulta", example = "DESISTENCIA_PACIENTE")
        ConsultaCancelamentoMotivo motivoCancelamento,

        @Schema(description = "Data do cancelamento")
        LocalDateTime dataCancelamento) {
}
