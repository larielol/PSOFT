package tccmanager.tccmanager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tccmanager.tccmanager.Model.Coordenador;

@Repository
public interface CoordenadorRepository extends JpaRepository<Coordenador, Long> {

}
