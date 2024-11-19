package tccmanager.tccmanager.Service.ProfessorService;


@FunctionalInterface
public interface ProfessorAprovarSolicitacaoTemaCadastradoPorAlunoService {

    void aprovarSolicitacaoTemaCadastradoPorAluno(Long idProfessor, String matricula, Long idSolicitacao);
}
