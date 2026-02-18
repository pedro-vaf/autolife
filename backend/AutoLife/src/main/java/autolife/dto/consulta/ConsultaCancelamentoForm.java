package autolife.dto.consulta;

import autolife.entities.enums.ConsultaCancelamentoMotivo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para cancelamento de marcação de consulta")
public record ConsultaCancelamentoForm(
        @Schema(description = "Enumeração de motivo do cancelamento da consulta. Valores possíveis são: " +
                "'DESISTENCIA_PACIENTE', 'CANCELAMENTO_MEDICO', e 'OUTROS'",
                example = "DESISTENCIA_PACIENTE")
        @NotNull(message = "Deve ser fornecido um motivo de cancelamento")
        ConsultaCancelamentoMotivo motivoCancelamento) {
}