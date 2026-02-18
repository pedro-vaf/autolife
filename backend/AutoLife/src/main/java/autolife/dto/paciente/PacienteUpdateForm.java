package autolife.dto.paciente;

import autolife.dto.endereco.EnderecoUpdateForm;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de atualização do registro do paciente")
public record PacienteUpdateForm(
        @Schema(description = "Novo nome. null ou string vazia serão ignorados", example = "João Santos")
        String nome,

        @Schema(description = "Novo telefone. null ou string vazia serão ignorados", example = "7198888888")
        String telefone,

        @Schema(description = "Novo endereço. null ou string vazia serão ignorados")
        EnderecoUpdateForm endereco) {
}