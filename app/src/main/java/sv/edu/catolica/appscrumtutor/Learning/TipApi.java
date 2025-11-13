package sv.edu.catolica.appscrumtutor.Learning;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TipApi {
    @GET("api/tips")
    Call<List<TipDTO>> listarTodos();

    @GET("api/tips/seccion/{nombre}")
    Call<List<TipDTO>> obtenerPorSeccion(@Path("nombre") String nombre);
}
