package sv.edu.catolica.appscrumtutor.proyecto;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NotificacionService {
    @GET("api/notificaciones/{idUsuario}")
    Call<List<Notificacion>> getByUsuario(@Path("idUsuario") long idUsuario);
    @POST("api/notificaciones")
    Call<Notificacion> crearNotificacion(@Body NotificacionCreateDTO dto);
}