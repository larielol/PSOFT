package service;

public class PagamentoService {
    private MetodoDePagamento metodoDePagamento;

    // Método para definir o método de pagamento a ser escolhido
    // que será utilizado, this. é como como um argumento de construtor
    // passando como argumento somente o método a ser executado
    public void setMetodoDePagamento(MetodoDePagamento metodoDePagamento) {
        this.metodoDePagamento = metodoDePagamento;
    }
    
    // Método para escolher o pagamento utilizando a estratégia
    // de acordo com a escolha do usuario
    public double processarPagamento(double valor) {
        if (metodoDePagamento == null) {
            throw new IllegalStateException("Método de pagamento não reconhecido.");
        }
        return metodoDePagamento.processarPagamento(valor);    
    }
}