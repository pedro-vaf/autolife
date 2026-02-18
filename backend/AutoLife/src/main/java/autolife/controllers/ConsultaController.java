package autolife.controllers;

import autolife.client.NotificacaoClient;
import autolife.dto.agendamento.ConsultaRequest;
import autolife.dto.consulta.ConsultaCancelamentoDTO;
import autolife.dto.consulta.ConsultaCancelamentoForm;
import autolife.dto.consulta.ConsultaDTO;
import autolife.dto.consulta.ConsultaMarcacaoForm;
import autolife.entities.Medico;
import autolife.entities.Paciente;
import autolife.exception.ConsultaError;
import autolife.repositories.MedicoRepository;
import autolife.repositories.PacienteRepository;
import autolife.service.ConsultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {
    private final ConsultaService consultaService;
    private final NotificacaoClient notificacaoClient;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    public ConsultaController(ConsultaService consultaService, NotificacaoClient notificacaoClient, PacienteRepository pacienteRepository, MedicoRepository medicoRepository) {
        this.consultaService = consultaService;
        this.notificacaoClient = notificacaoClient;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
    }

    @Operation(
            summary = "Obtém consultas",
            description = "Obtém uma lista paginada de consultas agendadas. " +
                    "Por padrão, retorna-se 10 itens."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de consultas agendadas",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ConsultaDTO.class)
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
    @GetMapping
    public Page<ConsultaDTO> getConsultas(@PageableDefault(size = 10) Pageable pageable) {
        return this.consultaService.getConsultasAgendadas(pageable);
    }

    @Operation(
            summary = "Marcar consulta",
            description = "Marca uma nova consulta"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Dados da consulta marcada",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ConsultaDTO.class)
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
    @PostMapping("/marcar")
    public ResponseEntity<ConsultaDTO> marcarConsulta(@RequestBody @Valid ConsultaMarcacaoForm form) {
        var consultaDTO = this.consultaService.marcarConsulta(form);

        Paciente paciente = pacienteRepository.getReferenceById(form.pacienteId());
        Medico medico = medicoRepository.getReferenceById(form.medicoId());

        ConsultaRequest request = ConsultaRequest.builder()
                .pacienteNome(paciente.getNome())
                .pacienteEmail(paciente.getEmail())
                .medicoNome(medico.getNome())
                .especialidade(String.valueOf(medico.getEspecialidade()))
                .dataConsulta(form.inicioConsulta().toLocalDate())
                .horaConsulta(form.inicioConsulta().toLocalTime())
                .local("Clínica Autolife")
                .build();

        notificacaoClient.criarConsulta(request);

        return ResponseEntity.ok(consultaDTO);
    }

    @Operation(
            summary = "Cancelar consulta",
            description = "Cancela uma consulta"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Dados do cancelamento da consulta",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ConsultaCancelamentoDTO.class)
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
    @PostMapping("/cancelar/{id}")
    public ResponseEntity<ConsultaCancelamentoDTO> cancelarConsulta(@PathVariable Long id, @RequestBody ConsultaCancelamentoForm form) {
        var consultaCancelamentoDTO = this.consultaService.cancelarConsulta(id, form);
        notificacaoClient.cancelarConsulta(id);
        return ResponseEntity.ok(consultaCancelamentoDTO);
    }
}