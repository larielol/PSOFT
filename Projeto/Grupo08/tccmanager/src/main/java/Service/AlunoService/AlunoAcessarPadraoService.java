package tccmanager.tccmanager.Service.AlunoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Model.Aluno;
import tccmanager.tccmanager.Repository.AlunoRepository;
import tccmanager.tccmanager.Util.AlunoUtil;

import java.util.Optional;

@Service
public class AlunoAcessarPadraoService implements AlunoAcessarService {

    @Autowired
    AlunoRepository alunoRepository;

    @Override
    public Optional<Aluno> acessarAluno(String matricula) {
        AlunoUtil.checkAlunoInexistente(matricula);
        return alunoRepository.findByMatricula(matricula);
    }
}
