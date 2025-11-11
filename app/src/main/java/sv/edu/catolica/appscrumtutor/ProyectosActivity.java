package sv.edu.catolica.appscrumtutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import sv.edu.catolica.appscrumtutor.network.ApiClient;
import sv.edu.catolica.appscrumtutor.proyecto.Proyecto;
import sv.edu.catolica.appscrumtutor.proyecto.ProyectoService;

public class ProyectosActivity extends AppCompatActivity {


    LinearLayout layoutProjects;
    Button btnCrearProyecto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_proyectos);
           ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_projects); // ← marca el botón actual

        HeaderController.setup(this);
        NavController.setup(this);


        layoutProjects = findViewById(R.id.layoutProjects);
        btnCrearProyecto = findViewById(R.id.btnCrearProyecto);


        cargarProyectosDesdeBackend();




    }



    private void agregarProyecto(String nombre, String descripcion) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_proyecto, layoutProjects, false);

        TextView tvNombre = view.findViewById(R.id.tvNombreProyecto);
        TextView tvDesc = view.findViewById(R.id.tvDescripcionProyecto);
        Button btnIr = view.findViewById(R.id.btnIrProyecto);

        tvNombre.setText(nombre);
        tvDesc.setText(descripcion);
        btnIr.setOnClickListener(v -> {Intent intent = new Intent(this, SprintsActivity.class);
            intent.putExtra("nombreProyecto", nombre);
            intent.putExtra("descripcionProyecto", descripcion);
            // En el futuro aquí también enviarás el idProyecto (para el backend)
            startActivity(intent);
        });

        layoutProjects.addView(view);
    }
    private void cargarProyectosDesdeBackend() {
        // Recuperar JWT guardado en SharedPreferences
        String jwt = getSharedPreferences("ScrumTutorPrefs", MODE_PRIVATE)
                .getString("jwt", null);

        if (jwt == null || jwt.isEmpty()) {
            Toast.makeText(this, "Sesión expirada. Inicia sesión de nuevo.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        // Crear servicio
        ProyectoService service = ApiClient.getClient(this).create(ProyectoService.class);

        service.getMisProyectos().enqueue(new retrofit2.Callback<List<Proyecto>>() {
            @Override
            public void onResponse(Call<List<Proyecto>> call,
                                   Response<List<Proyecto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<sv.edu.catolica.appscrumtutor.proyecto.Proyecto> proyectos = response.body();
                    layoutProjects.removeAllViews();

                    if (proyectos.isEmpty()) {
                        mostrarSinProyectos();
                    } else {
                        for (sv.edu.catolica.appscrumtutor.proyecto.Proyecto p : proyectos) {
                            agregarProyecto(p.getNombre(), p.getDescripcion());
                        }
                    }
                } else {
                    Toast.makeText(ProyectosActivity.this, "Error al cargar proyectos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<sv.edu.catolica.appscrumtutor.proyecto.Proyecto>> call, Throwable t) {
                Toast.makeText(ProyectosActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void mostrarSinProyectos() {
        TextView msg = new TextView(this);
        msg.setText("No tienes proyectos");
        msg.setTextSize(18);
        msg.setPadding(0, 24, 0, 24);
        msg.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
        layoutProjects.addView(msg);
    }


    public void CrearProyecto(View view) {
            Intent intent = new Intent(this, createProyect.class);
            startActivity(intent);


    }
}
