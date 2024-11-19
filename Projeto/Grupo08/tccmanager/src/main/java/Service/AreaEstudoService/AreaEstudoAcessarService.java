package tccmanager.tccmanager.Service.AreaEstudoService;

import tccmanager.tccmanager.Model.AreaEstudo;

import java.util.Optional;

@FunctionalInterface
public interface AreaEstudoAcessarService {
    Optional<AreaEstudo> acessarAreaEstudo(Long id);
}
