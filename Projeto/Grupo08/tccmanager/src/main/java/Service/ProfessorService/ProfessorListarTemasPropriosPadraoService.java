package tccmanager.tccmanager.Service.ProfessorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Model.AreaEstudo;
import tccmanager.tccmanager.Model.Professor;
import tccmanager.tccmanager.Model.TemaTCC;
import tccmanager.tccmanager.Repository.ProfessorRepository;
import tccmanager.tccmanager.Util.ProfessorUtil;

import java.util.Optional;

@Service
public class ProfessorListarTemasPropriosPadraoService implements ProfessorListarTemasPropriosService {

    @Autowired
    ProfessorRepository professorRepository;

    @Override
    public String listarTemasProprios(Long idProfessor) {
        ProfessorUtil.checkProfessorInexistente(idProfessor);
        Optional<Professor> professorOptional = professorRepository.findById(idProfessor);
        Professor professor = professorOptional.get();
        ProfessorUtil.checkProfessorSemTemasCadastrados(professor);

        String result = "";
        for (TemaTCC tema : professor.getTemasTCC()) {
            result += "Titulo: -----" + tema.getTitulo() + "-----\n" + "Areas de Estudo: ";
            for (AreaEstudo areaEstudo : tema.getAreasEstudo()){
                result += "-> " + areaEstudo.getNomeAreaEstudo() + "\n";
            }
        }
        return result;
    }
}

