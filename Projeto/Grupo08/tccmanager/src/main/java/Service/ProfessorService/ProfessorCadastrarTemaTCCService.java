package tccmanager.tccmanager.Service.ProfessorService;

import tccmanager.tccmanager.Dto.TemaTCCPostPutDto;
import tccmanager.tccmanager.Model.TemaTCC;

@FunctionalInterface
public interface ProfessorCadastrarTemaTCCService {
    TemaTCC cadastrarTemaTCC(Long id, TemaTCCPostPutDto temaTCCPostPutDto);
}
