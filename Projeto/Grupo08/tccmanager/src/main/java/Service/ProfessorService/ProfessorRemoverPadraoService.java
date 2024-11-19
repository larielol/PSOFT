package tccmanager.tccmanager.Service.ProfessorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Exception.ProfessorException.ProfessorInexistenteException;
import tccmanager.tccmanager.Model.Professor;
import tccmanager.tccmanager.Repository.ProfessorRepository;
import tccmanager.tccmanager.Util.ProfessorUtil;

import java.util.Optional;

@Service
public class ProfessorRemoverPadraoService implements ProfessorRemoverService {

    @Autowired
    ProfessorRepository professorRepository;

    @Override
    public void removerAluno(Long id) {
        ProfessorUtil.checkProfessorInexistente(id);
        Optional<Professor> professorOptional = professorRepository.findById(id);
        Professor professor = professorOptional.get();
        professorRepository.delete(professor);
    }
}
