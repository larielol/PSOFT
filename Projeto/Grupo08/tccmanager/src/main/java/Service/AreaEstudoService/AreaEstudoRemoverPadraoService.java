package tccmanager.tccmanager.Service.AreaEstudoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Exception.AreaEstudoException.AreaEstudoInexistenteException;
import tccmanager.tccmanager.Model.AreaEstudo;
import tccmanager.tccmanager.Repository.AreaEstudoRepository;
import tccmanager.tccmanager.Util.AreaEstudoUtil;

import java.util.Optional;

@Service
public class AreaEstudoRemoverPadraoService implements AreaEstudoRemoverService {

    @Autowired
    AreaEstudoRepository areaEstudoRepository;

    @Override
    public void removerAreaEstudo(Long id) {
        AreaEstudoUtil.checkAreaEstudoInexistente(id);
        Optional<AreaEstudo> areaEstudoOptional = areaEstudoRepository.findById(id);
        AreaEstudo area = areaEstudoOptional.get();
        areaEstudoRepository.delete(area);
    }
}
