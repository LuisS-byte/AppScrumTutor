package sv.edu.catolica.appscrumtutor.sprint;

public class HistoriaCreateDTO {
    private Long idProyecto;
    private Integer idSprint; // puede ser null
    private String titulo;
    private String descripcion;
    private Integer idPrioridad;
    private String criteriosAceptacion;
    private Integer idEstado;

    public HistoriaCreateDTO(Long idProyecto, Integer idSprint, String titulo,
                             String descripcion, Integer idPrioridad,
                             String criteriosAceptacion, Integer idEstado) {
        this.idProyecto = idProyecto;
        this.idSprint = idSprint;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.idPrioridad = idPrioridad;
        this.criteriosAceptacion = criteriosAceptacion;
        this.idEstado = idEstado;
    }
}
