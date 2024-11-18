import service.PagamentoBoleto;
import service.PagamentoCredito;
import service.PagamentoDebito;
import service.PagamentoPix;
import service.PagamentoService;

public class Main {
    public static void main(String[] args) {

        PagamentoService pagamentoService = new PagamentoService();

        // Pagamento por débito:
        pagamentoService.setMetodoDePagamento(new PagamentoDebito());
        pagamentoService.processarPagamento(500);
        System.out.println("============================================");

        // Pagamento por crédito:
        pagamentoService.setMetodoDePagamento(new PagamentoCredito());
        pagamentoService.processarPagamento(500);
        System.out.println("============================================");

        // Pagamento por pix:
        pagamentoService.setMetodoDePagamento(new PagamentoPix());
        pagamentoService.processarPagamento(50);
        System.out.println("============================================");

        // Pagamento por boleto:
        pagamentoService.setMetodoDePagamento(new PagamentoBoleto());
        pagamentoService.processarPagamento(500);
        System.out.println("============================================");
    }
}