package sv.edu.catolica.appscrumtutor.sprint;

public class Historia {

    private Integer id;
    private Long idProyecto;
    private Integer idSprint; // CAMPO NUEVO QUE NECESITAS
    private String titulo;
    private String descripcion;
    private Integer idPrioridad;
    private String prioridad;
    private String criteriosAceptacion;
    private Integer idEstado;
    private String estado;
    private String fechaCreacion; // LocalDateTime  String para Retrofit

    public Integer getId() { return id; }
    public Long getIdProyecto() { return idProyecto; }
    public Integer getIdSprint() { return idSprint; } // GETTER NUEVO
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public Integer getIdPrioridad() { return idPrioridad; }
    public String getPrioridad() { return prioridad; }
    public String getCriteriosAceptacion() { return criteriosAceptacion; }
    public Integer getIdEstado() { return idEstado; }
    public String getEstado() { return estado; }
    public String getFechaCreacion() { return fechaCreacion; }
}
