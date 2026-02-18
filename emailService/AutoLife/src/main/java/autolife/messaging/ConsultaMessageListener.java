package autolife.messaging;

import autolife.dto.ConsultaEmailDTO;
import autolife.dto.MedicoEmailDTO;
import autolife.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsultaMessageListener {

    private final EmailService emailService;

    @RabbitListener(queues = "${rabbitmq.queue.consulta.confirmacao}")
    public void handleConfirmacao(ConsultaEmailDTO consultaData) {
        log.info("Mensagem recebida para confirmação de consulta: {}", consultaData.getConsultaId());

        try {
            boolean sent = emailService.enviarConfirmacao(consultaData);

            if (sent) {
                log.info("Email de confirmação processado com sucesso para: {}", consultaData.getPacienteEmail());
            } else {
                log.warn("Falha ao enviar email de confirmação para: {}", consultaData.getPacienteEmail());
            }

        } catch (Exception e) {
            log.error("Erro ao processar mensagem de confirmação", e);
            throw e;
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.consulta.cancelamento}")
    public void handleCancelamento(ConsultaEmailDTO consultaData) {
        log.info("Mensagem recebida para cancelamento de consulta: {}", consultaData.getConsultaId());

        try {
            boolean sent = emailService.enviarCancelamento(consultaData);

            if (sent) {
                log.info("Email de cancelamento processado com sucesso para: {}", consultaData.getPacienteEmail());
            } else {
                log.warn("Falha ao enviar email de cancelamento para: {}", consultaData.getPacienteEmail());
            }

        } catch (Exception e) {
            log.error("Erro ao processar mensagem de cancelamento", e);
            throw e;
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.consulta.lembrete}")
    public void handleLembrete(ConsultaEmailDTO consultaData) {
        log.info("Mensagem recebida para lembrete de consulta: {}", consultaData.getConsultaId());

        try {
            boolean sent = emailService.enviarLembrete(consultaData);

            if (sent) {
                log.info("Email de lembrete processado com sucesso para: {}", consultaData.getPacienteEmail());
            } else {
                log.warn("Falha ao enviar email de lembrete para: {}", consultaData.getPacienteEmail());
            }

        } catch (Exception e) {
            log.error("Erro ao processar mensagem de lembrete", e);
            throw e;
        }
    }
}
