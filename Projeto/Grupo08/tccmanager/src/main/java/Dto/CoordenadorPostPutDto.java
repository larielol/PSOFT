package tccmanager.tccmanager.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoordenadorPostPutDto {

    @JsonProperty("email")
    @NotBlank(message = "O email do coordenador não pode ser vazio")
    private String email;
}
