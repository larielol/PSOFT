package tccmanager.tccmanager.Service.ProfessorService;

@FunctionalInterface
public interface ProfessorAtualizarQuotaService {
    void atualizarQuota(Long id, int quota);
}