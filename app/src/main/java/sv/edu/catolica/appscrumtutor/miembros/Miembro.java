package sv.edu.catolica.appscrumtutor.miembros;

public class Miembro {
    private Long idMiembro;
    private Long idProyecto;
    private Long idUsuario;
    private Long idRolProyecto;
    private String rolDescripcion;
    private String usuarioNombre;

    public Long getIdMiembro() { return idMiembro; }
    public Long getIdProyecto() { return idProyecto; }
    public Long getIdUsuario() { return idUsuario; }
    public Long getIdRolProyecto() { return idRolProyecto; }
    public String getRolDescripcion() { return rolDescripcion; }
    public String getUsuarioNombre() { return usuarioNombre; }
}
