package tccmanager.tccmanager.Service.AlunoService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Dto.AlunoPostPutDto;
import tccmanager.tccmanager.Model.Aluno;
import tccmanager.tccmanager.Repository.AlunoRepository;
import tccmanager.tccmanager.Util.AlunoUtil;

@Service
public class AlunoCadastrarPadraoService implements AlunoCadastrarService {

    @Autowired
    ObjectMapper modelMapper;

    @Autowired
    AlunoRepository alunoRepository;
    @Override
    public Aluno cadastrarAluno(AlunoPostPutDto alunoPostPutDto) {

        AlunoUtil.checkMatriculaDuplicada(alunoPostPutDto.getMatricula());
        AlunoUtil.checkEmailDuplicado(alunoPostPutDto.getEmail());

        Aluno aluno = modelMapper.convertValue(alunoPostPutDto, Aluno.class);
        return alunoRepository.save(aluno);
    }
}
