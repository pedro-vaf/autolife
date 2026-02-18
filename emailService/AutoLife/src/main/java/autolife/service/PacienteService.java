package autolife.service;

import autolife.dto.MedicoEmailDTO;
import autolife.dto.PacienteEmailDTO;
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
public class PacienteService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.name:Sistema AutoLife}")
    private String appName;

    public boolean enviarConfirmacao(PacienteEmailDTO dados) {
        log.info("Enviando email de cadastro para: {}", dados.getPacienteEmail());

        try {
            Context context = createContext(dados);
            context.setVariable("titulo", "Cadastro feito com Sucesso!");
            context.setVariable("mensagem", "Seu cadastro foi realizado com sucesso!");

            String htmlContent = templateEngine.process("cadastro-paciente", context);

            enviarEmailHtml(
                    dados.getPacienteEmail(),
                    "Confirmação de cadastro - " + appName,
                    htmlContent
            );

            log.info("Email de cadastro enviado com sucesso para: {}", dados.getPacienteNome());
            return true;

        } catch (Exception e) {
            log.error("Erro ao enviar email de cadastro para: {}", dados.getPacienteNome(), e);
            return false;
        }

    }

    private Context createContext(PacienteEmailDTO dados) {
        Context context = new Context();
        context.setVariable("pacienteNome", dados.getPacienteNome());
        context.setVariable("cpf", dados.getCpf());
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

