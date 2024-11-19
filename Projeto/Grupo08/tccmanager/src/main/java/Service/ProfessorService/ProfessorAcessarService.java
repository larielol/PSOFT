package tccmanager.tccmanager.Service.ProfessorService;

import tccmanager.tccmanager.Model.Professor;

import java.util.Optional;

@FunctionalInterface
public interface ProfessorAcessarService {
    Optional<Professor> acessarProfessor(Long id);
}
