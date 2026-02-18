package autolife.dto.consulta;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaRequest {

    @NotBlank(message = "Nome do paciente é obrigatório")
    private String pacienteNome;

    @NotBlank(message = "Email do paciente é obrigatório")
    @Email(message = "Email inválido")
    private String pacienteEmail;

    @NotBlank(message = "Nome do médico é obrigatório")
    private String medicoNome;

    @NotBlank(message = "Especialidade é obrigatória")
    private String especialidade;

    @NotNull(message = "Data da consulta é obrigatória")
    @Future(message = "Data deve ser no futuro")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataConsulta;

    @NotNull(message = "Horário da consulta é obrigatório")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaConsulta;

    @NotBlank(message = "Local é obrigatório")
    private String local;
}