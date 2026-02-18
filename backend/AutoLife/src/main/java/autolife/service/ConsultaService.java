package autolife.service;

import autolife.dto.consulta.ConsultaCancelamentoDTO;
import autolife.dto.consulta.ConsultaCancelamentoForm;
import autolife.dto.consulta.ConsultaDTO;
import autolife.dto.consulta.ConsultaMarcacaoForm;
import autolife.entities.Consulta;
import autolife.entities.ConsultaCancelamento;
import autolife.entities.Medico;
import autolife.entities.Paciente;
import autolife.entities.enums.AtividadeStatus;
import autolife.entities.enums.ConsultaStatus;
import autolife.exception.ConsultaException;
import autolife.repositories.ConsultaCancelamentoRepository;
import autolife.repositories.ConsultaRepository;
import autolife.repositories.MedicoRepository;
import autolife.repositories.PacienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.*;
import java.util.Optional;

@Service
public class ConsultaService {
    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final ConsultaCancelamentoRepository consultaCancelamentoRepository;

    public ConsultaService (
            MedicoRepository medicoRepository,
            PacienteRepository pacienteRepository,
            ConsultaRepository consultaRepository,
            ConsultaCancelamentoRepository consultaCancelamentoRepository
    ) {
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.consultaRepository = consultaRepository;
        this.consultaCancelamentoRepository = consultaCancelamentoRepository;
    }

    public Page<ConsultaDTO> getConsultasAgendadas(Pageable pageable) {
        return this.consultaRepository.findByStatus(ConsultaStatus.AGENDADA, pageable).map(ConsultaDTO::new);
    }

    @Transactional
    public ConsultaDTO marcarConsulta(ConsultaMarcacaoForm form) throws ConsultaException {
        validarHorarioConsulta(form.inicioConsulta());

        var paciente = pacienteRepository.findById(form.pacienteId()).orElseThrow(() -> new ConsultaException("Paciente não encontrado"));
        var inicioConsulta = form.inicioConsulta();
        var fimConsulta = Consulta.calcularFimConsulta(inicioConsulta);

        validarPacienteConsulta(paciente, inicioConsulta);

        Medico medico;
        if (form.medicoId() != null) {
            medico = medicoRepository.findById(form.medicoId()).orElseThrow(() -> new ConsultaException("Médico não encontrado"));
            validarMedicoConsulta(medico, form.inicioConsulta());
        }

        Optional<Medico> medicoDisponivel = encontrarMedicoDisponivel(inicioConsulta, fimConsulta);
        if (medicoDisponivel.isEmpty())
            throw new ConsultaException("Nenhum médico encontrado na data de consulta fornecida");

        medico = medicoDisponivel.get();

        var consulta = new Consulta(paciente, medico, form.inicioConsulta());
        consultaRepository.save(consulta);

        return new ConsultaDTO(consulta);
    }

    @Transactional
    public ConsultaCancelamentoDTO cancelarConsulta(Long id, ConsultaCancelamentoForm form) throws ConsultaException {
        var consulta = consultaRepository.findById(id).orElse(null);

        if (consulta == null)
            throw new ConsultaException("Consulta não registrada");

        Duration intervaloEntreMarcacaoECancelamento = Duration.between(LocalDateTime.now(), consulta.getInicio());

        if (intervaloEntreMarcacaoECancelamento.toHours() <= 24)
            throw new ConsultaException("Um intervalo de pelo menos 24h deve ser respeitado entre marcação e cancelamento de consulta");

        consulta.setStatus(ConsultaStatus.CANCELADA);
        consultaRepository.save(consulta);

        var consultaCancelamento = new ConsultaCancelamento(consulta, form.motivoCancelamento(), LocalDateTime.now());
        consultaCancelamentoRepository.save(consultaCancelamento);

        return new ConsultaCancelamentoDTO(consulta.getPaciente().getNome(),
                consultaCancelamento.getMotivoCancelamento(), consultaCancelamento.getDataCancelamento());
    }

    private void validarHorarioConsulta(LocalDateTime dataHorarioConsulta) throws ConsultaException{
        var dataHorarioMarcacao = LocalDateTime.now();

        if (dataHorarioConsulta.isBefore(dataHorarioMarcacao))
            throw new ConsultaException("A consulta está sendo marcada para uma data anterior à atual");

        var dataConsulta = dataHorarioConsulta.toLocalDate();
        var horarioConsulta = dataHorarioConsulta.toLocalTime();

        var dataCorrente = dataHorarioMarcacao.toLocalDate();
        var horarioCorrente = dataHorarioMarcacao.toLocalTime();

        if (dataConsulta.getDayOfWeek() == DayOfWeek.SUNDAY)
            throw new ConsultaException("A clínica não funciona em dias de domingo");

        boolean foraDoHorarioComercial = horarioConsulta.isBefore(LocalTime.of(7,0))
                || horarioConsulta.isAfter(LocalTime.of(19,0));

        if (foraDoHorarioComercial)
            throw new ConsultaException("Tentativa de marcação fora do horário da clínica");

        var intervaloTempoEntreMarcacaoEConsulta = Duration.between(horarioConsulta, horarioCorrente);

        if (dataConsulta.equals(dataCorrente) && intervaloTempoEntreMarcacaoEConsulta.toMinutes() < 30)
            throw new ConsultaException("Tentativa de marcação muito recente (menos de 30 minutos)");
    }

    private void validarPacienteConsulta (Paciente paciente, LocalDateTime inicioConsulta) throws ConsultaException {
        boolean pacienteInexistenteOuInativo = paciente == null || paciente.getStatus() == AtividadeStatus.INATIVO;

        if (pacienteInexistenteOuInativo)
            throw new ConsultaException("Paciente não encontrado no sistema");

        LocalDate dataConsulta = inicioConsulta.toLocalDate();

        LocalDateTime inicioDiaConsulta = dataConsulta.atStartOfDay();
        LocalDateTime fimDiaConsulta = dataConsulta.plusDays(1).atStartOfDay();

        boolean jaPossuiConsultaAgendadaNoDia = consultaRepository
                .existsByPacienteAndStatusAndInicioBetween(
                        paciente,
                        ConsultaStatus.AGENDADA,
                        inicioDiaConsulta,
                        fimDiaConsulta
                );

        if (jaPossuiConsultaAgendadaNoDia)
            throw new ConsultaException("O paciente já possui consulta marcada no mesmo dia");
    }

    private void validarMedicoConsulta(Medico medico, LocalDateTime inicioConsulta) throws ConsultaException {
        boolean medicoInexistenteOuInativo = medico == null
                || medico.getStatus() == AtividadeStatus.INATIVO;

        if (medicoInexistenteOuInativo)
            throw new ConsultaException("Médico não encontrado no sistema");

        var fimConsulta = Consulta.calcularFimConsulta(inicioConsulta);

        boolean jaPossuiConsultaAgendadaNoHorarioFornecido = consultaRepository
                .existsByMedicoAndStatusAndInicioLessThanAndFimGreaterThan(
                        medico,
                        ConsultaStatus.AGENDADA,
                        fimConsulta,
                        inicioConsulta
                );

        if (jaPossuiConsultaAgendadaNoHorarioFornecido)
            throw new ConsultaException("Médico já possui consulta na data fornecida");
    }

    private Optional<Medico> encontrarMedicoDisponivel(
            LocalDateTime inicioConsulta, LocalDateTime fimConsulta) {
        return medicoRepository
                .findMedicosDisponiveis(
                        inicioConsulta,
                        fimConsulta,
                        PageRequest.of(0, 1)
                )
                .stream()
                .findFirst();
    }
}