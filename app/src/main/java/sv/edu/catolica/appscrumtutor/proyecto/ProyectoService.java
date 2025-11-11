package sv.edu.catolica.appscrumtutor.proyecto;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;
import retrofit2.http.Path;
import sv.edu.catolica.appscrumtutor.proyecto.Proyecto;
import sv.edu.catolica.appscrumtutor.proyecto.ProyectoCreateDTO;
import sv.edu.catolica.appscrumtutor.proyecto.ProyectoUpdateDTO;

public interface ProyectoService {

    // Obtener proyectos del usuario actual
    @GET("api/proyectos")
    Call<List<Proyecto>> getMisProyectos();

    // Crear un nuevo proyecto
    @POST("api/proyectos")
    Call<Proyecto> crearProyecto(@Body ProyectoCreateDTO nuevo);

    // Obtener por id
    @GET("api/proyectos/{id}")
    Call<Proyecto> getById(@Path("id") Long id);

    // Actualizar
    @PUT("api/proyectos/{id}")
    Call<Proyecto> updateProyecto(@Path("id") Long id, @Body ProyectoUpdateDTO dto);

    // Eliminar
    @DELETE("api/proyectos/{id}")
    Call<Void> deleteProyecto(@Path("id") Long id);


}
