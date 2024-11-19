package tccmanager.tccmanager.Service.ProfessorService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Dto.ProfessorPostPutDto;
import tccmanager.tccmanager.Model.Professor;
import tccmanager.tccmanager.Repository.ProfessorRepository;
import tccmanager.tccmanager.Util.ProfessorUtil;

@Service
public class ProfessorCadastrarPadraoService implements ProfessorCadastrarService {

    @Autowired
    ObjectMapper modelMapper;

    @Autowired
    ProfessorRepository professorRepository;

    @Override
    public Professor cadastrarProfessor(ProfessorPostPutDto professorPostPutDto) {
        ProfessorUtil.checkEmailDuplicado(professorPostPutDto.getEmail());
        Professor professor = modelMapper.convertValue(professorPostPutDto, Professor.class);
        return professorRepository.save(professor);
    }
}
