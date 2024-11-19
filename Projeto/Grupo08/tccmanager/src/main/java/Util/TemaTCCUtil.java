package tccmanager.tccmanager.Util;

import org.springframework.stereotype.Component;
import tccmanager.tccmanager.Exception.TemaTCCException.TemaTCCNaoCadastradoPeloAlunoException;
import tccmanager.tccmanager.Exception.TemaTCCException.TituloDuplicadoException;
import tccmanager.tccmanager.Model.Aluno;
import tccmanager.tccmanager.Model.TemaTCC;
import tccmanager.tccmanager.Repository.TemaTCCRepository;

@Component
public class TemaTCCUtil {

    private static TemaTCCRepository temaTCCRepository;

    public TemaTCCUtil(TemaTCCRepository repo) {
        temaTCCRepository = repo;
    }

    public static void checkTemaTCCInexistente(Long id) {
        if (!temaTCCRepository.existsById(id)) {
            throw new TituloDuplicadoException("O tema de TCC indicado não existe");
        }
    }

    public static void checkTituloDuplicado(String titulo) {
        if (temaTCCRepository.existsByTitulo(titulo)) {
            throw new TituloDuplicadoException("O título do TCC já está em uso: " + titulo);
        }
    }
}
