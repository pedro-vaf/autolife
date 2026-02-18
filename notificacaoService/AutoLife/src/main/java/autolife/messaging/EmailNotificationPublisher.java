package autolife.messaging;

import autolife.dto.consulta.ConsultaEmailDTO;
import autolife.dto.medico.MedicoEmailDTO;
import autolife.dto.paciente.PacienteEmailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.queue.consulta.confirmacao}")
    private String confirmacaoQueue;

    @Value("${rabbitmq.queue.consulta.cancelamento}")
    private String cancelamentoQueue;

    @Value("${rabbitmq.queue.consulta.lembrete}")
    private String lembreteQueue;

    @Value("${rabbitmq.queue.cadastro.medico}")
    private String cadastroMedicoQueue;

    @Value("${rabbitmq.queue.cadastro.paciente}")
    private String cadastroPacienteQueue;


    public void publicarConfirmacao(ConsultaEmailDTO emailData) {
        try {
            log.info("Publicando mensagem de confirmação para: {}", emailData.getPacienteEmail());
            rabbitTemplate.convertAndSend(confirmacaoQueue, emailData);
            log.info("Mensagem de confirmação publicada com sucesso - ID: {}", emailData.getConsultaId());
        } catch (Exception e) {
            log.error("Erro ao publicar mensagem de confirmação para: {}", emailData.getPacienteEmail(), e);
            throw new RuntimeException("Falha ao publicar evento de confirmação", e);
        }
    }

    public void publicarCancelamento(ConsultaEmailDTO emailData) {
        try {
            log.info("Publicando mensagem de cancelamento para: {}", emailData.getPacienteEmail());
            rabbitTemplate.convertAndSend(cancelamentoQueue, emailData);
            log.info("Mensagem de cancelamento publicada com sucesso - ID: {}", emailData.getConsultaId());
        } catch (Exception e) {
            log.error("Erro ao publicar mensagem de cancelamento para: {}", emailData.getPacienteEmail(), e);
            throw new RuntimeException("Falha ao publicar evento de cancelamento", e);
        }
    }

    public void publicarLembrete(ConsultaEmailDTO emailData) {
        try {
            log.info("Publicando mensagem de lembrete para: {}", emailData.getPacienteEmail());
            rabbitTemplate.convertAndSend(lembreteQueue, emailData);
            log.info("Mensagem de lembrete publicada com sucesso - ID: {}", emailData.getConsultaId());
        } catch (Exception e) {
            log.error("Erro ao publicar mensagem de lembrete para: {}", emailData.getPacienteEmail(), e);
            throw new RuntimeException("Falha ao publicar evento de lembrete", e);
        }
    }

    public void publicarCadastroMedico(MedicoEmailDTO dados) {
        try {
            log.info("Publicando mensagem de cadastro para: {}", dados.getMedicoEmail());
            rabbitTemplate.convertAndSend(cadastroMedicoQueue, dados);
            log.info("Mensagem de cadastro publicada com sucesso - e-mail: {}", dados.getMedicoEmail());
        } catch (Exception e) {
            log.error("Erro ao publicar mensagem de cadastro para: {}", dados.getMedicoEmail(), e);
            throw new RuntimeException("Falha ao publicar evento de cadastro", e);
        }
    }

    public void publicarCadastroPaciente(PacienteEmailDTO dados) {
        try {
            log.info("Publicando mensagem de cadastro para: {}", dados.getPacienteEmail());
            rabbitTemplate.convertAndSend(cadastroPacienteQueue, dados);
            log.info("Mensagem de cadastro publicada com sucesso - e-mail: {}", dados.getPacienteEmail());
        } catch (Exception e) {
            log.error("Erro ao publicar mensagem de cadastro para: {}", dados.getPacienteEmail(), e);
            throw new RuntimeException("Falha ao publicar evento de cadastro", e);
        }
    }

}

