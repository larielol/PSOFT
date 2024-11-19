package tccmanager.tccmanager.Service.ProfessorService;

import tccmanager.tccmanager.Dto.ProfessorPostPutDto;
import tccmanager.tccmanager.Model.Professor;

@FunctionalInterface
public interface ProfessorCadastrarService {
    Professor cadastrarProfessor(ProfessorPostPutDto professorPostPutDto);
}
