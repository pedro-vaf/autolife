package autolife.dto.paciente;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteRequest {

    @NotBlank(message = "Nome do paciente é obrigatório")
    private String pacienteNome;

    @NotBlank(message = "Especialidade é obrigatória")
    private String cpf;

    @NotBlank(message = "O e-mail do médico é obrigatório")
    private String pacienteEmail;
}