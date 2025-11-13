package sv.edu.catolica.appscrumtutor.rol;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RolService {
    @GET("api/roles")
    Call<List<Rol>> getRoles();
}
