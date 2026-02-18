package autolife.dto.medico;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicoResponse {

    private Long id;
    private String medicoNome;
    private String especialidade;
    private String medicoEmail;
}