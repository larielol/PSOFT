public class Produto {
    private String nome;
    private double valor;

    public Produto(String nome, double valor){
        this.nome = nome;
        this.valor = valor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome() {
        this.nome = nome;
    }
    
    public double getValor() {
        return valor;
    }
    
    public void setValor() {
        this.valor = valor;
    }

}
