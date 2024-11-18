package service;
//Método de pagamento via cartão de débito

public class PagamentoDebito implements MetodoDePagamento {
    @Override
    public double processarPagamento(double valor) {
        System.out.println("Checando saldo da conta do usuário.");
        System.out.println("Realizando verificações de segurança.");
        System.out.println("Realizando pagamento via cartão de débito. Valor da compra = " + valor);
        System.out.println("Realizando atualização do saldo da conta.");
        System.out.println("Notifica compra concluída.");
        return valor;
    }
}