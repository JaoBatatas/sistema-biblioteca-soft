import java.util.Map;

public class MidiaFactory extends ItemAcervoFactory {

    @Override
    public ItemAcervo criarItemAcervo(Map<String, Object> dados) {
        int id = (int) dados.getOrDefault("id", 0);
        String titulo = (String) dados.get("titulo");
        String codigo = (String) dados.get("codigo");
        String autor = (String) dados.get("autor");
        String[] categorias = (String[]) dados.getOrDefault("categorias", new String[0]);
        int duracao = (int) dados.getOrDefault("duracao", 0);
        String[] idiomas = (String[]) dados.getOrDefault("idiomas", new String[0]);

        return new Midia(id, titulo, codigo, autor, categorias, duracao, idiomas);
    }
}