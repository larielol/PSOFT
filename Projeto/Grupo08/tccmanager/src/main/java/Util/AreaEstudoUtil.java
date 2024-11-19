package tccmanager.tccmanager.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tccmanager.tccmanager.Exception.AreaEstudoException.AreaEstudoDuplicadaException;
import tccmanager.tccmanager.Exception.AreaEstudoException.AreaEstudoInexistenteException;
import tccmanager.tccmanager.Repository.AreaEstudoRepository;

@Component
public class AreaEstudoUtil {

    private static AreaEstudoRepository areaEstudoRepository;

    @Autowired
    public AreaEstudoUtil(AreaEstudoRepository repo) {
        areaEstudoRepository = repo;
    }

    public static void checkAreaEstudoInexistente(Long id) {
        if (!areaEstudoRepository.existsById(id)) {
            throw new AreaEstudoInexistenteException("Essa área de estudo não existe: " + id);
        }
    }

    public static void checkAreaEstudoDuplicada(String nome) {
        if (areaEstudoRepository.existsByNomeAreaEstudo(nome)) {
            throw new AreaEstudoDuplicadaException("A área de estudo já existe :" + nome);
        }
    }
}
