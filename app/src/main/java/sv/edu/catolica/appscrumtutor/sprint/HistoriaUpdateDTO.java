package sv.edu.catolica.appscrumtutor.sprint;

public class HistoriaUpdateDTO {
    private String titulo;
    private String descripcion;
    private Integer idSprint;
    private Integer idPrioridad;
    private String criteriosAceptacion;
    private Integer idEstado;

    public HistoriaUpdateDTO(String titulo, String descripcion, Integer idSprint,
                             Integer idPrioridad, String criteriosAceptacion, Integer idEstado) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.idSprint = idSprint;
        this.idPrioridad = idPrioridad;
        this.criteriosAceptacion = criteriosAceptacion;
        this.idEstado = idEstado;
    }
}
