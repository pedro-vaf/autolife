package autolife.service;

import autolife.dto.paciente.PacienteCreateForm;
import autolife.dto.paciente.PacienteDTO;
import autolife.dto.paciente.PacienteUpdateForm;
import autolife.entities.enums.AtividadeStatus;
import autolife.exception.ConsultaException;
import autolife.repositories.PacienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {
    private final PacienteRepository pacienteRepository;
    private final EnderecoService enderecoService;

    public PacienteService(PacienteRepository pacienteRepository, EnderecoService enderecoService) {
        this.pacienteRepository = pacienteRepository;
        this.enderecoService = enderecoService;
    }

    @Transactional
    public PacienteDTO savePaciente(PacienteCreateForm pacienteCreateForm) {
        var paciente = pacienteCreateForm.toEntity();
        this.enderecoService.saveEndereco(pacienteCreateForm.endereco());
        this.pacienteRepository.save(paciente);
        return new PacienteDTO(paciente);
    }

    public Page<PacienteDTO> getPacientesAtivos(Pageable pageable) {
        return this.pacienteRepository.findByStatus(AtividadeStatus.ATIVO, pageable).map(PacienteDTO::new);
    }

    @Transactional
    public PacienteDTO updatePaciente(Long id, PacienteUpdateForm form) throws ConsultaException {
        var paciente = this.pacienteRepository.findById(id).orElseThrow(() -> new ConsultaException("Paciente não encontrado"));

        if (form.nome() != null && !form.nome().isEmpty())
            paciente.setNome(form.nome());

        if (form.telefone() != null && !form.telefone().isEmpty())
            paciente.setTelefone(form.telefone().replace("\\D", ""));

        if (form.endereco() != null) {
            enderecoService.updateEndereco(paciente.getEndereco().getId(), form.endereco());
        }

        this.pacienteRepository.save(paciente);
        return new PacienteDTO(paciente);
    }

    @Transactional
    public PacienteDTO deletePaciente(Long id) throws  ConsultaException {
        var paciente = this.pacienteRepository.findById(id).orElseThrow(() -> new ConsultaException("Paciente não encontrado"));
        paciente.setStatus(AtividadeStatus.INATIVO);
        return new PacienteDTO(paciente);
    }
}