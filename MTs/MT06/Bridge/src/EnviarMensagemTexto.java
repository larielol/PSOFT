public class EnviarMensagemTexto extends AppMensagemAbstract {
    public EnviarMensagemTexto(AppDeMensagem appDeMensagem) {
        super(appDeMensagem);
    }

    public void enviarMensagem(String destinatario) {
        Mensagem mensagemDeTexto = new MensagemDeTexto();
        appDeMensagem.enviarMensagem(mensagemDeTexto, destinatario);
    }
}
