package autolife.dto.consulta;

import autolife.entities.Consulta;
import io.swagger.v3.oas.annotations.media.Schema;
import autolife.dto.medico.MedicoDTO;
import autolife.dto.paciente.PacienteDTO;
import jdk.jshell.Snippet;

import java.time.LocalDateTime;

@Schema(description = "Dados da consulta")
public record ConsultaDTO(
        @Schema(description = "Identificação da consulta", example = "1")
        Long id,

        @Schema(description = "Nome do paciente", example = "João Santos")
        PacienteDTO paciente,

        @Schema(description = "Nome do médico", example = "Maria da Silva")
        MedicoDTO medico,

        @Schema(description = "Data e hora do início da consulta")
        LocalDateTime inicioConsulta,

        @Schema(description = "Data e hora do fim da consulta")
        LocalDateTime fimConsulta) {

    public ConsultaDTO(Consulta consulta) {
        this(consulta.getId(), new PacienteDTO(consulta.getPaciente()), new MedicoDTO(consulta.getMedico()), consulta.getInicio(),
                consulta.getFim());
    }
}