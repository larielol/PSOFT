package service;
// Método de pagamento via cartão de crédito

public class PagamentoCredito implements MetodoDePagamento {
    @Override
    public double processarPagamento(double valor) {
        double valorFinal = valor * 1.1;
        System.out.println("Checando limite de crédito do usuário");
        System.out.println("Realizando verificações de segurança");
        System.out.println("Realizando pagamento via cartão de crédito. Valor da compra = " + valorFinal);
        System.out.println("Realizando atualização do limite de crédito.");
        System.out.println("Notifica compra concluída.");
        return valorFinal;
    }
}