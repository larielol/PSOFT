package tccmanager.tccmanager.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tccmanager.tccmanager.Exception.AlunoException.AlunoInexistenteException;
import tccmanager.tccmanager.Exception.AlunoException.AlunoSemTemasCadastradosException;
import tccmanager.tccmanager.Exception.AlunoException.MatriculaDuplicadaException;
import tccmanager.tccmanager.Exception.EmailDuplicadoException;
import tccmanager.tccmanager.Exception.SolicitacaoException.SolicitacaoDeTemaNaoCadastradoPeloAlunoException;
import tccmanager.tccmanager.Exception.TemaTCCException.TemaTCCNaoCadastradoPeloAlunoException;
import tccmanager.tccmanager.Model.Aluno;
import tccmanager.tccmanager.Model.Solicitacao;
import tccmanager.tccmanager.Model.TemaTCC;
import tccmanager.tccmanager.Repository.AlunoRepository;

@Component
public class AlunoUtil {

    private static AlunoRepository alunoRepository;

    @Autowired
    public AlunoUtil(AlunoRepository repo) {
        alunoRepository = repo;
    }

    public static void checkAlunoInexistente(String matricula) {
        if (!alunoRepository.existsByMatricula(matricula)) {
            throw new AlunoInexistenteException("O aluno com essa matrícula não existe: " + matricula);
        }
    }

    public static void checkMatriculaDuplicada(String matricula) {
        if (alunoRepository.existsByMatricula(matricula)) {
            throw new MatriculaDuplicadaException("A matrícula já está em uso: " + matricula);
        }
    }

    public static void checkEmailDuplicado(String email) {
        if (alunoRepository.existsByEmail(email)) {
            throw new EmailDuplicadoException("O email já está em uso: " + email);
        }
    }

    public static void checkAlunoSemTemasCadastrados(Aluno aluno) {
        if (aluno.getTemasTCC().isEmpty()) {
            throw new AlunoSemTemasCadastradosException("O aluno " + aluno.getNome() + " não tem temas de TCC cadastrados.");
        }
    }

    public static void checkTemaTCCNaoCadastradoPeloAluno(Aluno aluno, TemaTCC temaTCC) {
        if (!aluno.getTemasTCC().contains(temaTCC)) {
            throw new TemaTCCNaoCadastradoPeloAlunoException("O tema de TCC indicado '" + temaTCC.getTitulo() + "' não foi cadastrado pelo aluno: " + aluno.getNome());
        }
    }

    public static void checkSolicitacaoDeTemaNaoCadastradaPeloAluno(Aluno aluno, Solicitacao solicitacao) {
        if (!solicitacao.getAluno().equals(aluno)) {
            throw new SolicitacaoDeTemaNaoCadastradoPeloAlunoException("O aluno com a matrícula " + aluno.getMatricula() + " não realizou a solicitação de id " + solicitacao.getId());
        }
    }
}
