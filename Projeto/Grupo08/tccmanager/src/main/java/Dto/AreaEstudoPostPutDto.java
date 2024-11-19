package tccmanager.tccmanager.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tccmanager.tccmanager.Model.TemaTCC;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AreaEstudoPostPutDto {

    @JsonProperty("nomeAreaEstudo")
    @NotBlank(message = "Nome da área de estudo não pode ser vazio")
    private String nomeAreaEstudo;

    @JsonProperty("temasTCC")
    private List<TemaTCC> temasTCC;
}
