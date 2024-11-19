package tccmanager.tccmanager.Service.AlunoService;

import tccmanager.tccmanager.Model.Professor;

import java.util.List;

@FunctionalInterface
public interface AlunoListarProfessoresService {
    List<Professor> listarProfessores(String matricula);
}