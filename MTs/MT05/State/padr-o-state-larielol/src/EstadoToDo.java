public class EstadoToDo implements Estado {
    @Override
    public void mover(UserStory userStory) {
        userStory.setEstado(new EstadoInProgress());
    }
}
