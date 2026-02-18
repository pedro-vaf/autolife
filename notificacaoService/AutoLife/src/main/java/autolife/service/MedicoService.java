package autolife.service;

import autolife.dto.medico.MedicoEmailDTO;
import autolife.dto.medico.MedicoRequest;
import autolife.dto.medico.MedicoResponse;
import autolife.entity.Medico;
import autolife.entity.enums.Especialidade;
import autolife.messaging.EmailNotificationPublisher;
import autolife.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final EmailNotificationPublisher emailPublisher;

    @Transactional
    public MedicoResponse criarCadastroMedico(MedicoRequest request) {
        Medico medico = Medico.builder()
                .medicoNome(request.getMedicoNome())
                .especialidade(Especialidade.valueOf((request.getEspecialidade())))
                .medicoEmail(request.getMedicoEmail())
                .build();

        Medico medicoSalva = medicoRepository.save(medico);

        try {
            MedicoEmailDTO emailData = construirEmailDTO(medicoSalva);
            emailPublisher.publicarCadastroMedico(emailData);
        } catch (Exception e) {
            throw  new RuntimeException("Erro ao publicar evento de email, mas cadastro foi criado", e);
        }

        return converterParaResponse(medicoSalva);
    }

    private MedicoEmailDTO construirEmailDTO(Medico medico) {
        return MedicoEmailDTO.builder()
                .medicoNome(medico.getMedicoNome())
                .especialidade(String.valueOf(medico.getEspecialidade()))
                .medicoEmail(medico.getMedicoEmail())
                .build();
    }

    private MedicoResponse converterParaResponse(Medico medico) {
        return MedicoResponse.builder()
                .id(medico.getId())
                .medicoNome(medico.getMedicoNome())
                .especialidade(String.valueOf(medico.getEspecialidade()))
                .medicoEmail(medico.getMedicoEmail())
                .build();
    }
}
