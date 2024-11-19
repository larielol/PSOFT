package tccmanager.tccmanager.Service.AlunoService;

import tccmanager.tccmanager.Dto.SolicitacaoPostPutDto;
import tccmanager.tccmanager.Model.Solicitacao;

@FunctionalInterface
public interface AlunoSolicitarOrientacaoTemaProfessorService {
    Solicitacao solicitarOrientacaoTemaProfessor(String matricula, Long idProfessor, Long idTemaProfessor, SolicitacaoPostPutDto solicitacaoPostPutDto);
}
