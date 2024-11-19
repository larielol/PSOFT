package tccmanager.tccmanager.Service.ProfessorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Model.Aluno;
import tccmanager.tccmanager.Model.AreaEstudo;
import tccmanager.tccmanager.Model.TemaTCC;
import tccmanager.tccmanager.Repository.AlunoRepository;
import tccmanager.tccmanager.Repository.ProfessorRepository;
import tccmanager.tccmanager.Util.AlunoUtil;
import tccmanager.tccmanager.Util.ProfessorUtil;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfessorListarTemasCadastradosPorAlunosPadraoService implements ProfessorListarTemasCadastradosPorAlunosService {
    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    AlunoRepository alunoRepository;

    @Override
    public String listarTemasDosAlunos(Long idProfessor) {
        ProfessorUtil.checkProfessorInexistente(idProfessor);
        List<Aluno> alunosComTemasCadastrados = new ArrayList<>();
        List<Aluno> todosAlunos = alunoRepository.findAll();
        for (Aluno aluno : todosAlunos) {
            AlunoUtil.checkAlunoSemTemasCadastrados(aluno);
            alunosComTemasCadastrados.add(aluno);
        }

        String result = "";
        for (Aluno aluno : alunosComTemasCadastrados) {
            for (TemaTCC tema : aluno.getTemasTCC()) {
                result += "Titulo: -----" + tema.getTitulo() + "-----\n" + "Areas de Estudo: ";
                for (AreaEstudo areaEstudo : tema.getAreasEstudo()) {
                    result += "-> " + areaEstudo.getNomeAreaEstudo() + "\n";
                }
            }
        }
        return result;
    }
}
