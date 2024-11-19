package tccmanager.tccmanager.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import tccmanager.tccmanager.Model.AreaEstudo;
import tccmanager.tccmanager.Model.TemaTCC;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlunoPostPutDto {

    @JsonProperty("nome")
    @NotBlank(message = "Nome do aluno não pode ser vazio")
    private String nome;

    @JsonProperty("matricula")
    @NotBlank(message = "Matrícula do aluno não pode ser vazia")
    private String matricula;

    @JsonProperty("email")
    @NotBlank(message = "Email do aluno não pode ser vazio")
    private String email;

    @JsonProperty("periodoConclusao")
    @NotBlank(message = "Período de conclusão do aluno não pode ser vazio")
    private String periodoConclusao;

    @JsonProperty("areasDeInteresse")
    private java.util.Set<AreaEstudo> areasDeInteresse;

    @JsonProperty("temasTCC")
    private java.util.Set<TemaTCC> temasTCC;
}
