package autolife.service;

import autolife.dto.ConsultaEmailDTO;
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

import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.name:Sistema AutoLife}")
    private String appName;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public boolean enviarConfirmacao(ConsultaEmailDTO consultaData) {
        log.info("Enviando email de confirmação para: {}", consultaData.getPacienteEmail());

        try {
            Context context = createContext(consultaData);
            context.setVariable("titulo", "Consulta Agendada com Sucesso!");
            context.setVariable("mensagem", "Sua consulta foi agendada com sucesso. Seguem os detalhes:");

            String htmlContent = templateEngine.process("consulta-confirmacao", context);

            enviarEmailHtml(
                    consultaData.getPacienteEmail(),
                    "Confirmação de Consulta - " + appName,
                    htmlContent
            );

            log.info("Email de confirmação enviado com sucesso para: {}", consultaData.getPacienteEmail());
            return true;

        } catch (Exception e) {
            log.error("Erro ao enviar email de confirmação para: {}", consultaData.getPacienteEmail(), e);
            return false;
        }
    }

    public boolean enviarCancelamento(ConsultaEmailDTO consultaData) {
        log.info("Enviando email de cancelamento para: {}", consultaData.getPacienteEmail());

        try {
            Context context = createContext(consultaData);
            context.setVariable("titulo", "Consulta Cancelada");
            context.setVariable("mensagem", "Sua consulta foi cancelada. Seguem os detalhes:");

            String htmlContent = templateEngine.process("consulta-cancelamento", context);

            enviarEmailHtml(
                    consultaData.getPacienteEmail(),
                    "Cancelamento de Consulta - " + appName,
                    htmlContent
            );

            log.info("Email de cancelamento enviado com sucesso para: {}", consultaData.getPacienteEmail());
            return true;

        } catch (Exception e) {
            log.error("Erro ao enviar email de cancelamento para: {}", consultaData.getPacienteEmail(), e);
            return false;
        }
    }

    public boolean enviarLembrete(ConsultaEmailDTO consultaData) {
        log.info("Enviando email de lembrete para: {}", consultaData.getPacienteEmail());

        try {
            Context context = createContext(consultaData);
            context.setVariable("titulo", "Lembrete de Consulta");
            context.setVariable("mensagem", "Este é um lembrete da sua consulta agendada:");

            String htmlContent = templateEngine.process("consulta-lembrete", context);

            enviarEmailHtml(
                    consultaData.getPacienteEmail(),
                    "Lembrete de Consulta - " + appName,
                    htmlContent
            );

            log.info("Email de lembrete enviado com sucesso para: {}", consultaData.getPacienteEmail());
            return true;

        } catch (Exception e) {
            log.error("Erro ao enviar email de lembrete para: {}", consultaData.getPacienteEmail(), e);
            return false;
        }
    }

    private Context createContext(ConsultaEmailDTO consultaData) {
        Context context = new Context();
        context.setVariable("pacienteNome", consultaData.getPacienteNome());
        context.setVariable("medicoNome", consultaData.getMedicoNome());
        context.setVariable("especialidade", consultaData.getEspecialidade());
        context.setVariable("dataConsulta", consultaData.getDataConsulta().format(DATE_FORMATTER));
        context.setVariable("horaConsulta", consultaData.getHoraConsulta().format(TIME_FORMATTER));
        context.setVariable("local", consultaData.getLocal());
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
