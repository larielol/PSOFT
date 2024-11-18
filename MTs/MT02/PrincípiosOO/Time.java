import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Time {

    private List<Funcionario> funcionarios;

    //cria um array para alocar o time
    public Time() {
        ArrayList Funcionario = new ArrayList<>();
    }

    //aloca o time a partir dos funcionarios selecionados
    public void alocaMembroNaEquipe(Funcionario funcionario) {
        funcionarios.add(funcionario);
    }

    //retorna os funcionarios selecionados
    public List<Funcionario> getFuncionarios() {
        return funcionarios;
    }

}