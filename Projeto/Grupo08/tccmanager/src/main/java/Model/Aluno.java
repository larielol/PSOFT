package tccmanager.tccmanager.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "alunos")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(name = "nome", nullable = false)
    @JsonProperty("nome")
    private String nome;

    @Column(name = "matricula", unique = true, nullable = false)
    @JsonProperty("matricula")
    private String matricula;

    @Column(name = "email", unique = true, nullable = false)
    @JsonProperty("email")
    private String email;

    @Column(name = "periodoConclusao", nullable = false)
    @JsonProperty("periodoConclusao")
    private String periodoConclusao;

    @JsonProperty("areasDeInteresse")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "aluno_areaEstudo",
                joinColumns = @JoinColumn(name = "aluno_id"),
                inverseJoinColumns = @JoinColumn(name = "areaEstudo_id"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<AreaEstudo> areasDeInteresse = new HashSet<>();

    @JsonProperty("temasTCC")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<TemaTCC> temasTCC = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aluno aluno = (Aluno) o;
        return nome.equals(aluno.nome) &&
                matricula.equals(aluno.matricula) &&
                email.equals(aluno.email) &&
                periodoConclusao.equals(aluno.periodoConclusao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, matricula, email, periodoConclusao);
    }
}
