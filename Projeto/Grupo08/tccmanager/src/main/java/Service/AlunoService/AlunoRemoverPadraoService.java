package tccmanager.tccmanager.Service.AlunoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Model.Aluno;
import tccmanager.tccmanager.Repository.AlunoRepository;
import tccmanager.tccmanager.Util.AlunoUtil;

import java.util.Optional;

@Service
public class AlunoRemoverPadraoService implements AlunoRemoverService {

    @Autowired
    AlunoRepository alunoRepository;

    @Override
    public void removerAluno(String matricula) {
        AlunoUtil.checkAlunoInexistente(matricula);
        Optional<Aluno> alunoOptional = alunoRepository.findByMatricula(matricula);
        Aluno aluno = alunoOptional.get();
        alunoRepository.delete(aluno);
    }
}
