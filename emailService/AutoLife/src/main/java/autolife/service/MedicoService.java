package autolife.service;

import autolife.dto.ConsultaEmailDTO;
import autolife.dto.MedicoEmailDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicoService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.name:Sistema AutoLife}")
    private String appName;

    public boolean enviarConfirmacao(MedicoEmailDTO dados) {
        log.info("Enviando email de confirmação para: {}", dados.getMedicoEmail());

        try {
            Context context = createContext(dados);
            context.setVariable("titulo", "Cadastro feito com Sucesso!");
            context.setVariable("mensagem", "Seu cadastro foi realizado com sucesso!");

            String htmlContent = templateEngine.process("cadastro-medico", context);

            enviarEmailHtml(
                    dados.getMedicoEmail(),
                    "Confirmação de cadastro - " + appName,
                    htmlContent
            );

            log.info("Email de cadastro enviado com sucesso para: {}", dados.getMedicoEmail());
            return true;

        } catch (Exception e) {
            log.error("Erro ao enviar email de cadastro para: {}", dados.getMedicoEmail(), e);
            return false;
        }

    }

    private Context createContext(MedicoEmailDTO dados) {
        Context context = new Context();
        context.setVariable("medicoNome", dados.getMedicoNome());
        context.setVariable("especialidade", dados.getEspecialidade());
        context.setVariable("medicoEmail", dados.getMedicoEmail());
        context.setVariable("appName", appName);

        return context;
    }

    private void enviarEmailHtml(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
