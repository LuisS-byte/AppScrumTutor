package sv.edu.catolica.appscrumtutor.usuario;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import sv.edu.catolica.appscrumtutor.usuario.Usuario;

public interface UsuarioService {
    @GET("api/usuarios/buscar")
    Call<Usuario> getUsuarioPorCorreo(@Query("correo") String correo);
}
