package madstodolist.controller;

public class InvitacionData {
    private Long idEquipo;
    private Long idUsuario;

    public InvitacionData() {
        this.idEquipo = null;
        this.idUsuario = null;
    }

    public InvitacionData(Long idEquipo, Long idUsuario) {
        this.idEquipo = idEquipo;
        this.idUsuario = idUsuario;
    }

    public Long getIdEquipo() {
        return idEquipo;
    }
    public void setIdEquipo(Long idEquipo) {
        this.idEquipo = idEquipo;
    }
    public Long getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
}
