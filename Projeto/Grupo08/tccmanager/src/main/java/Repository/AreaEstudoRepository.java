package tccmanager.tccmanager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tccmanager.tccmanager.Model.AreaEstudo;

import java.util.Optional;

@Repository
public interface AreaEstudoRepository extends JpaRepository<AreaEstudo, Long> {

    boolean existsById(Long id);

    boolean existsByNomeAreaEstudo(String nome);

    Optional<AreaEstudo> findById(Long id);
}
