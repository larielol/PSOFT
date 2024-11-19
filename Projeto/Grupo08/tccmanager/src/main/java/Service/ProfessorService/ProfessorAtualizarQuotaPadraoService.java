package tccmanager.tccmanager.Service.ProfessorService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import tccmanager.tccmanager.Model.Professor;
import tccmanager.tccmanager.Repository.ProfessorRepository;
import tccmanager.tccmanager.Util.ProfessorUtil;

import java.util.Optional;

@Service
public class ProfessorAtualizarQuotaPadraoService implements ProfessorAtualizarQuotaService {

    @Autowired
    ProfessorRepository professorRepository;

    public void atualizarQuota(Long idProfessor, int novaQuota) {
        ProfessorUtil.checkProfessorInexistente(idProfessor);

        Optional<Professor> professorOptional = professorRepository.findById(idProfessor);
        Professor professor = professorOptional.get();

        ProfessorUtil.checkProfessorQuotaInvalida(novaQuota);
            professor.setQuota(novaQuota);

        professorRepository.save(professor);
    }
}