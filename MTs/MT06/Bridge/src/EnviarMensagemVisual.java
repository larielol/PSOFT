public class EnviarMensagemVisual extends AppMensagemAbstract {
    public EnviarMensagemVisual(AppDeMensagem appDeMensagem) {
        super(appDeMensagem);
    }

    public void enviarMensagem(String destinatario) {
        Mensagem mensagemVisual = new MensagemVisual();
        appDeMensagem.enviarMensagem(mensagemVisual, destinatario);
    }
}