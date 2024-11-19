package tccmanager.tccmanager.Service.AreaEstudoService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Dto.AreaEstudoPostPutDto;
import tccmanager.tccmanager.Model.AreaEstudo;
import tccmanager.tccmanager.Repository.AreaEstudoRepository;
import tccmanager.tccmanager.Util.AreaEstudoUtil;

import java.util.Optional;

@Service
public class AreaEstudoAtualizarPadraoService implements AreaEstudoAtualizarService {

    @Autowired
    ObjectMapper modelMapper;

    @Autowired
    AreaEstudoRepository areaEstudoRepository;

    @Override
    public Optional<AreaEstudo> atualizarAreaEstudo(Long id, AreaEstudoPostPutDto areaEstudoPostPutDto) {
        AreaEstudoUtil.checkAreaEstudoInexistente(id);

        Optional<AreaEstudo> areaEstudoOptional = areaEstudoRepository.findById(id);
        AreaEstudo areaEstudo = areaEstudoOptional.get();
        String novoNome = areaEstudoPostPutDto.getNomeAreaEstudo();

        AreaEstudoUtil.checkAreaEstudoDuplicada(novoNome);

        areaEstudo.setNomeAreaEstudo(novoNome);

        return Optional.of(areaEstudoRepository.save(areaEstudo));
    }
}
