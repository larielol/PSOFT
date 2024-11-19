package tccmanager.tccmanager.Service.AreaEstudoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Model.AreaEstudo;
import tccmanager.tccmanager.Repository.AreaEstudoRepository;
import tccmanager.tccmanager.Util.AreaEstudoUtil;

import java.util.Optional;

@Service
public class AreaEstudoAcessarPadraoService implements AreaEstudoAcessarService {

    @Autowired
    AreaEstudoRepository areaEstudoRepository;

    @Override
    public Optional<AreaEstudo> acessarAreaEstudo(Long id) {
        AreaEstudoUtil.checkAreaEstudoInexistente(id);
        return areaEstudoRepository.findById(id);
    }
}
