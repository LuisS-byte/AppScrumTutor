package sv.edu.catolica.appscrumtutor.net;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import sv.edu.catolica.appscrumtutor.net.dto.ProyectoDto;

public interface ApiService {
    @GET("api/proyectos")
    Call<List<ProyectoDto>> listarMisProyectos(@Header("Authorization") String token);
    @POST("api/auth/google")
    Call<String> loginConGoogle(@Body String idToken);

}
