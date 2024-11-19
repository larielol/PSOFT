package tccmanager.tccmanager.Service.AlunoService;

import tccmanager.tccmanager.Dto.AlunoPostPutDto;
import tccmanager.tccmanager.Model.Aluno;

@FunctionalInterface
public interface AlunoCadastrarService {
    Aluno cadastrarAluno(AlunoPostPutDto alunoPostPutDto);
}
