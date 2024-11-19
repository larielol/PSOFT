package tccmanager.tccmanager.Service.ProfessorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Enum.StatusSolicitacao;
import tccmanager.tccmanager.Enum.StatusTemaTCC;
import tccmanager.tccmanager.Exception.AlunoException.AlunoInexistenteException;
import tccmanager.tccmanager.Exception.ProfessorException.ProfessorInexistenteException;
import tccmanager.tccmanager.Exception.SolicitacaoException.SolicitacaoDeTemaNaoCadastradoPeloProfessorException;
import tccmanager.tccmanager.Exception.SolicitacaoException.SolicitacaoInexistenteException;
import tccmanager.tccmanager.Exception.TemaTCCException.TemaTCCNaoCadastradoPeloProfessorException;
import tccmanager.tccmanager.Model.Aluno;
import tccmanager.tccmanager.Model.Solicitacao;
import tccmanager.tccmanager.Model.TemaTCC;
import tccmanager.tccmanager.Repository.AlunoRepository;
import tccmanager.tccmanager.Repository.ProfessorRepository;
import tccmanager.tccmanager.Repository.SolicitacaoRepository;
import tccmanager.tccmanager.Repository.TemaTCCRepository;
import java.util.Optional;

@Service
public class ProfessorAprovarSolicitacaoTemaProprioPadraoService implements ProfessorAprovarSolicitacaoTemaProprioService {
    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    AlunoRepository alunoRepository;

    @Autowired
    TemaTCCRepository temaTCCRepository;

    @Autowired
    SolicitacaoRepository solicitacaoRepository;

    @Override
    public void aprovarSolicitacaoTemaProprio(Long idProfessor, String matricula, Long idSolicitacao){
        if (!professorRepository.existsById(idProfessor)) {
            throw new ProfessorInexistenteException("O professor com esse id não existe: " + idProfessor);
        }

        if (!alunoRepository.existsByMatricula(matricula)) {
            throw new AlunoInexistenteException("O aluno com essa matricula não existe: " + matricula);
        }

        Optional<Aluno> alunoOptional = alunoRepository.findByMatricula(matricula);
        Aluno aluno = alunoOptional.get();

        if (!solicitacaoRepository.existsById(idSolicitacao)) {
            throw new SolicitacaoInexistenteException("A solicitação indicada não existe.");
        }

        Optional<Solicitacao> solicitacaoOptional = solicitacaoRepository.findById(idSolicitacao);
        Solicitacao solicitacao = solicitacaoOptional.get();

        if (!solicitacao.getProfessor().getId().equals(idProfessor)) {
            throw new SolicitacaoDeTemaNaoCadastradoPeloProfessorException("A solicitacao de id " + solicitacao.getId()+ " nao foi feita para o professor de id " + idProfessor);
        }

        if (!solicitacao.getTema().getProfessor().getId().equals(idProfessor)) {
            throw new TemaTCCNaoCadastradoPeloProfessorException("O professor de id " + idProfessor + " nao cadastrou o tema: " + solicitacao.getTema().getTitulo());
        }

        TemaTCC tema = solicitacao.getTema();
        tema.setStatus(StatusTemaTCC.ALOCADO);
        tema.setAluno(aluno);
        solicitacao.setStatus(StatusSolicitacao.APROVADA);
        temaTCCRepository.save(tema);
        solicitacaoRepository.save(solicitacao);
        System.out.println("Coordenador, \n O aluno " + solicitacao.getAluno().getNome() + " (" + matricula + ")" + " teve sua solicitação para o tema '" + solicitacao.getTema().getTitulo() + "', cadastrado pelo professor de id " + idProfessor + ", aprovada.");
    }
}
