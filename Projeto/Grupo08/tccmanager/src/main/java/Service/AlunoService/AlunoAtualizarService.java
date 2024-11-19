package tccmanager.tccmanager.Service.AlunoService;

import tccmanager.tccmanager.Dto.AlunoPostPutDto;
import tccmanager.tccmanager.Model.Aluno;

import java.util.Optional;

@FunctionalInterface
public interface AlunoAtualizarService {
    Optional<Aluno> atualizarAluno(String matricula, AlunoPostPutDto alunoPostPutDto);
}
