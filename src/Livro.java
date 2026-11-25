public class Livro extends ItemAcervo {
    private int nPaginas;
    private String idioma;

    public Livro() {
    }

    public Livro(int id, String titulo, String codigo, String autor, String[] categorias, int nPaginas, String idioma) {
        super(id, titulo, codigo, autor, categorias);
        this.nPaginas = nPaginas;
        this.idioma = idioma;
    }

    public int getNPaginas() {
        return nPaginas;
    }

    public void setNPaginas(int nPaginas) {
        this.nPaginas = nPaginas;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }
}