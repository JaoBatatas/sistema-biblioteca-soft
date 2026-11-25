public class Periodico extends ItemAcervo {
    private int nPaginas;
    private int nEdicao;
    private String tituloEdicao;

    public Periodico() {
    }

    public Periodico(int id, String titulo, String codigo, String autor, String[] categorias, int nPaginas, int nEdicao, String tituloEdicao) {
        super(id, titulo, codigo, autor, categorias);
        this.nPaginas = nPaginas;
        this.nEdicao = nEdicao;
        this.tituloEdicao = tituloEdicao;
    }

    public int getNPaginas() {
        return nPaginas;
    }

    public void setNPaginas(int nPaginas) {
        this.nPaginas = nPaginas;
    }

    public int getNEdicao() {
        return nEdicao;
    }

    public void setNEdicao(int nEdicao) {
        this.nEdicao = nEdicao;
    }

    public String getTituloEdicao() {
        return tituloEdicao;
    }

    public void setTituloEdicao(String tituloEdicao) {
        this.tituloEdicao = tituloEdicao;
    }
}