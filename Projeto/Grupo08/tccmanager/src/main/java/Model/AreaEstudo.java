package tccmanager.tccmanager.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "areasEstudo")
public class AreaEstudo {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nomeAreaEstudo", unique = true, nullable = false)
    @JsonProperty("nomeAreaEstudo")
    private String nomeAreaEstudo;

    @ManyToMany(mappedBy = "areasEstudo", fetch = FetchType.EAGER)
    private List<TemaTCC> temasTCC;
}