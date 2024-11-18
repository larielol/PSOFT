public class EstadoToVerify implements Estado {

    @Override
    public void mover(UserStory userStory) {

    }

    public void verificar(UserStory userStory, boolean aprovada) {
        if(aprovada) {
            //aprovando e movendo para concluida
            userStory.setEstado(new EstadoDone());
        } else {
            //rejeitando e movendo para progresso novamente
            userStory.setEstado(new EstadoInProgress());
        }
    }
}
