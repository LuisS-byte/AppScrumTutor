package sv.edu.catolica.appscrumtutor.sprint;

public class Historia {

    private Integer id;                  // coincide con el campo del DTO
    private Long idProyecto;
    private String titulo;
    private String descripcion;
    private Integer idPrioridad;
    private String prioridad;
    private String criteriosAceptacion;
    private Integer idEstado;
    private String estado;
    private String fechaCreacion;        // Retrofit convierte LocalDateTime -> String

    // Getters
    public Integer getId() { return id; }
    public Long getIdProyecto() { return idProyecto; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public Integer getIdPrioridad() { return idPrioridad; }
    public String getPrioridad() { return prioridad; }
    public String getCriteriosAceptacion() { return criteriosAceptacion; }
    public Integer getIdEstado() { return idEstado; }
    public String getEstado() { return estado; }
    public String getFechaCreacion() { return fechaCreacion; }
}