import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Acervo {
    private List<ItemAcervo> itens = new ArrayList<>();
    private Map<Integer, ItemAcervo> catalogoPorId = new HashMap<>();
    private List<Emprestimo> emprestimosRegistrados = new ArrayList<>();

    public void registrarItem(ItemAcervo item) {
        if (item != null) {
            catalogoPorId.put(item.getId(), item);
            if (!itens.contains(item)) {
                itens.add(item);
            }
        }
    }

    public void registrarEmprestimo(Emprestimo emprestimo) {
        if (emprestimo != null && !emprestimosRegistrados.contains(emprestimo)) {
            emprestimosRegistrados.add(emprestimo);
        }
    }

    public void adicionarItem(int item) {
        ItemAcervo encontrado = catalogoPorId.get(item);
        if (encontrado != null && !itens.contains(encontrado)) {
            itens.add(encontrado);
        }
    }

    public ItemAcervo editarItem(int item) {
        return buscaId(item);
    }

    public void removerItem(int id) {
        itens.removeIf(i -> i.getId() == id);
        catalogoPorId.remove(id);
    }

    public ItemAcervo[] buscaTitulo(String titulo) {
        return itens.stream()
                .filter(i -> i.getTitulo() != null && i.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                .toArray(ItemAcervo[]::new);
    }

    public ItemAcervo buscaAutor(String autor) {
        return itens.stream()
                .filter(i -> i.getAutor() != null && i.getAutor().equalsIgnoreCase(autor))
                .findFirst()
                .orElse(null);
    }

    public ItemAcervo buscaId(int id) {
        return itens.stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public ItemAcervo buscaCategoria(String categoria) {
        for (ItemAcervo item : itens) {
            if (item.getCategorias() != null) {
                for (String cat : item.getCategorias()) {
                    if (cat.equalsIgnoreCase(categoria)) {
                        return item;
                    }
                }
            }
        }
        return null;
    }

    public ItemAcervo buscaCodigo(String codigo) {
        return itens.stream()
                .filter(i -> i.getCodigo() != null && i.getCodigo().equalsIgnoreCase(codigo))
                .findFirst()
                .orElse(null);
    }

    public ItemAcervo[] relatorioAtrasados() {
        List<ItemAcervo> atrasados = new ArrayList<>();
        for (Emprestimo emp : emprestimosRegistrados) {
            if (emp.getStatus() == StatusEmprestimo.ATRASADO || emp.calcularAtraso() > 0) {
                for (ItemAcervo item : itens) {
                    if (item.getExemplares().contains(emp.getExemplar()) && !atrasados.contains(item)) {
                        atrasados.add(item);
                    }
                }
            }
        }
        return atrasados.toArray(new ItemAcervo[0]);
    }

    public ItemAcervo[] relatorioItensMaisEmprestados(int quantidade) {
        Map<ItemAcervo, Integer> contagem = new HashMap<>();
        for (ItemAcervo item : itens) {
            contagem.put(item, 0);
        }
        for (Emprestimo emp : emprestimosRegistrados) {
            for (ItemAcervo item : itens) {
                if (item.getExemplares().contains(emp.getExemplar())) {
                    contagem.put(item, contagem.get(item) + 1);
                }
            }
        }
        return contagem.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(quantidade)
                .map(Map.Entry::getKey)
                .toArray(ItemAcervo[]::new);
    }

    public Emprestimo[] relatorioMultasPendentes() {
        return emprestimosRegistrados.stream()
                .filter(e -> e.getMulta() != null && "PENDENTE".equalsIgnoreCase(e.getMulta().getStatus()))
                .toArray(Emprestimo[]::new);
    }
}