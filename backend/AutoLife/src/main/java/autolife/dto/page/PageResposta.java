package autolife.dto.page;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/* Essa classe foi adicionada para permitir expor o schema via OpenAPI */
@Schema(name = "PageResposta")
public record PageResposta<T>(
        @Schema(description = "Lista de itens da página")
        List<T> conteudo,

        @Schema(description = "Número da página atual (início em 0)")
        int number,

        @Schema(description = "Quantidade de elementos por página")
        int quantidade,

        @Schema(description = "Total de elementos")
        long totalElementos,

        @Schema(description = "Total de páginas")
        int totalPaginas,

        @Schema(description = "Primeira página")
        boolean primeiro,

        @Schema(description = "Última página")
        boolean ultimo
){}