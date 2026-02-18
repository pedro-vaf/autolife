package autolife.entities;

import autolife.entities.enums.ConsultaCancelamentoMotivo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name="cancelamentos_consultas")
public class ConsultaCancelamento {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "consulta_id")
    private Consulta consulta;

    @Getter
    @Enumerated(EnumType.STRING)
    ConsultaCancelamentoMotivo motivoCancelamento;

    @Getter
    private LocalDateTime dataCancelamento;

    public ConsultaCancelamento(Consulta consulta, ConsultaCancelamentoMotivo motivoCancelamento,
                                LocalDateTime dataCancelamento) {
        this.consulta = consulta;
        this.motivoCancelamento = motivoCancelamento;
        this.dataCancelamento = dataCancelamento;
    }

    public ConsultaCancelamento() {}
}