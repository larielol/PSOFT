package tccmanager.tccmanager.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tccmanager.tccmanager.Exception.SolicitacaoException.SolicitacaoDeTemaNaoCadastradoPeloAlunoException;
import tccmanager.tccmanager.Exception.SolicitacaoException.SolicitacaoInexistenteException;
import tccmanager.tccmanager.Model.Solicitacao;
import tccmanager.tccmanager.Repository.SolicitacaoRepository;

@Component
public class SolicitacaoUtil {

    private static SolicitacaoRepository solicitacaoRepository;

    @Autowired
    public SolicitacaoUtil(SolicitacaoRepository repo) {
        solicitacaoRepository = repo;
    }

    public static void checkSolicitacaoInexistente(Long id) {
        if (!solicitacaoRepository.existsById(id)) {
            throw new SolicitacaoInexistenteException("A solicitação com esse id não existe: " + id);
        }
    }
}
