package tccmanager.tccmanager.Service.AreaEstudoService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Dto.AreaEstudoPostPutDto;
import tccmanager.tccmanager.Model.AreaEstudo;
import tccmanager.tccmanager.Repository.AreaEstudoRepository;
import tccmanager.tccmanager.Util.AreaEstudoUtil;

@Service
public class AreaEstudoCadastrarPadraoService implements AreaEstudoCadastrarService {

    @Autowired
    ObjectMapper modelMapper;

    @Autowired
    AreaEstudoRepository areaEstudoRepository;

    @Override
    public AreaEstudo cadastrarAreaEstudo(AreaEstudoPostPutDto areaEstudoPostPutDto) {
        AreaEstudoUtil.checkAreaEstudoDuplicada(areaEstudoPostPutDto.getNomeAreaEstudo());

        AreaEstudo areaEstudo = modelMapper.convertValue(areaEstudoPostPutDto, AreaEstudo.class);
        return areaEstudoRepository.save(areaEstudo);
    }
}
