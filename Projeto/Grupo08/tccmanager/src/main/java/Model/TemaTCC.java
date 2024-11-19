package tccmanager.tccmanager.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import tccmanager.tccmanager.Enum.StatusTemaTCC;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "temasTCC")
@JsonIgnoreProperties({"aluno", "professor"})
public class TemaTCC {
    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false, unique = true)
    @JsonProperty("titulo")
    private String titulo;

    @Column(name = "descricao", nullable = false)
    @JsonProperty("descricao")
    private String descricao;

    @JsonProperty("status")
    @Column(name = "status", nullable = false)
    private StatusTemaTCC status;

    @JsonProperty("areasEstudo")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "temaTCC_areasEstudo",
            joinColumns = @JoinColumn(name = "temaTCC_id"),
            inverseJoinColumns = @JoinColumn(name = "areasEstudo_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<AreaEstudo> areasEstudo = new HashSet<>();

    @JsonProperty("aluno")
    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @JsonProperty("professor")
    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;
}
