package sv.edu.catolica.appscrumtutor.Learning;

public class   TipDTO {

private Integer id;
private String seccion;
private String mensaje;

public TipDTO() {}

public TipDTO(Integer id, String seccion, String mensaje) {
    this.id = id;
    this.seccion = seccion;
    this.mensaje = mensaje;
}

public Integer getId() {
    return id;
}

public void setId(Integer id) {
    this.id = id;
}

public String getSeccion() {
    return seccion;
}

public void setSeccion(String seccion) {
    this.seccion = seccion;
}

public String getMensaje() {
    return mensaje;
}

public void setMensaje(String mensaje) {
    this.mensaje = mensaje;
}
}