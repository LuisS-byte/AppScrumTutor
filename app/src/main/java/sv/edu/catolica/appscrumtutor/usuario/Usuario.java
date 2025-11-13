package sv.edu.catolica.appscrumtutor.usuario;

import com.google.gson.annotations.SerializedName;

public class Usuario {
    @SerializedName("id")
    private Long idUsuario;
    private String nombre;
    private String correo;
    private Rol rol;
    private boolean activo;
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Rol getRol() { return rol; }
    public boolean isActivo() { return activo; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    // Clase interna para el rol
    public static class Rol {
        private Long id;
        private String nombre;
        public Long getId() { return id; }
        public String getNombre() { return nombre; }
    }
}
