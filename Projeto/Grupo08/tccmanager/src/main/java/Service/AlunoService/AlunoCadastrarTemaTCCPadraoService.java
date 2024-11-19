package tccmanager.tccmanager.Service.AlunoService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Dto.TemaTCCPostPutDto;
import tccmanager.tccmanager.Enum.StatusTemaTCC;
import tccmanager.tccmanager.Exception.TemaTCCException.TituloDuplicadoException;
import tccmanager.tccmanager.Model.Aluno;
import tccmanager.tccmanager.Model.TemaTCC;
import tccmanager.tccmanager.Repository.AlunoRepository;
import tccmanager.tccmanager.Repository.TemaTCCRepository;
import tccmanager.tccmanager.Util.AlunoUtil;
import tccmanager.tccmanager.Util.TemaTCCUtil;

import java.util.Optional;

@Service
public class AlunoCadastrarTemaTCCPadraoService implements AlunoCadastrarTemaTCCService {

    @Autowired
    ObjectMapper modelMapper;

    @Autowired
    AlunoRepository alunoRepository;

    @Autowired
    TemaTCCRepository temaTCCRepository;

    @Override
    public TemaTCC cadastrarTemaTCC(String matricula, TemaTCCPostPutDto temaTCCPostPutDto) {
        AlunoUtil.checkAlunoInexistente(matricula);
        Optional<Aluno> alunoOptional = alunoRepository.findByMatricula(matricula);
        Aluno aluno = alunoOptional.get();

        TemaTCCUtil.checkTituloDuplicado(temaTCCPostPutDto.getTitulo());
        TemaTCC temaTCC = modelMapper.convertValue(temaTCCPostPutDto, TemaTCC.class);
        temaTCC.setAluno(aluno);
        temaTCC.setStatus(StatusTemaTCC.NOVO);
        aluno.getTemasTCC().add(temaTCC);
        return temaTCCRepository.save(temaTCC);
    }
}
