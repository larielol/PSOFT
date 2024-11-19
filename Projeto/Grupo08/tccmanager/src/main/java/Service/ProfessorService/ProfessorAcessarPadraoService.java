package tccmanager.tccmanager.Service.ProfessorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Model.Professor;
import tccmanager.tccmanager.Repository.ProfessorRepository;
import tccmanager.tccmanager.Util.ProfessorUtil;

import java.util.Optional;

@Service
public class ProfessorAcessarPadraoService implements ProfessorAcessarService {

    @Autowired
    ProfessorRepository professorRepository;
    @Override
    public Optional<Professor> acessarProfessor(Long id) {
        ProfessorUtil.checkProfessorInexistente(id);
        return professorRepository.findById(id);
    }
}
