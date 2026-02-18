package autolife.controllers;

import autolife.client.NotificacaoClient;
import autolife.dto.agendamento.PacienteRequest;
import autolife.dto.medico.MedicoDTO;
import autolife.dto.paciente.PacienteCreateForm;
import autolife.dto.paciente.PacienteDTO;
import autolife.dto.paciente.PacienteUpdateForm;
import autolife.dto.page.PageResposta;
import autolife.exception.ConsultaError;
import autolife.service.PacienteService;
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
@RequestMapping("/pacientes")
public class PacienteController {
    private final PacienteService pacienteService;
    private final NotificacaoClient notificacaoClient;

    public  PacienteController(PacienteService pacienteService, NotificacaoClient notificacaoClient) {
        this.pacienteService = pacienteService;
        this.notificacaoClient = notificacaoClient;
    }

    @PostMapping
    @Operation(
            summary = "Registrar novo paciente",
            description = "Registra um novo paciente no sistema"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Paciente registrado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema =  @Schema(implementation = PacienteDTO.class)
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
            }
    )
    public ResponseEntity<PacienteDTO> createPaciente(@RequestBody @Valid PacienteCreateForm form) {
        var pacienteDTO = this.pacienteService.savePaciente(form);

        PacienteRequest request = PacienteRequest.builder()
                .pacienteNome(form.nome())
                .cpf(form.cpf())
                .pacienteEmail(form.email())
                .build();

        notificacaoClient.criarCadastroPaciente(request);
        return ResponseEntity.ok(pacienteDTO);
    }

    @GetMapping
    @Operation(
            summary = "Obtém pacientes",
            description = "Obtém uma lista paginada de pacientes ativos no sistema ordenada pelo nome. " +
                    "Por padrão, retorna-se 10 itens."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de pacientes ativos",
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
    public Page<PacienteDTO> getPacientes(
            @PageableDefault(
                    size = 10,
                    sort = "nome",
                    direction = Sort.Direction.ASC) Pageable pageable) {
        return this.pacienteService.getPacientesAtivos(pageable);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar dados de um paciente",
            description = "Registra um novo paciente no sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Dados atualizados do paciente",
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
    public ResponseEntity<PacienteDTO> updatePaciente(
            @Parameter(
                    description = "Id do paciente",
                    example = "1",
                    required = true
            )
            @PathVariable Long id,

            @RequestBody PacienteUpdateForm form) {
        var pacienteAtualizado = this.pacienteService.updatePaciente(id, form);
        return ResponseEntity.ok(pacienteAtualizado);
    }

    @PostMapping("/{id}")
    @Operation(
            summary = "Remover paciente",
            description = "Torna paciente inativo no sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Dados do paciente removido",
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
    public ResponseEntity<PacienteDTO> deletePaciente(
            @Parameter(
                    description = "Id do paciente",
                    example = "1",
                    required = true
            )
            @PathVariable Long id) {
        PacienteDTO pacienteExcluido = this.pacienteService.deletePaciente(id);
        return  ResponseEntity.ok(pacienteExcluido);
    }
}