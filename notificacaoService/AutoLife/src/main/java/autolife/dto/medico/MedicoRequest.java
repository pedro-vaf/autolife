package autolife.dto.medico;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicoRequest {

    @NotBlank(message = "Nome do médico é obrigatório")
    private String medicoNome;

    @NotBlank(message = "Especialidade é obrigatória")
    private String especialidade;

    @NotBlank(message = "O e-mail do médico é obrigatório")
    private String medicoEmail;
}