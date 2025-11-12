package sv.edu.catolica.appscrumtutor.proyecto;

public class Notificacion {
    private int id;
    private long idUsuario;
    private int idTipo;
    private String mensaje;
    private boolean leido;
    private String fechaCreacion;

    public int getId() { return id; }
    public long getIdUsuario() { return idUsuario; }
    public int getIdTipo() { return idTipo; }
    public String getMensaje() { return mensaje; }
    public boolean isLeido() { return leido; }
    public String getFechaCreacion() { return fechaCreacion; }
}
