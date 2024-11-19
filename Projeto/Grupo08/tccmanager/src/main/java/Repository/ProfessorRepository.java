package tccmanager.tccmanager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tccmanager.tccmanager.Model.Professor;

import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    boolean existsByEmail(String email);

    boolean existsById(Long id);

    Optional<Professor> findById(Long id);
}
