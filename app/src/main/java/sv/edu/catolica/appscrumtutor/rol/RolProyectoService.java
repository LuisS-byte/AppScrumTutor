package sv.edu.catolica.appscrumtutor.rol;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RolProyectoService {
    @GET("api/roles-proyecto")
    Call<List<RolProyecto>> getRolesProyecto();
}
