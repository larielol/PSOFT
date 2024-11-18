public abstract class AppMensagemAbstract {
    protected AppDeMensagem appDeMensagem;

    public AppMensagemAbstract(AppDeMensagem appDeMensagem) {
        this.appDeMensagem = appDeMensagem;
    }

    public abstract void enviarMensagem(String destinatario);
}
