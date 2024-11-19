package tccmanager.tccmanager.Service.AreaEstudoService;

import tccmanager.tccmanager.Dto.AreaEstudoPostPutDto;
import tccmanager.tccmanager.Model.AreaEstudo;

@FunctionalInterface
public interface AreaEstudoCadastrarService {
    AreaEstudo cadastrarAreaEstudo (AreaEstudoPostPutDto areaEstudoPostPutDto);
}
