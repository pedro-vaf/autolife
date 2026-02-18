package autolife.controllers;

import autolife.client.NotificacaoClient;
import autolife.dto.agendamento.MedicoRequest;
import autolife.dto.medico.MedicoCreateForm;
import autolife.dto.medico.MedicoDTO;
import autolife.dto.medico.MedicoUpdateForm;
import autolife.dto.page.PageResposta;
import autolife.exception.ConsultaError;
import autolife.service.MedicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicos")
public class MedicoController {
    private final MedicoService medicoService;
    private final NotificacaoClient notificacaoClient;

    public MedicoController(MedicoService service, NotificacaoClient notificacaoClient) { this.medicoService = service;
        this.notificacaoClient = notificacaoClient;
    }

    @GetMapping
    @Operation(
            summary = "Obter médicos",
            description = "Obtém uma lista paginada de médicos ativos no sistema ordenada pelo nome. " +
                    "Por padrão, retorna-se 10 itens."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de médicos ativos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PageResposta.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro de validação ou regra de negócio",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConsultaError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConsultaError.class)
                    )
            )
    })
    public Page<MedicoDTO> getMedicos(
            @PageableDefault(
                    size = 10,
                    sort = "nome",
                    direction = Sort.Direction.ASC
            ) Pageable pageable) {
        return this.medicoService.getMedicosAtivos(pageable);
    }

    @PostMapping
    @Operation(
            summary = "Registrar novo médico",
            description = "Registra um novo médico no sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Dados do médico registrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MedicoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro de validação ou regra de negócio",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConsultaError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConsultaError.class)
                    )
            )
    })
    public ResponseEntity<MedicoDTO> createMedico(@RequestBody @Valid MedicoCreateForm medicoForm) {
        var doctorDTO = this.medicoService.saveMedico(medicoForm);

        MedicoRequest request = MedicoRequest.builder()
                .medicoNome(medicoForm.nome())
                .especialidade(String.valueOf(medicoForm.especialidade()))
                .medicoEmail(medicoForm.email())
                .build();

        notificacaoClient.criarCadastroMedico(request);
        return ResponseEntity.ok(doctorDTO);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar dados de um médico",
            description = "Registra um novo médico no sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Dados atualizados do médico",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MedicoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro de validação ou regra de negócio",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConsultaError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConsultaError.class)
                    )
            )
    })
    public ResponseEntity<MedicoDTO> updateMedico(
            @Parameter(
                    description = "Id do médico",
                    example = "1",
                    required = true
            )
            @PathVariable Long id,

            @RequestBody MedicoUpdateForm dados) {
        MedicoDTO medicoAtualizado = this.medicoService.updateMedico(id, dados);
        return ResponseEntity.ok(medicoAtualizado);
    }

    @PostMapping("/{id}")
    @Operation(
            summary = "Remover médico",
            description = "Torna médico inativo no sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Dados do médico removido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MedicoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro de validação ou regra de negócio",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConsultaError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConsultaError.class)
                    )
            )
    })
    public ResponseEntity<MedicoDTO> deleteMedico(
            @Parameter(
                    description = "Id do médico",
                    example = "1",
                    required = true
            )
            @PathVariable Long id) {
        MedicoDTO medicoExcluido = this.medicoService.deleteMedico(id);
        return ResponseEntity.ok(medicoExcluido);
    }
}
