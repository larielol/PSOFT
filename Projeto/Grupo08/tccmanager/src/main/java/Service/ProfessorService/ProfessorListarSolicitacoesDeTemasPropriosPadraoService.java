package tccmanager.tccmanager.Service.ProfessorService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Exception.ProfessorException.ProfessorSemSolicitacoesException;
import tccmanager.tccmanager.Model.Professor;
import tccmanager.tccmanager.Model.Solicitacao;
import tccmanager.tccmanager.Model.TemaTCC;
import tccmanager.tccmanager.Repository.ProfessorRepository;
import tccmanager.tccmanager.Repository.SolicitacaoRepository;
import tccmanager.tccmanager.Repository.TemaTCCRepository;
import tccmanager.tccmanager.Util.ProfessorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfessorListarSolicitacoesDeTemasPropriosPadraoService implements ProfessorListarSolicitacoesDeTemasPropriosService {

    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    SolicitacaoRepository solicitacaoRepository;

    @Autowired
    TemaTCCRepository temaTCCRepository;

    public String listarSolicitacoesDeTemasProprios(Long idProfessor){
        ProfessorUtil.checkProfessorInexistente(idProfessor);

        List<Solicitacao> todasAsSolicitacoes = solicitacaoRepository.findAll();

        List<TemaTCC> temasCadastradosPeloProfessor = new ArrayList<>();
        List<TemaTCC> todosOsTemas = temaTCCRepository.findAll();

        for(TemaTCC temaTCC : todosOsTemas) {
            if (temaTCC.getProfessor().getId().equals(idProfessor)){
                temasCadastradosPeloProfessor.add(temaTCC);
            }
        }

        Optional<Professor> professorOptional = professorRepository.findById(idProfessor);
        Professor professor = professorOptional.get();

        ProfessorUtil.checkProfessorSemTemasCadastrados(professor);

        String result = "";
        for (Solicitacao solicitacao : todasAsSolicitacoes) {
            if (temasCadastradosPeloProfessor.contains(solicitacao.getTema())) {
                result += "Solicitacao para orientacao do aluno: " + solicitacao.getAluno().getNome() +
                        "\n Tema de TCC solicitado: '" + solicitacao.getTema().getTitulo() + "'.\n";
            }
        }

        if (result.isEmpty()) {
            throw new ProfessorSemSolicitacoesException("Esse professor não tem solicitação de orientação para seus temas");
        }

        return result;
    }
}
