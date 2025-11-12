package sv.edu.catolica.appscrumtutor.rol;

import com.google.gson.annotations.SerializedName;

public class RolProyecto {

    @SerializedName("id")
    private long idRolProyecto;

    @SerializedName("descripcion")
    private String descripcion;

    public long getIdRolProyecto() { return idRolProyecto; }
    public void setIdRolProyecto(Long idRolProyecto) { this.idRolProyecto = idRolProyecto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
