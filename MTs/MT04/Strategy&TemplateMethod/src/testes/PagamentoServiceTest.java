package testes;

import org.junit.jupiter.api.Test;

import service.PagamentoBoleto;
import service.PagamentoCredito;
import service.PagamentoDebito;
import service.PagamentoPix;
import service.PagamentoService;

import static org.junit.jupiter.api.Assertions.*;

public class PagamentoServiceTest {

    // Testando pagamento no Debito com 500
    @Test
    void testPagamentoDebito() {
        PagamentoService pagamentoService = new PagamentoService();
        pagamentoService.setMetodoDePagamento(new PagamentoDebito());
        double valorFinal = pagamentoService.processarPagamento(500);
        assertEquals(500, valorFinal);
    }

    // Testando pagamento no Credito
    @Test
    void testPagamentoCredito() {
        PagamentoService pagamentoService = new PagamentoService();
        pagamentoService.setMetodoDePagamento(new PagamentoCredito());
        double valorFinal = pagamentoService.processarPagamento(1122);
        assertEquals(1234.2, valorFinal);
    }

    // Testando pagamento no Pix
    @Test
    void testePagamentoPix() {
        PagamentoService pagamentoService = new PagamentoService();
        pagamentoService.setMetodoDePagamento(new PagamentoPix());
        double valorFinal = pagamentoService.processarPagamento(600);
        assertEquals(540, valorFinal);
    }

    // Testando pagamento por Boleto
    @Test
    void testePagamentoBoleto() {
        PagamentoService pagamentoService = new PagamentoService();
        pagamentoService.setMetodoDePagamento(new PagamentoBoleto());
        double valorFinal = pagamentoService.processarPagamento(160);
        assertEquals(152, valorFinal);
    }
}
