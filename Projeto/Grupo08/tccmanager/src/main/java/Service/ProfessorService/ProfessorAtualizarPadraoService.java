package tccmanager.tccmanager.Service.ProfessorService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Dto.ProfessorPostPutDto;
import tccmanager.tccmanager.Model.Professor;
import tccmanager.tccmanager.Repository.ProfessorRepository;
import tccmanager.tccmanager.Util.ProfessorUtil;

import java.util.Optional;

@Service
public class ProfessorAtualizarPadraoService implements ProfessorAtualizarService {

    @Autowired
    ObjectMapper modelMapper;

    @Autowired
    ProfessorRepository professorRepository;
    @Override
    public Optional<Professor> atualizarProfessor(Long id, ProfessorPostPutDto professorPostPutDto) {
        ProfessorUtil.checkProfessorInexistente(id);

        Optional<Professor> professorOptional = professorRepository.findById(id);
        Professor professor = professorOptional.get();
        String novoEmail = professorPostPutDto.getEmail();

        if (!(professor.getEmail().equals(novoEmail))) {
            ProfessorUtil.checkEmailDuplicado(novoEmail);
        }

        professor.setNome(professorPostPutDto.getNome());
        professor.setEmail(novoEmail);
        professor.setLaboratorios(professorPostPutDto.getLaboratorios());

        return Optional.of(professorRepository.save(professor));
    }
}
