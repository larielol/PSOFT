package tccmanager.tccmanager.Service.ProfessorService;


@FunctionalInterface
public interface ProfessorNegarSolicitacaoTemaCadastradoPorAlunoService {
    void negarSolicitacaoTemaCadastradoPorAluno(Long idProfessor, String matricula, Long idSolicitacao);
}
