package tccmanager.tccmanager.Service.AlunoService;

import tccmanager.tccmanager.Dto.TemaTCCPostPutDto;
import tccmanager.tccmanager.Model.TemaTCC;

@FunctionalInterface
public interface AlunoCadastrarTemaTCCService {
    TemaTCC cadastrarTemaTCC(String matricula, TemaTCCPostPutDto temaTCCPostPutDto);
}
