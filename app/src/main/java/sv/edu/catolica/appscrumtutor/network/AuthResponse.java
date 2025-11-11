package sv.edu.catolica.appscrumtutor.network;

public class AuthResponse {
    private String jwt;
    private String message;
    private String email;
    private String nombre;

    public String getJwt() { return jwt; }
    public String getMessage() { return message; }
    public String getEmail() { return email; }
    public String getNombre() { return nombre; }
}
