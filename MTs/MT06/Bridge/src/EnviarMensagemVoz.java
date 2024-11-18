public class EnviarMensagemVoz extends AppMensagemAbstract {
    public EnviarMensagemVoz(AppDeMensagem appDeMensagem) {
        super(appDeMensagem);
    }

    public void enviarMensagem(String destinatario) {
        Mensagem mensagemDeVoz = new MensagemDeVoz();
        appDeMensagem.enviarMensagem(mensagemDeVoz, destinatario);
    }
}