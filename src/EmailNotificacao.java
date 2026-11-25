public class EmailNotificacao extends Notificacao {

    public EmailNotificacao(String titulo, String mensagem) {
        super(titulo, mensagem);
    }

    @Override
    public void enviar(Cadastro destinatario) {
        if (destinatario != null) {
            destinatario.atualizar("[E-MAIL para " + destinatario.getEmail() + "] " + titulo + ": " + mensagem);
        }
    }
}