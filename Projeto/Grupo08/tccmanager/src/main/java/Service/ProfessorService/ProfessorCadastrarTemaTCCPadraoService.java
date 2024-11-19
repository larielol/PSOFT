package tccmanager.tccmanager.Service.ProfessorService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Dto.TemaTCCPostPutDto;
import tccmanager.tccmanager.Enum.StatusTemaTCC;
import tccmanager.tccmanager.Model.Aluno;
import tccmanager.tccmanager.Model.AreaEstudo;
import tccmanager.tccmanager.Model.Professor;
import tccmanager.tccmanager.Model.TemaTCC;
import tccmanager.tccmanager.Repository.AlunoRepository;
import tccmanager.tccmanager.Repository.ProfessorRepository;
import tccmanager.tccmanager.Repository.TemaTCCRepository;
import tccmanager.tccmanager.Util.ProfessorUtil;
import tccmanager.tccmanager.Util.TemaTCCUtil;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessorCadastrarTemaTCCPadraoService implements ProfessorCadastrarTemaTCCService {
    @Autowired
    ObjectMapper modelMapper;

    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    AlunoRepository alunoRepository;

    @Autowired
    TemaTCCRepository temaTCCRepository;

    @Override
    public TemaTCC cadastrarTemaTCC(Long id, TemaTCCPostPutDto temaTCCPostPutDto) {
        ProfessorUtil.checkProfessorInexistente(id);
        Optional<Professor> professorOptional = professorRepository.findById(id);
        Professor professor = professorOptional.get();

        TemaTCCUtil.checkTituloDuplicado(temaTCCPostPutDto.getTitulo());
        TemaTCC temaTCC = modelMapper.convertValue(temaTCCPostPutDto, TemaTCC.class);
        temaTCC.setProfessor(professor);
        temaTCC.setStatus(StatusTemaTCC.NOVO);

        notificarAlunos(temaTCC);
        return temaTCCRepository.save(temaTCC);
    }

    private void notificarAlunos(TemaTCC novoTemaTCC) {
        List<Aluno> alunos = alunoRepository.findAll();
        for (Aluno aluno : alunos) {
            for (AreaEstudo area : aluno.getAreasDeInteresse()) {
                if (novoTemaTCC.getAreasEstudo().contains(area)) {
                    System.out.println("\nAluno " + aluno.getNome()+ " ("+aluno.getMatricula()+"), " +
                            " você está recebendo essa notificação porque um professor esta disponivel para orientacao de TCC em uma área de seu interesse: " +
                            area.getNomeAreaEstudo());
                    break;
                }
            }
        }
    }
}

