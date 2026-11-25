public abstract class Notificacao {
    protected String titulo;
    protected String mensagem;

    protected Notificacao(String titulo, String mensagem) {
        this.titulo = titulo;
        this.mensagem = mensagem;
    }

    public abstract void enviar(Cadastro destinatario);

    public String getTitulo() {
        return titulo;
    }

    public String getMensagem() {
        return mensagem;
    }
}