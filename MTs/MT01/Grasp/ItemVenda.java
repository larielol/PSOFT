public class ItemVenda {
    private Produto produto;
    private int quantidade;
    private double valor;

    public ItemVenda(Produto produto, int quantidade, double valor) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.valor = valor;
    }

    public double calcularValorTotal() {
        if(quantidade >= 100) {
            valor = valor * 0.9;
        }
        return valor;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade() {
        this.quantidade = quantidade;
    }

    public double getValor() {
        return valor;
    }

    public void setValor() {
        this.valor = valor;
    }

    

}
