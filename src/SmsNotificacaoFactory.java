public class SmsNotificacaoFactory extends NotificacaoFactory {

    @Override
    public Notificacao criarNotificacao(String titulo, String mensagem) {
        return new SmsNotificacao(titulo, mensagem);
    }
}