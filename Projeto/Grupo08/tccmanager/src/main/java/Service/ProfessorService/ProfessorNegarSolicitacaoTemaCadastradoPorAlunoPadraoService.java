package tccmanager.tccmanager.Service.ProfessorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Enum.StatusSolicitacao;
import tccmanager.tccmanager.Enum.StatusTemaTCC;
import tccmanager.tccmanager.Model.Aluno;
import tccmanager.tccmanager.Model.Solicitacao;
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
public class ProfessorNegarSolicitacaoTemaCadastradoPorAlunoPadraoService implements ProfessorNegarSolicitacaoTemaCadastradoPorAlunoService {
    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    AlunoRepository alunoRepository;

    @Autowired
    TemaTCCRepository temaTCCRepository;

    @Autowired
    SolicitacaoRepository solicitacaoRepository;

    @Override
    public void negarSolicitacaoTemaCadastradoPorAluno(Long idProfessor, String matricula, Long idSolicitacao) {
        ProfessorUtil.checkProfessorInexistente(idProfessor);

        AlunoUtil.checkAlunoInexistente(matricula);

        SolicitacaoUtil.checkSolicitacaoInexistente(idSolicitacao);

        Optional<Solicitacao> solicitacaoOptional = solicitacaoRepository.findById(idSolicitacao);
        Solicitacao solicitacao = solicitacaoOptional.get();

        Optional<Aluno> alunoOptional = alunoRepository.findByMatricula(matricula);
        Aluno aluno = alunoOptional.get();

        AlunoUtil.checkSolicitacaoDeTemaNaoCadastradaPeloAluno(aluno, solicitacao);

        AlunoUtil.checkTemaTCCNaoCadastradoPeloAluno(aluno, solicitacao.getTema());

        TemaTCC tema = solicitacao.getTema();
        tema.setStatus(StatusTemaTCC.NOVO);
        solicitacao.setStatus(StatusSolicitacao.NEGADA);
        temaTCCRepository.save(tema);
        solicitacaoRepository.save(solicitacao);
        System.out.println("Aluno " + solicitacao.getAluno().getNome() + ",\nSua solicitação para o tema '" + solicitacao.getTema().getTitulo() + "' foi negada.");
    }
}
