package autolife.entities;

import autolife.entities.enums.ConsultaStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity(name = "consultas")
public class Consulta {
    private static final Duration DURACAO_PADRAO = Duration.ofHours(1);

    public static LocalDateTime calcularFimConsulta(LocalDateTime inicio) {
        return inicio.plus(DURACAO_PADRAO);
    }

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @Getter
    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;

    @Getter
    private LocalDateTime inicio;
    @Getter
    private LocalDateTime fim;

    @Setter
    @Enumerated(EnumType.STRING)
    private ConsultaStatus status = ConsultaStatus.AGENDADA;

    public Consulta(Paciente paciente, Medico medico, LocalDateTime inicio) {
        this.paciente = paciente;
        this.medico = medico;
        this.inicio = inicio;
        this.fim = calcularFimConsulta(inicio);
    }

    public Consulta() {}
}