package autolife.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteEmailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String pacienteNome;
    private String cpf;
    private String pacienteEmail;
}
