package tccmanager.tccmanager.Service.AlunoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Model.AreaEstudo;
import tccmanager.tccmanager.Model.Professor;
import tccmanager.tccmanager.Model.TemaTCC;
import tccmanager.tccmanager.Repository.ProfessorRepository;
import tccmanager.tccmanager.Util.ProfessorUtil;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlunoListarTemasCadastradosPorProfessorPadraoService implements AlunoListarTemasCadastradosPorProfessorService {

    @Autowired
    ProfessorRepository professorRepository;

    @Override
    public String listarTemasCadastradosPorProfessores() {
        String result = "";
        String areasEstudo = "Areas de Estudo: ";
        List<Professor> professoresComTemasCadastrados = new ArrayList<>();
        List<Professor> todosProfessores = professorRepository.findAll();

        for (Professor professor : todosProfessores) {
            ProfessorUtil.checkProfessorSemTemasCadastrados(professor);
            professoresComTemasCadastrados.add(professor);
        }

        for (Professor professor : professoresComTemasCadastrados) {
            for (TemaTCC tema : professor.getTemasTCC()) {
                result += "Titulo: -----" + tema.getTitulo() + "-----\n" + areasEstudo;
                for (AreaEstudo areaEstudo : tema.getAreasEstudo()){
                    result += "-> " + areaEstudo.getNomeAreaEstudo() + "\n";
                }
            }
            result += "Professor responsavel: " + professor.getNome() + "\n";
        }
        return result;
    }
}
