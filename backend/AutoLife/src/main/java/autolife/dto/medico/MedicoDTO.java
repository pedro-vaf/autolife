package autolife.dto.medico;

import autolife.entities.Medico;
import autolife.entities.enums.Especialidade;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados do médico")
public record MedicoDTO(
        @Schema(description = "id do médico", example = "1")
        Long id,

        @Schema(description = "Nome", example = "Maria da Silva")
        String nome,

        @Schema(description = "Endereço de e-mail", example = "maria@email.com")
        String email,

        @Schema(description = "Número de cadastro no CRM", example = "123456")
        String crm,

        @Schema(description = "Enumeração da especialidade médica")
        Especialidade especialidade) {

    public MedicoDTO(Medico medico) {
        this(medico.getId(), medico.getNome(), medico.getEmail(), medico.getCrm(), medico.getEspecialidade());
    }
}