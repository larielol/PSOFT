package tccmanager.tccmanager.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tccmanager.tccmanager.Model.AreaEstudo;
import tccmanager.tccmanager.Model.TemaTCC;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorPostPutDto {

    @JsonProperty("nome")
    @NotBlank(message = "Nome do professor não pode ser vazio")
    private String nome;

    @JsonProperty("email")
    @NotBlank(message = "Email do professor não pode ser vazio")
    private String email;

    @JsonProperty("laboratorios")
    private String laboratorios;

    @JsonProperty("quota")
    @PositiveOrZero(message = "Quota não pode ser negativa")
    private int quota;
    private static final int MAX_QUOTA = 8;

    @JsonProperty("areasDeInteresse")
    private Set<AreaEstudo> areasDeInteresse;

    @JsonProperty("temasTCC")
    private Set<TemaTCC> temasTCC;
}
