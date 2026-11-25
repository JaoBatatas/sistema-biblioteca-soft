public class EmailNotificacaoFactory extends NotificacaoFactory {

    @Override
    public Notificacao criarNotificacao(String titulo, String mensagem) {
        return new EmailNotificacao(titulo, mensagem);
    }
}