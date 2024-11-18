package service;
// Método de pagamento via boleto

public class PagamentoBoleto implements MetodoDePagamento {
    @Override
    public double processarPagamento(double valor) {
        double valorFinal = valor * 0.95;
        System.out.println("Checando chave Boleto do destinatário.");
        System.out.println("Realizando verificações de segurança.");
        System.out.println("Realizando pagamento via Boleto. Valor da compra = " + valorFinal);
        System.out.println("Realizando agendamento para atualização do saldo.");
        System.out.println("Notifica compra concluída.");
        return valorFinal;
    }
}