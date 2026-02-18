package autolife.repositories;

import autolife.entities.Consulta;
import autolife.entities.Medico;
import autolife.entities.Paciente;
import autolife.entities.enums.ConsultaStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    // Essa query busca determinar se o paciente possui alguma consulta no intervalo dado (normalmente o início e o
    // fim de um dia)
    boolean existsByPacienteAndStatusAndInicioBetween(
            Paciente paciente,
            ConsultaStatus consultaStatus,
            LocalDateTime inicio,
            LocalDateTime fim
    );

    // Essa query busca determinar se existe alguma consulta do médico que intersecte o intervalo fornecido
    // (início antes do fim do intervalo dado ou fim depois do início)
    boolean existsByMedicoAndStatusAndInicioLessThanAndFimGreaterThan(
            Medico medico,
            ConsultaStatus consultaStatus,
            LocalDateTime fim,
            LocalDateTime inicio
    );

    Page<Consulta> findByStatus(ConsultaStatus status, Pageable pageable);
}