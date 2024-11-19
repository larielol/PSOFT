package tccmanager.tccmanager.Service.ProfessorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Enum.StatusSolicitacao;
import tccmanager.tccmanager.Enum.StatusTemaTCC;
import tccmanager.tccmanager.Model.Aluno;
import tccmanager.tccmanager.Model.Solicitacao;
import tccmanager.tccmanager.Model.Professor;
import tccmanager.tccmanager.Model.TemaTCC;
import tccmanager.tccmanager.Repository.AlunoRepository;
import tccmanager.tccmanager.Repository.ProfessorRepository;
import tccmanager.tccmanager.Repository.SolicitacaoRepository;
import tccmanager.tccmanager.Repository.TemaTCCRepository;
import tccmanager.tccmanager.Util.AlunoUtil;
import tccmanager.tccmanager.Util.ProfessorUtil;
import tccmanager.tccmanager.Util.SolicitacaoUtil;

import java.util.Optional;

@Service
public class ProfessorAprovarSolicitacaoTemaCadastradoPorAlunoPadraoService implements ProfessorAprovarSolicitacaoTemaCadastradoPorAlunoService {
    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    AlunoRepository alunoRepository;

    @Autowired
    TemaTCCRepository temaTCCRepository;

    @Autowired
    SolicitacaoRepository solicitacaoRepository;

    @Override
    public void aprovarSolicitacaoTemaCadastradoPorAluno(Long idProfessor, String matricula, Long idSolicitacao){
        ProfessorUtil.checkProfessorInexistente(idProfessor);

        Optional<Professor> professorOptional = professorRepository.findById(idProfessor);
        Professor professor = professorOptional.get();

        AlunoUtil.checkAlunoInexistente(matricula);

        SolicitacaoUtil.checkSolicitacaoInexistente(idSolicitacao);

        Optional<Solicitacao> solicitacaoOptional = solicitacaoRepository.findById(idSolicitacao);
        Solicitacao solicitacao = solicitacaoOptional.get();

        Optional<Aluno> alunoOptional = alunoRepository.findByMatricula(matricula);
        Aluno aluno = alunoOptional.get();

        AlunoUtil.checkSolicitacaoDeTemaNaoCadastradaPeloAluno(aluno, solicitacao);
        AlunoUtil.checkTemaTCCNaoCadastradoPeloAluno(aluno, solicitacao.getTema());

        TemaTCC tema = solicitacao.getTema();
        tema.setStatus(StatusTemaTCC.ALOCADO);
        tema.setProfessor(professor);
        solicitacao.setStatus(StatusSolicitacao.APROVADA);
        temaTCCRepository.save(tema);
        solicitacaoRepository.save(solicitacao);
        System.out.println("Coordenador, \n O aluno " + solicitacao.getAluno().getNome() + " (" + matricula + ")" + " teve sua solicitação para o tema '" + solicitacao.getTema().getTitulo() + "' aprovada.");
    }
}
