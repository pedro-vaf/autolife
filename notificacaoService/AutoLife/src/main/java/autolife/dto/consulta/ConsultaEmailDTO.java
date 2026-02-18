package autolife.dto.consulta;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaEmailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String pacienteNome;
    private String pacienteEmail;
    private String medicoNome;
    private String especialidade;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataConsulta;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaConsulta;

    private String local;
    private String consultaId;
}