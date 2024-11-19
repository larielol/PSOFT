package tccmanager.tccmanager.Service.AlunoService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Dto.SolicitacaoPostPutDto;
import tccmanager.tccmanager.Enum.StatusSolicitacao;
import tccmanager.tccmanager.Enum.StatusTemaTCC;
import tccmanager.tccmanager.Model.Aluno;
import tccmanager.tccmanager.Model.Professor;
import tccmanager.tccmanager.Model.Solicitacao;
import tccmanager.tccmanager.Model.TemaTCC;
import tccmanager.tccmanager.Repository.AlunoRepository;
import tccmanager.tccmanager.Repository.ProfessorRepository;
import tccmanager.tccmanager.Repository.SolicitacaoRepository;
import tccmanager.tccmanager.Repository.TemaTCCRepository;
import tccmanager.tccmanager.Util.AlunoUtil;
import tccmanager.tccmanager.Util.ProfessorUtil;
import tccmanager.tccmanager.Util.TemaTCCUtil;

import java.util.Optional;

@Service
public class AlunoSolicitarOrientacaoTemaProprioPadraoService implements AlunoSolicitarOrientacaoTemaProprioService {
    @Autowired
    ObjectMapper modelMapper;

    @Autowired
    AlunoRepository alunoRepository;

    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    TemaTCCRepository temaTCCRepository;

    @Autowired
    SolicitacaoRepository solicitacaoRepository;

    @Override
    public Solicitacao solicitarOrientacaoTemaProprio(String matricula, Long idProfessor, Long idTema, SolicitacaoPostPutDto solicitacaoPostPutDto) {
        AlunoUtil.checkAlunoInexistente(matricula);
        Optional<Aluno> alunoOptional = alunoRepository.findByMatricula(matricula);
        Aluno aluno = alunoOptional.get();

        ProfessorUtil.checkProfessorInexistente(idProfessor);
        Optional<Professor> professorOptional = professorRepository.findById(idProfessor);
        Professor professor = professorOptional.get();

        TemaTCCUtil.checkTemaTCCInexistente(idTema);
        Optional<TemaTCC> temaTCCOptional = temaTCCRepository.findById(idTema);
        TemaTCC temaTCC = temaTCCOptional.get();

        AlunoUtil.checkTemaTCCNaoCadastradoPeloAluno(aluno, temaTCC);

        Solicitacao solicitacao = modelMapper.convertValue(solicitacaoPostPutDto, Solicitacao.class);
        temaTCC.setStatus(StatusTemaTCC.PENDENTE);
        solicitacao.setAluno(aluno);
        solicitacao.setProfessor(professor);
        solicitacao.setTema(temaTCC);
        solicitacao.setStatus(StatusSolicitacao.EM_ANALISE);
        notificarProfessor(professor);
        return solicitacaoRepository.save(solicitacao);
    }

    private void notificarProfessor(Professor professor) {
        System.out.println("Enviando email para " + professor.getEmail() + ": " +
                "Professor(a) " + professor.getNome()+ ",\n" +
                "Você está recebendo essa notificação porque um aluno solicitou orientação em um tema de TCC");
    }
}
