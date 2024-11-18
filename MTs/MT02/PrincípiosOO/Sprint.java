public class Sprint {
    
    private Funcionario desenvolvedor; 
    private Funcionario gerente;
    private Funcionario lider;

    public void defineEquipe(Funcionario g, Funcionario lider, Funcionario dev) {
        setGerente(gerente);
        setLider(lider); 
        setDesenvolvedor(dev);
    }

    private void setGerente(Funcionario gerente){
        this.gerente = gerente;
    }
    
    private void setLider(Funcionario lider){
        this.lider = lider;
    }
    
    private void setDesenvolvedor(Funcionario dev){
        this.desenvolvedor = dev;
    }

}
