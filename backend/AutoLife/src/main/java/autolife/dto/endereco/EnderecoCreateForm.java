package autolife.dto.endereco;

import autolife.entities.Endereco;
import autolife.entities.enums.UnidadeFederativa;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Informações para registro de endereço no sistema")
public record EnderecoCreateForm(
        @Schema(description = "Logradouro", example = "Praça Lauro de Freitas")
        @NotBlank(message = "Logradouro deve ser fornecido")
        String logradouro,

        @Schema(description = "Número do edifício", example = "105")
        String numero,

        @Schema(description = "Complemento para identificação do edifício", example = "Casa")
        String complemento,

        @Schema(description = "Nome do Bairro", example = "Centro")
        @NotBlank(message = "Nome do bairro deve ser fornecido")
        String bairro,

        @Schema(description = "Nome da cidade", example = "Pojuca")
        @NotBlank(message = "Nome da cidade deve ser fornecido")
        String cidade,

        @Schema(description = "Enumeração da Unidade Federativa. Utilizar as siglas oficiais", example = "BA")
        @NotNull(message = "Sigla da Unidade Federativa deve ser fornecida")
        UnidadeFederativa uf,

        @Schema(description = "Número do CEP", example = "48120970")
        @NotBlank(message = "Número do CEP deve ser fornecido")
        String cep) {
        public EnderecoCreateForm(Endereco endereco) {
            this(
                    endereco.getLogradouro(),
                    endereco.getNumero(),
                    endereco.getComplemento(),
                    endereco.getBairro(),
                    endereco.getCidade(),
                    endereco.getUf(),
                    endereco.getCep()
            );
        }

        public Endereco toEntity() {
            return new Endereco(
                    logradouro,
                    numero,
                    complemento,
                    bairro,
                    cidade,
                    uf,
                    cep
            );
        }
}