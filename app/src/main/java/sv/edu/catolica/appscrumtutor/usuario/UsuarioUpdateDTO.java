package sv.edu.catolica.appscrumtutor.usuario;

/**
 * DTO para actualizar los datos del usuario desde la app móvil.
 * Coincide con el backend: public record UsuarioUpdateDTO(String nombre, Long idRol)
 */
public class UsuarioUpdateDTO {

    private String nombre;
    private Long idRol;

    public UsuarioUpdateDTO(String nombre, Long idRol) {
        this.nombre = nombre;
        this.idRol = idRol;
    }

    public String getNombre() {
        return nombre;
    }

    public Long getIdRol() {
        return idRol;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }
}
