package autolife.entities;

import autolife.entities.enums.AtividadeStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "pacientes")
public class Paciente {
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
    private String telefone;

    @Getter
    private String cpf;

    @Getter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    private AtividadeStatus status = AtividadeStatus.ATIVO;

    public Paciente(String nome, String email, String telefone, String cpf, Endereco endereco) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.endereco = endereco;
    }

    public Paciente() {}
}