package tccmanager.tccmanager.Service.AlunoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Model.Aluno;
import tccmanager.tccmanager.Model.AreaEstudo;
import tccmanager.tccmanager.Model.Professor;
import tccmanager.tccmanager.Repository.AlunoRepository;
import tccmanager.tccmanager.Repository.ProfessorRepository;
import tccmanager.tccmanager.Util.AlunoUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AlunoListarProfessoresPadraoService implements AlunoListarProfessoresService {

    @Autowired
    AlunoRepository alunoRepository;

    @Autowired
    ProfessorRepository professorRepository;

    @Override
    public List<Professor> listarProfessores(String matricula) {
        AlunoUtil.checkAlunoInexistente(matricula);
        Optional<Aluno> alunoOptional = alunoRepository.findByMatricula(matricula);
        Aluno aluno = alunoOptional.get();
        Set<AreaEstudo> areasDeInteresseAluno = aluno.getAreasDeInteresse();

        List<Professor> professoresDisponiveis = new ArrayList<>();
        List<Professor> todosProfessores = professorRepository.findAll();

        for (Professor professor : todosProfessores) {
            if (professor.getQuota() > 0 && temInteresse(professor, areasDeInteresseAluno)) {
                professoresDisponiveis.add(professor);
            }
        }
        return professoresDisponiveis;
    }

    private boolean temInteresse(Professor professor, Set<AreaEstudo> areasDeInteresseAluno) {
        Set<Long> idsAreasAluno = areasDeInteresseAluno.stream().map(AreaEstudo::getId).collect(Collectors.toSet());
        Set<Long> idsAreasProfessor = professor.getAreasDeInteresse().stream().map(AreaEstudo::getId).collect(Collectors.toSet());

        return idsAreasAluno.stream().anyMatch(idsAreasProfessor::contains);
    }
}