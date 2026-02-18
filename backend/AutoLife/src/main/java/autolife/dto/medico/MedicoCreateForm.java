package autolife.dto.medico;

import autolife.dto.endereco.EnderecoCreateForm;
import autolife.entities.Medico;
import autolife.entities.enums.Especialidade;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para registro de novo médico no sistema")
public record MedicoCreateForm(
        @Schema(description = "Nome", example = "Maria da Silva")
        @NotBlank(message = "Nome deve ser fornecido")
        String nome,

        @Schema(description = "Endereço de e-mail", example = "maria@email.com")
        @NotBlank(message = "Endereço de e-mail deve ser fornecido")
        String email,

        @Schema(description = "Número de telefone", example = "71912345678")
        @NotBlank(message = "Número de telefone deve ser fornecido")
        String telefone,

        @Schema(description = "Número de cadastro no CRM", example = "123456")
        @NotBlank(message = "Número do CRM deve ser fornecido")
        String crm,

        @Schema(description = "Enumeração da especialidade médica. Os valores possíveis são: " +
                "'ORTOPEDIA', 'CARDIOLOGIA', 'GINECOLOGIA', e 'DERMATOLOGIA'",
                example = "ORTOPEDIA")
        @NotNull(message = "Especialidade médica deve ser fornecida")
        Especialidade especialidade,

        @Schema(description = "Informações do endereço do médico")
        @Valid
        @NotNull(message = "Informações de endereço devem ser fornecidas")
        EnderecoCreateForm endereco) {
        public MedicoCreateForm(Medico medico) {
            this(
                    medico.getNome(),
                    medico.getEmail(),
                    medico.getTelefone(),
                    medico.getCrm(),
                    medico.getEspecialidade(),
                    new EnderecoCreateForm(medico.getEndereco())
            );
        }

        public Medico toEntity() {
            return new Medico(
                    nome,
                    email,
                    telefone,
                    crm,
                    especialidade,
                    endereco.toEntity()
            );
        }
}