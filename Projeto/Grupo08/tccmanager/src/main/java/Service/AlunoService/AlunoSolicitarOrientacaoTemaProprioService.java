package tccmanager.tccmanager.Service.AlunoService;

import tccmanager.tccmanager.Dto.SolicitacaoPostPutDto;
import tccmanager.tccmanager.Model.Solicitacao;

@FunctionalInterface
public interface AlunoSolicitarOrientacaoTemaProprioService {
    Solicitacao solicitarOrientacaoTemaProprio(String matricula, Long idProfessor, Long idTema, SolicitacaoPostPutDto solicitacaoPostPutDto);
}
