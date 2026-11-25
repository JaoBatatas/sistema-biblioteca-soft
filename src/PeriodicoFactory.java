import java.util.Map;

public class PeriodicoFactory extends ItemAcervoFactory {

    @Override
    public ItemAcervo criarItemAcervo(Map<String, Object> dados) {
        int id = (int) dados.getOrDefault("id", 0);
        String titulo = (String) dados.get("titulo");
        String codigo = (String) dados.get("codigo");
        String autor = (String) dados.get("autor");
        String[] categorias = (String[]) dados.getOrDefault("categorias", new String[0]);
        int nPaginas = (int) dados.getOrDefault("nPaginas", 0);
        int nEdicao = (int) dados.getOrDefault("nEdicao", 1);
        String tituloEdicao = (String) dados.getOrDefault("tituloEdicao", "");

        return new Periodico(id, titulo, codigo, autor, categorias, nPaginas, nEdicao, tituloEdicao);
    }
}