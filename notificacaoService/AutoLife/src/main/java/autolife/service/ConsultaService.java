package autolife.service;

import autolife.dto.consulta.ConsultaEmailDTO;
import autolife.dto.consulta.ConsultaRequest;
import autolife.dto.consulta.ConsultaResponse;
import autolife.entity.Consulta;
import autolife.entity.enums.StatusConsulta;
import autolife.exception.ConsultaNaoEncontradaException;
import autolife.messaging.EmailNotificationPublisher;
import autolife.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final EmailNotificationPublisher emailPublisher;

    @Transactional
    public ConsultaResponse criarConsulta(ConsultaRequest request) {
        Consulta consulta = Consulta.builder()
                .pacienteNome(request.getPacienteNome())
                .pacienteEmail(request.getPacienteEmail())
                .medicoNome(request.getMedicoNome())
                .especialidade(request.getEspecialidade())
                .dataConsulta(request.getDataConsulta())
                .horaConsulta(request.getHoraConsulta())
                .local(request.getLocal())
                .status(StatusConsulta.AGENDADA)
                .build();

        Consulta consultaSalva = consultaRepository.save(consulta);

        try {
            ConsultaEmailDTO emailData = construirEmailDTO(consultaSalva);
            emailPublisher.publicarConfirmacao(emailData);
        } catch (Exception e) {
            throw  new RuntimeException("Erro ao publicar evento de email, mas consulta foi criada", e);
        }

        return converterParaResponse(consultaSalva);
    }

    @Transactional
    public ConsultaResponse cancelarConsulta(Long id) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ConsultaNaoEncontradaException("Consulta não encontrada com ID: " + id));

        consulta.setStatus(StatusConsulta.CANCELADA);
        Consulta consultaCancelada = consultaRepository.save(consulta);

        try {
            ConsultaEmailDTO emailData = construirEmailDTO(consultaCancelada);
            emailPublisher.publicarCancelamento(emailData);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao publicar evento de cancelamento de email", e);
        }

        return converterParaResponse(consultaCancelada);
    }

    public void enviarLembretes() {

        LocalDate amanha = LocalDate.now().plusDays(1);
        List<Consulta> consultasAmanha = consultaRepository.findConsultasAgendadasPorData(amanha);

        for (Consulta consulta : consultasAmanha) {
            try {
                ConsultaEmailDTO emailData = construirEmailDTO(consulta);
                emailPublisher.publicarLembrete(emailData);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao enviar lembrete para consulta ID: {}",  e);
            }
        }
    }

    private ConsultaEmailDTO construirEmailDTO(Consulta consulta) {
        return ConsultaEmailDTO.builder()
                .pacienteNome(consulta.getPacienteNome())
                .pacienteEmail(consulta.getPacienteEmail())
                .medicoNome(consulta.getMedicoNome())
                .especialidade(consulta.getEspecialidade())
                .dataConsulta(consulta.getDataConsulta())
                .horaConsulta(consulta.getHoraConsulta())
                .local(consulta.getLocal())
                .consultaId(consulta.getId().toString())
                .build();
    }

    private ConsultaResponse converterParaResponse(Consulta consulta) {
        return ConsultaResponse.builder()
                .id(consulta.getId())
                .pacienteNome(consulta.getPacienteNome())
                .pacienteEmail(consulta.getPacienteEmail())
                .medicoNome(consulta.getMedicoNome())
                .especialidade(consulta.getEspecialidade())
                .dataConsulta(consulta.getDataConsulta())
                .horaConsulta(consulta.getHoraConsulta())
                .local(consulta.getLocal())
                .status(consulta.getStatus())
                .criadoEm(consulta.getCriadoEm())
                .atualizadoEm(consulta.getAtualizadoEm())
                .build();
    }
}