package sv.edu.catolica.appscrumtutor.miembros;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MiembroService {
    @GET("api/proyectos/{idProyecto}/miembros")
    Call<List<Miembro>> getMiembros(@Path("idProyecto") long idProyecto);
}
