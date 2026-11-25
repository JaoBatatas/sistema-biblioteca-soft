import java.util.Map;

public class LivroFactory extends ItemAcervoFactory {

    @Override
    public ItemAcervo criarItemAcervo(Map<String, Object> dados) {
        int id = (int) dados.getOrDefault("id", 0);
        String titulo = (String) dados.get("titulo");
        String codigo = (String) dados.get("codigo");
        String autor = (String) dados.get("autor");
        String[] categorias = (String[]) dados.getOrDefault("categorias", new String[0]);
        int nPaginas = (int) dados.getOrDefault("nPaginas", 0);
        String idioma = (String) dados.getOrDefault("idioma", "Português");

        return new Livro(id, titulo, codigo, autor, categorias, nPaginas, idioma);
    }
}