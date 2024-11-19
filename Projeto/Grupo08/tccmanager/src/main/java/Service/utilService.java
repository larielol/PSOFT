package tccmanager.tccmanager.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Repository.*;


@Service
public class utilService {
    @Autowired
    AlunoRepository alunoRepository;

    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    TemaTCCRepository temaTCCRepository;

    @Autowired
    AreaEstudoRepository areaEstudoRepository;

    @Autowired
    SolicitacaoRepository solicitacaoRepository;
}
