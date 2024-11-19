package tccmanager.tccmanager.Service.AlunoService;

import tccmanager.tccmanager.Model.Aluno;

import java.util.Optional;

@FunctionalInterface
public interface AlunoAcessarService {
    Optional<Aluno> acessarAluno(String matricula);
}
