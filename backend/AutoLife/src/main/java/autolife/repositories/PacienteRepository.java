package autolife.repositories;

import autolife.entities.Paciente;
import autolife.entities.enums.AtividadeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Page<Paciente> findByStatus(AtividadeStatus status, Pageable pageable);
}