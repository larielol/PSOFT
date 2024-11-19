package tccmanager.tccmanager.Service.ProfessorService;

import tccmanager.tccmanager.Dto.ProfessorPostPutDto;
import tccmanager.tccmanager.Model.Professor;
import java.util.Optional;

@FunctionalInterface
public interface ProfessorAtualizarService {
    Optional<Professor> atualizarProfessor(Long id, ProfessorPostPutDto professorPostPutDto);
}
