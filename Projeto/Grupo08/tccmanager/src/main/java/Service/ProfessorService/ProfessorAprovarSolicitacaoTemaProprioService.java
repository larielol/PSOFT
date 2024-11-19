package tccmanager.tccmanager.Service.ProfessorService;

@FunctionalInterface
public interface ProfessorAprovarSolicitacaoTemaProprioService {
    void aprovarSolicitacaoTemaProprio(Long idProfessor, String matricula, Long idSolicitacao);
}
