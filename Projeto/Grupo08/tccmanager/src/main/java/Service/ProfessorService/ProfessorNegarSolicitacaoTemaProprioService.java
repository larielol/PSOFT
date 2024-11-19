package tccmanager.tccmanager.Service.ProfessorService;


@FunctionalInterface
public interface ProfessorNegarSolicitacaoTemaProprioService {

    void negarSolicitacaoTemaProprio(Long idProfessor, String matricula, Long idSolicitacao);
}
