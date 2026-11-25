public class SmsNotificacao extends Notificacao {

    public SmsNotificacao(String titulo, String mensagem) {
        super(titulo, mensagem);
    }

    @Override
    public void enviar(Cadastro destinatario) {
        if (destinatario != null) {
            destinatario.atualizar("[SMS para " + destinatario.getNumeroTelefone() + "] " + titulo + ": " + mensagem);
        }
    }
}