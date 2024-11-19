package tccmanager.tccmanager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tccmanager.tccmanager.Model.TemaTCC;

import java.util.Optional;

@Repository
public interface TemaTCCRepository extends JpaRepository<TemaTCC,Long>{
    boolean existsByTitulo(String titulo);

    Optional<TemaTCC> findByTitulo(String titulo);
}
