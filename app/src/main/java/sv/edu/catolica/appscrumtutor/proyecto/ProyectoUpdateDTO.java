package sv.edu.catolica.appscrumtutor.proyecto;

public class ProyectoUpdateDTO {


    private String nombre;
    private String descripcion;

    public ProyectoUpdateDTO(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }

}
