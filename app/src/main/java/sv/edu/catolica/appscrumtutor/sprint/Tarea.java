package sv.edu.catolica.appscrumtutor.sprint;

public class Tarea {

    private int id;
    private int idSprint;
    private Integer idHistoria;
    private String titulo;
    private String descripcion;
    private int idEstado;
    private String estado;
    private Long idAsignadoA;
    private String asignadoANombre;
    private String fechaLimite;
    private String fechaCreacion;

    public int getId() { return id; }
    public int getIdSprint() { return idSprint; }
    public Integer getIdHistoria() { return idHistoria; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public int getIdEstado() { return idEstado; }
    public String getEstado() { return estado; }
    public Long getIdAsignadoA() { return idAsignadoA; }
    public String getAsignadoANombre() { return asignadoANombre; }
    public String getFechaLimite() { return fechaLimite; }
    public String getFechaCreacion() { return fechaCreacion; }

}
