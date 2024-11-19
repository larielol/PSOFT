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
@Table(name = "professores")
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(name = "nome", nullable = false)
    @JsonProperty("nome")
    private String nome;

    @Column(name = "email", unique = true, nullable = false)
    @JsonProperty("email")
    private String email;

    @Column(name = "laboratorios", length = 500)
    @JsonProperty("laboratorios")
    private String laboratorios;

    @Column(name = "quota", nullable = false)
    @JsonProperty("quota")
    private int quota;
    public static final int MAX_QUOTA = 8;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty("temasTCC")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<TemaTCC> temasTCC = new HashSet<>();

    @JsonProperty("areasDeInteresse")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "professor_areaEstudo",
            joinColumns = @JoinColumn(name = "professor_id"),
            inverseJoinColumns = @JoinColumn(name = "areaEstudo_id"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<AreaEstudo> areasDeInteresse = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Professor professor = (Professor) o;
        return nome.equals(professor.nome) &&
                email.equals(professor.email) &&
                laboratorios.equals(professor.laboratorios) &&
                quota== professor.quota;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, email, laboratorios, quota);
    }
}
