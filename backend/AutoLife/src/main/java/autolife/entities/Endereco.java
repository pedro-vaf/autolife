package autolife.entities;

import autolife.entities.enums.UnidadeFederativa;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity(name = "enderecos")
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    private String logradouro;

    @Setter
    private String numero;

    @Setter
    private String complemento;

    @Setter
    private String bairro;

    @Setter
    private String cidade;

    @Setter
    private String cep;

    @Setter
    @Enumerated(EnumType.STRING)
    private UnidadeFederativa uf;

    public Endereco() {}

    public Endereco(String logradouro, String numero, String complemento, String bairro, String cidade,
                    UnidadeFederativa uf, String cep) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.cep = cep;
    }
}