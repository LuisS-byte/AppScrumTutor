package sv.edu.catolica.appscrumtutor.rol;


import com.google.gson.annotations.SerializedName;

public class Rol {

    @SerializedName("id")
    private Long idRol;
    private String nombre;

    public Long getIdRol() { return idRol; }
    public void setIdRol(Long idRol) { this.idRol = idRol; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
