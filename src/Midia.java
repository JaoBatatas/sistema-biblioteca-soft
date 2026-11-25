public class Midia extends ItemAcervo {
    private int duracao;
    private String[] idiomas;

    public Midia() {
    }

    public Midia(int id, String titulo, String codigo, String autor, String[] categorias, int duracao, String[] idiomas) {
        super(id, titulo, codigo, autor, categorias);
        this.duracao = duracao;
        this.idiomas = idiomas;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public String[] getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(String[] idiomas) {
        this.idiomas = idiomas;
    }
}