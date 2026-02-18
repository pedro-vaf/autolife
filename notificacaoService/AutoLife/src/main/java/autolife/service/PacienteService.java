package autolife.service;

import autolife.dto.paciente.PacienteEmailDTO;
import autolife.dto.paciente.PacienteRequest;
import autolife.dto.paciente.PacienteResponse;
import autolife.entity.Paciente;
import autolife.messaging.EmailNotificationPublisher;
import autolife.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final EmailNotificationPublisher emailPublisher;

    @Transactional
    public PacienteResponse criarCadastroPaciente(PacienteRequest request) {
        Paciente paciente = Paciente.builder()
                .pacienteNome(request.getPacienteNome())
                .cpf(request.getCpf())
                .pacienteEmail(request.getPacienteEmail())
                .build();

        Paciente pacienteSalva = pacienteRepository.save(paciente);

        try {
            PacienteEmailDTO emailData = construirEmailDTO(pacienteSalva);
            emailPublisher.publicarCadastroPaciente(emailData);
        } catch (Exception e) {
            throw  new RuntimeException("Erro ao publicar evento de email, mas cadastro foi criado", e);
        }

        return converterParaResponse(pacienteSalva);
    }

    private PacienteEmailDTO construirEmailDTO(Paciente paciente) {
        return PacienteEmailDTO.builder()
                .pacienteNome(paciente.getPacienteNome())
                .cpf(paciente.getCpf())
                .pacienteEmail(paciente.getPacienteEmail())
                .build();
    }

    private PacienteResponse converterParaResponse(Paciente paciente) {
        return PacienteResponse.builder()
                .id(paciente.getId())
                .pacienteNome(paciente.getPacienteNome())
                .cpf(paciente.getCpf())
                .pacienteEmail(paciente.getPacienteEmail())
                .build();
    }
}
