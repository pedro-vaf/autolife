package autolife.dto.endereco;

import autolife.entities.Endereco;
import autolife.entities.enums.UnidadeFederativa;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados do endereço")
public record EnderecoDTO(
        @Schema(description = "Logradouro", example = "Praça Lauro de Freitas")
        String logradouro,

        @Schema(description = "Número do edifício", example = "105")
        String numero,

        @Schema(description = "Complemento para identificação do edifício", example = "Casa")
        String complemento,

        @Schema(description = "Nome do Bairro", example = "Centro")
        String bairro,

        @Schema(description = "Nome da cidade", example = "Pojuca")
        String cidade,

        @Schema(description = "Número do CEP", example = "48120970")
        String cep, UnidadeFederativa uf) {

        public EnderecoDTO(Endereco endereco) {
            this(endereco.getLogradouro(), endereco.getNumero(), endereco.getComplemento(), endereco.getBairro(),
                    endereco.getCidade(), endereco.getCep(), endereco.getUf()
            );
        }
}