package tccmanager.tccmanager.Service.CoordenadorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Enum.StatusTemaTCC;
import tccmanager.tccmanager.Model.Aluno;
import tccmanager.tccmanager.Model.AreaEstudo;
import tccmanager.tccmanager.Repository.AlunoRepository;
import tccmanager.tccmanager.Repository.TemaTCCRepository;
import tccmanager.tccmanager.Model.TemaTCC;

import java.util.List;
import java.util.Set;

@Service
public class CoordenadorGerarRelatorioPadraoService implements CoordenadorGerarRelatorioService {

    @Autowired
    AlunoRepository alunoRepository;

    @Autowired
    TemaTCCRepository temaTCCRepository;

    @Override
    public String gerarRelatorio() {
        StringBuilder result = new StringBuilder();

        if(!alunoRepository.findAll().isEmpty()) {
            result.append("=== Relatorio de Orientacoes de TCC ===\n");
            List<TemaTCC> todosOsTemas = temaTCCRepository.findAll();
            for (TemaTCC tema : todosOsTemas) {
                if (tema.getStatus().equals(StatusTemaTCC.ALOCADO)) {
                    result.append("Aluno: ").append(tema.getAluno().getNome()).append("\n");
                    result.append("Professor Orientador: ").append(tema.getProfessor().getNome()).append("\n");
                    result.append("Tema de TCC: ").append(tema.getTitulo()).append("\n");
                    result.append("Area de Estudo: ");
                    Set<AreaEstudo> areasEstudo = tema.getAreasEstudo();
                    for (AreaEstudo area : areasEstudo) {
                        result.append(area.getNomeAreaEstudo());
                        result.append(", ");
                    }
                    result.setLength(result.length() - 2);
                    result.append("\n\n");
                }
            }
            result.append("=== Alunos sem Orientador ===\n");
            for (TemaTCC tema : todosOsTemas) {
                if (!tema.getStatus().equals(StatusTemaTCC.ALOCADO) && tema.getProfessor() == null) {
                    result.append("Aluno: ").append(tema.getAluno().getNome()).append("\n");
                }
            }
            List<Aluno> todosOsAlunos = alunoRepository.findAll();
            for (Aluno aluno : todosOsAlunos) {
                if (aluno.getTemasTCC().isEmpty()) {
                    result.append("Aluno: ").append(aluno.getNome()).append("\n");
                }
            }
        } else {
            result.append("Sem dados de alunos no sistema.\n");
        }
        return result.toString();
    }
}
