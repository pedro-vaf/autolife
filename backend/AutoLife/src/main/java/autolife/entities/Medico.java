package autolife.entities;

import autolife.entities.enums.AtividadeStatus;
import autolife.entities.enums.Especialidade;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity(name = "medicos")
public class Medico {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    private String nome;

    @Getter
    private String email;

    @Setter
    @Getter
    private String telefone;

    @Getter
    private String crm;

    @Getter
    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    private AtividadeStatus status = AtividadeStatus.ATIVO;

    @Setter
    @Getter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    @OneToMany(mappedBy = "medico")
    private List<Consulta> consultas;

    public Medico(String nome, String email, String telefone, String crm, Especialidade especialidade, Endereco endereco) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.crm = crm;
        this.especialidade = especialidade;
        this.endereco = endereco;
    }

    public Medico() {}
}