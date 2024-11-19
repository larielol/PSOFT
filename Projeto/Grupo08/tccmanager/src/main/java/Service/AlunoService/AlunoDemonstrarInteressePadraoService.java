package tccmanager.tccmanager.Service.AlunoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Model.Aluno;
import tccmanager.tccmanager.Model.AreaEstudo;
import tccmanager.tccmanager.Repository.AlunoRepository;
import tccmanager.tccmanager.Repository.AreaEstudoRepository;
import tccmanager.tccmanager.Util.AlunoUtil;
import tccmanager.tccmanager.Util.AreaEstudoUtil;


import java.util.Optional;
import java.util.Set;

@Service
public class AlunoDemonstrarInteressePadraoService implements AlunoDemonstrarInteresseService {

    @Autowired
    AreaEstudoRepository areaEstudoRepository;

    @Autowired
    AlunoRepository alunoRepository;

    @Override
    public void demonstrarInteresse(String matriculaAluno, Long idArea) {

        AreaEstudoUtil.checkAreaEstudoInexistente(idArea);
        Optional<AreaEstudo> areaEstudoOptional = areaEstudoRepository.findById(idArea);
        AreaEstudo areaDeInteresse = areaEstudoOptional.get();

        AlunoUtil.checkAlunoInexistente(matriculaAluno);

        Optional<Aluno> alunoOptional = alunoRepository.findByMatricula(matriculaAluno);
        Aluno alunoInteressado = alunoOptional.get();

        Set<AreaEstudo> areasDeInteresse = alunoInteressado.getAreasDeInteresse();
        areasDeInteresse.add(areaDeInteresse);
        alunoInteressado.setAreasDeInteresse(areasDeInteresse);

        alunoRepository.save(alunoInteressado);
    }
}