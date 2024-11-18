package service;
// Interface para todos os métodos de pagamento (STRATEGY)
public interface MetodoDePagamento {
    double processarPagamento(double valor);
}