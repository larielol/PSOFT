package tccmanager.tccmanager.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tccmanager.tccmanager.Exception.EmailDuplicadoException;
import tccmanager.tccmanager.Exception.ProfessorException.ProfessorInexistenteException;
import tccmanager.tccmanager.Exception.ProfessorException.ProfessorQuotaInvalidaException;
import tccmanager.tccmanager.Exception.ProfessorException.ProfessorSemTemasCadastradosException;
import tccmanager.tccmanager.Exception.SolicitacaoException.SolicitacaoDeTemaNaoCadastradoPeloProfessorException;
import tccmanager.tccmanager.Exception.TemaTCCException.TemaTCCNaoCadastradoPeloProfessorException;
import tccmanager.tccmanager.Model.Professor;
import tccmanager.tccmanager.Model.Solicitacao;
import tccmanager.tccmanager.Model.TemaTCC;
import tccmanager.tccmanager.Repository.ProfessorRepository;

@Component
public class ProfessorUtil {

    private static ProfessorRepository professorRepository;

    @Autowired
    public ProfessorUtil(ProfessorRepository repo) {
        professorRepository = repo;
    }

    public static void checkProfessorInexistente(Long id) {
        if (!professorRepository.existsById(id)) {
            throw new ProfessorInexistenteException("O professor com esse id não existe: " + id);
        }
    }

    public static void checkEmailDuplicado(String email) {
        if (professorRepository.existsByEmail(email)) {
            throw new EmailDuplicadoException("O email já está em uso: " + email);
        }
    }
    public static void checkProfessorSemTemasCadastrados(Professor professor) {
        if (professor.getTemasTCC().isEmpty()) {
            throw new ProfessorSemTemasCadastradosException("O professor " + professor.getNome() + " não tem temas de TCC cadastrados.");
        }
    }

    public static void checkProfessorQuotaInvalida(int quota) {
        if (quota < 0) {
            throw new ProfessorQuotaInvalidaException("A quota não pode ser negativa.");
        } else if (quota > Professor.MAX_QUOTA) {
            throw new ProfessorQuotaInvalidaException("Professor não está disponível.");
        }
    }

    public static void checkTemaTCCNaoCadastradoPeloProfessor(Professor professor, TemaTCC temaTCC) {
        if (!professor.getTemasTCC().contains(temaTCC)) {
            throw new TemaTCCNaoCadastradoPeloProfessorException("O tema de TCC indicado '" + temaTCC.getTitulo() + "' não foi cadastrado pelo professor: " + professor.getNome());
        }
    }

    public static void checkSolicitacaoDeTemaNaoCadastradaPeloProfessor(Professor professor, Solicitacao solicitacao) {
        if (!solicitacao.getProfessor().equals(professor)) {
            throw new SolicitacaoDeTemaNaoCadastradoPeloProfessorException("A solicitacao de id " + solicitacao.getId()+ " nao foi feita para o professor de id " + professor.getId());
        }
    }
}
