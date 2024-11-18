import java.util.List;
import java.util.ArrayList;

public class Venda {
    private List<ItemVenda> itens;

    public Venda() {
        this.itens = new ArrayList<>();
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }

    public double calcularValorTotalVenda() {
        double totalVenda = 0;
        for(ItemVenda item : itens) {
            totalVenda += item.calcularValorTotalItem();
        }
        return totalVenda;
    }
}