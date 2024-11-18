public class EstadoInProgress implements Estado {
    //user story nao pode ser movida para concluida sem aprovação
    @Override
    public void mover(UserStory userStory) {
        userStory.setEstado(new EstadoToVerify());
    }
}
