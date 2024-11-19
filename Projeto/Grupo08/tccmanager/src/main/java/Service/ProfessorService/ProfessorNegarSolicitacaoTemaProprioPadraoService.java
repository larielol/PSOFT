package tccmanager.tccmanager.Service.ProfessorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Enum.StatusSolicitacao;
import tccmanager.tccmanager.Model.Professor;
import tccmanager.tccmanager.Model.Solicitacao;
import tccmanager.tccmanager.Repository.AlunoRepository;
import tccmanager.tccmanager.Repository.ProfessorRepository;
import tccmanager.tccmanager.Repository.SolicitacaoRepository;
import tccmanager.tccmanager.Repository.TemaTCCRepository;
import tccmanager.tccmanager.Util.AlunoUtil;
import tccmanager.tccmanager.Util.ProfessorUtil;
import tccmanager.tccmanager.Util.SolicitacaoUtil;

import java.util.Optional;

@Service
public class ProfessorNegarSolicitacaoTemaProprioPadraoService implements ProfessorNegarSolicitacaoTemaProprioService {
    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    AlunoRepository alunoRepository;

    @Autowired
    TemaTCCRepository temaTCCRepository;

    @Autowired
    SolicitacaoRepository solicitacaoRepository;

    @Override
    public void negarSolicitacaoTemaProprio(Long idProfessor, String matricula, Long idSolicitacao) {
        ProfessorUtil.checkProfessorInexistente(idProfessor);

        AlunoUtil.checkAlunoInexistente(matricula);

        SolicitacaoUtil.checkSolicitacaoInexistente(idSolicitacao);

        Optional<Solicitacao> solicitacaoOptional = solicitacaoRepository.findById(idSolicitacao);
        Solicitacao solicitacao = solicitacaoOptional.get();

        Optional<Professor> professorOptional = professorRepository.findById(idProfessor);
        Professor professor = professorOptional.get();

        ProfessorUtil.checkSolicitacaoDeTemaNaoCadastradaPeloProfessor(professor, solicitacao);

        ProfessorUtil.checkTemaTCCNaoCadastradoPeloProfessor(professor, solicitacao.getTema());

        solicitacao.setStatus(StatusSolicitacao.NEGADA);
        solicitacaoRepository.save(solicitacao);
        System.out.println("Aluno " + solicitacao.getAluno().getNome() + ",\nSua solicitacao para o tema '" + solicitacao.getTema().getTitulo() + "', cadastrado pelo professor de id " + idProfessor + ", foi negada.");
    }
}
