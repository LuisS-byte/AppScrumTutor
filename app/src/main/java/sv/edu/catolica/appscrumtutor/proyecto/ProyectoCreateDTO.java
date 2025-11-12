package sv.edu.catolica.appscrumtutor.proyecto;

public class ProyectoCreateDTO {
    private String nombre;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;

    public ProyectoCreateDTO(String nombre, String descripcion, String fechaInicio, String fechaFin) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getFechaInicio() { return fechaInicio; }
    public String getFechaFin() { return fechaFin; }
}
