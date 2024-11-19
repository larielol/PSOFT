package tccmanager.tccmanager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tccmanager.tccmanager.Model.Aluno;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno,Long> {
    boolean existsByMatricula(String matricula);
    boolean existsByEmail(String email);
    Optional<Aluno> findByMatricula(String matricula);
}
