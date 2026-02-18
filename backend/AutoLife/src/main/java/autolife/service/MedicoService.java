package autolife.service;

import autolife.dto.medico.MedicoCreateForm;
import autolife.dto.medico.MedicoDTO;
import autolife.dto.medico.MedicoUpdateForm;
import autolife.entities.enums.AtividadeStatus;
import autolife.exception.ConsultaException;
import autolife.repositories.MedicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MedicoService {
    private final MedicoRepository medicoRepository;
    private final EnderecoService enderecoService;

    public MedicoService(MedicoRepository medicoRepository, EnderecoService enderecoService) {
        this.medicoRepository = medicoRepository;
        this.enderecoService = enderecoService;
    }

    public Page<MedicoDTO> getMedicosAtivos(Pageable pageable) {
        return this.medicoRepository.findByStatus(AtividadeStatus.ATIVO, pageable).map(MedicoDTO::new);
    }

    @Transactional
    public MedicoDTO saveMedico(MedicoCreateForm medicoForm) {
        var newMedico = medicoForm.toEntity();
        this.enderecoService.saveEndereco(medicoForm.endereco());
        this.medicoRepository.save(newMedico);
        return new MedicoDTO(newMedico);
    }

    @Transactional
    public MedicoDTO updateMedico(Long id, MedicoUpdateForm dados) throws ConsultaException {
        var medico = this.medicoRepository.findById(id).orElseThrow(() -> new ConsultaException("Médico não encontrado"));

        boolean nomeAlteradoNaoRemovido = dados.nome() != null && !dados.nome().isBlank();
        if (nomeAlteradoNaoRemovido)
            medico.setNome(dados.nome());

        boolean telefoneAlteradoNaoRemovido = dados.telefone() != null && !dados.telefone().isBlank();
        if (telefoneAlteradoNaoRemovido)
            medico.setTelefone(dados.telefone().replaceAll("\\D", ""));

        if (dados.endereco() != null) {
            enderecoService.updateEndereco(medico.getEndereco().getId(), dados.endereco());
        }

        this.medicoRepository.save(medico);
        return new MedicoDTO(medico);
    }

    @Transactional
    public MedicoDTO deleteMedico(Long id) {
        var medico = this.medicoRepository.findById(id).orElseThrow(() -> new ConsultaException("Médico não encontrado"));
        medico.setStatus(AtividadeStatus.INATIVO);
        return new MedicoDTO(medico);
    }
}