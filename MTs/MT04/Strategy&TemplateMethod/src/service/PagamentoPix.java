package service;
// Método de pagamento via PIX

public class PagamentoPix implements MetodoDePagamento {
    @Override
    public double processarPagamento(double valor) {
        double valorFinal = valor * 0.9;
        System.out.println("Checando chave PIX do destinatário.");
        System.out.println("Realizando verificações de segurança.");
        System.out.println("Realizando pagamento via PIX. Valor da compra = " + valorFinal);
        System.out.println("Realizando atualização do saldo da conta.");
        System.out.println("Notifica compra concluída.");
        return valorFinal;
    }
}