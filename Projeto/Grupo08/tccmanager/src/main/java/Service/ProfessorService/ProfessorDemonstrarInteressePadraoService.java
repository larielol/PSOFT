package tccmanager.tccmanager.Service.ProfessorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Model.AreaEstudo;
import tccmanager.tccmanager.Model.Professor;
import tccmanager.tccmanager.Repository.AreaEstudoRepository;
import tccmanager.tccmanager.Repository.ProfessorRepository;
import tccmanager.tccmanager.Util.AreaEstudoUtil;
import tccmanager.tccmanager.Util.ProfessorUtil;

import java.util.Optional;
import java.util.Set;

@Service
public class ProfessorDemonstrarInteressePadraoService implements ProfessorDemonstrarInteresseService {

    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    AreaEstudoRepository areaEstudoRepository;

    @Override
    public void demonstrarInteresse(Long idProfessor, Long idArea) {

        AreaEstudoUtil.checkAreaEstudoInexistente(idArea);
        Optional<AreaEstudo> areaEstudoOptional = areaEstudoRepository.findById(idArea);
        AreaEstudo areaDeInteresse = areaEstudoOptional.get();

        ProfessorUtil.checkProfessorInexistente(idProfessor);
        Optional<Professor> professorOptional = professorRepository.findById(idProfessor);
        Professor professorInteressado = professorOptional.get();

        Set<AreaEstudo> areasDeInteresse = professorInteressado.getAreasDeInteresse();
        areasDeInteresse.add(areaDeInteresse);
        professorInteressado.setAreasDeInteresse(areasDeInteresse);

        professorRepository.save(professorInteressado);
    }
}
