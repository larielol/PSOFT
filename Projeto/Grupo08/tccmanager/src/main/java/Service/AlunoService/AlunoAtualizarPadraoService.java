package tccmanager.tccmanager.Service.AlunoService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Dto.AlunoPostPutDto;
import tccmanager.tccmanager.Model.Aluno;
import tccmanager.tccmanager.Repository.AlunoRepository;
import tccmanager.tccmanager.Util.AlunoUtil;

import java.util.Optional;

@Service
public class AlunoAtualizarPadraoService implements AlunoAtualizarService {

    @Autowired
    ObjectMapper modelMapper;

    @Autowired
    AlunoRepository alunoRepository;
    @Override
    public Optional<Aluno> atualizarAluno(String matricula, AlunoPostPutDto alunoPostPutDto) {
        AlunoUtil.checkAlunoInexistente(matricula);
        Optional<Aluno> alunoOptional = alunoRepository.findByMatricula(matricula);
        Aluno aluno = alunoOptional.get();
        String email = aluno.getEmail();
        String novaMatricula = alunoPostPutDto.getMatricula();
        String novoEmail = alunoPostPutDto.getEmail();

        if (!matricula.equals(novaMatricula)) {
            AlunoUtil.checkMatriculaDuplicada(novaMatricula);
        }
        if (!email.equals(novoEmail)) {
            AlunoUtil.checkEmailDuplicado(novoEmail);
        }

        aluno.setMatricula(novaMatricula);
        aluno.setEmail(novoEmail);
        aluno.setNome(alunoPostPutDto.getNome());
        aluno.setPeriodoConclusao(alunoPostPutDto.getPeriodoConclusao());

        return Optional.of(alunoRepository.save(aluno));
    }
}
