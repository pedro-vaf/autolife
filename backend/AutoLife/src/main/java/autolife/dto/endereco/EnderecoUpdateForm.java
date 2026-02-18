package autolife.dto.endereco;

import autolife.entities.enums.UnidadeFederativa;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de atualização de endereço")
public record EnderecoUpdateForm(
        @Schema(description = "Novo logradouro. null ou string vazia serão ignorados")
        String logradouro,

        @Schema(description = "Novo número. null ou string vazia serão ignorados")
        String numero,

        @Schema(description = "Novo complemento. null ou string vazia serão ignorados")
        String complemento,

        @Schema(description = "Novo bairro. null ou string vazia serão ignorados")
        String bairro,

        @Schema(description = "Nova cidade. null ou string vazia serão ignorados")
        String cidade,

        @Schema(description = "Novo sigla de UF. null ou string vazia serão ignorados")
        UnidadeFederativa uf, String cep) {
}
