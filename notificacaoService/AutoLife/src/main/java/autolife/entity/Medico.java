package autolife.entity;

import autolife.entity.enums.Especialidade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medicos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String medicoNome;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;

    @Column(nullable = false)
    private String medicoEmail;
}
