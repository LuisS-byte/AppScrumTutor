package sv.edu.catolica.appscrumtutor.sprint;

public class SprintCreateDTO {
    private long idProyecto;
    private String nombre;
    private String objetivo;
    private String fechaInicio;
    private String fechaFin;
    private int idEstado;

    public SprintCreateDTO(long idProyecto, String nombre, String objetivo,
                           String fechaInicio, String fechaFin, int idEstado) {
        this.idProyecto = idProyecto;
        this.nombre = nombre;
        this.objetivo = objetivo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.idEstado = idEstado;
    }
}
