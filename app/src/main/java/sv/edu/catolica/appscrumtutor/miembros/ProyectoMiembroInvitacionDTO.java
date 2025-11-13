package sv.edu.catolica.appscrumtutor.miembros;

public class ProyectoMiembroInvitacionDTO {
    private Boolean invitacion;

    public ProyectoMiembroInvitacionDTO(Boolean invitacion) {
        this.invitacion = invitacion;
    }

    public Boolean getInvitacion() { return invitacion; }
    public void setInvitacion(Boolean invitacion) { this.invitacion = invitacion; }
}
