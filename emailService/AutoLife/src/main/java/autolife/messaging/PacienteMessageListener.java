package autolife.messaging;

import autolife.dto.PacienteEmailDTO;
import autolife.service.PacienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PacienteMessageListener {

    private final PacienteService pacienteService;

    @RabbitListener(queues = "${rabbitmq.queue.cadastro.paciente}")
    public void handleConfirmacaoCadastro(PacienteEmailDTO dados) {
        log.info("Mensagem recebida para confirmação de cadastro: {}", dados.getPacienteEmail());

        try {
            boolean sent = pacienteService.enviarConfirmacao(dados);

            if (sent) {
                log.info("Email de confirmação processado com sucesso para: {}", dados.getPacienteEmail());
            } else {
                log.warn("Falha ao enviar email de confirmação para: {}", dados.getPacienteEmail());
            }

        } catch (Exception e) {
            log.error("Erro ao processar mensagem de confirmação", e);
            throw e;
        }
    }
}
