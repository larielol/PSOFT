public class UserStory {
    private String descricao;
    private Estado estado;


    public UserStory(String descricao) {
        this.descricao = descricao;
        this.estado = new EstadoToDo();
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void mover() {
        estado.mover(this);
    }

    public void verificar(boolean aprovada) {
        if (estado instanceof EstadoToVerify) {
            ((EstadoToVerify) estado).verificar(this, aprovada);
        } else {
            // Não está no estado para verificar
        }
    }
}
