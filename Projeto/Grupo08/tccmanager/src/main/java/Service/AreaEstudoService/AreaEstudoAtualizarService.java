package tccmanager.tccmanager.Service.AreaEstudoService;

import tccmanager.tccmanager.Dto.AreaEstudoPostPutDto;
import tccmanager.tccmanager.Model.AreaEstudo;
import java.util.Optional;

@FunctionalInterface
public interface AreaEstudoAtualizarService {
    Optional<AreaEstudo> atualizarAreaEstudo(Long id, AreaEstudoPostPutDto areaEstudoPostPutDto);
}
