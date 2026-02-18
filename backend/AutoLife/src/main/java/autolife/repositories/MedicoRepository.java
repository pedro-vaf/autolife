package autolife.repositories;

import autolife.entities.Medico;
import autolife.entities.enums.AtividadeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    // Esta consulta busca encontrar um médico disponível no intervalo dado, ou seja,
    // que não tenha uma consulta registrada cujo intervalo intersecta o fornecido
    @Query(
        """
            SELECT m
            FROM medicos AS m
            WHERE NOT EXISTS (
                SELECT 1
                FROM m.consultas c
                WHERE m.status = autolife.entities.enums.AtividadeStatus.ATIVO
                    AND c.status =  autolife.entities.enums.ConsultaStatus.AGENDADA
                    AND c.medico = m
                    AND c.inicio < :fim
                    AND c.fim > :inicio
            )
        """
    )
    List<Medico> findMedicosDisponiveis(
            @Param("inicio")LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            Pageable pageable
    );

    Page<Medico> findByStatus(AtividadeStatus status, Pageable pageable);
}
