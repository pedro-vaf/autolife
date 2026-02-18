package autolife.repository;

import autolife.entity.Consulta;
import autolife.entity.enums.StatusConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    @Query("SELECT c FROM Consulta c WHERE c.dataConsulta = :data AND c.status = 'AGENDADA'")
    List<Consulta> findConsultasAgendadasPorData(LocalDate data);

}