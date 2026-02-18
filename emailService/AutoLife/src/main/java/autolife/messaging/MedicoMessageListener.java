package autolife.messaging;

import autolife.dto.MedicoEmailDTO;
import autolife.service.MedicoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MedicoMessageListener {

    private final MedicoService medicoService;

    @RabbitListener(queues = "${rabbitmq.queue.cadastro.medico}")
    public void handleConfirmacaoCadastro(MedicoEmailDTO dados) {
        log.info("Mensagem recebida para confirmação de cadastro: {}", dados.getMedicoNome());

        try {
            boolean sent = medicoService.enviarConfirmacao(dados);

            if (sent) {
                log.info("Email de confirmação processado com sucesso para: {}", dados.getMedicoEmail());
            } else {
                log.warn("Falha ao enviar email de confirmação para: {}", dados.getMedicoEmail());
            }

        } catch (Exception e) {
            log.error("Erro ao processar mensagem de confirmação", e);
            throw e;
        }
    }
}
