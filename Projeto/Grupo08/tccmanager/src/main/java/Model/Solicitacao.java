package tccmanager.tccmanager.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tccmanager.tccmanager.Enum.StatusSolicitacao;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "solicitacoes")
public class Solicitacao {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("aluno")
    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @JsonProperty("professor")
    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @JsonProperty("tema")
    @ManyToOne
    @JoinColumn(name = "tema_id")
    private TemaTCC tema;

    @JsonProperty("status")
    @Column(name = "status", nullable = false)
    private StatusSolicitacao status;
}
