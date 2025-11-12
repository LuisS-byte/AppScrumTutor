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
import retrofit2.Callback;
import retrofit2.Response;
import sv.edu.catolica.appscrumtutor.network.ApiClient;
import sv.edu.catolica.appscrumtutor.proyecto.Proyecto;
import sv.edu.catolica.appscrumtutor.proyecto.ProyectoService;

public class ProyectosActivity extends AppCompatActivity {

    private LinearLayout layoutProjects;
    private Button btnCrearProyecto;

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
        bottomNav.setSelectedItemId(R.id.nav_projects);

        HeaderController.setup(this);
        NavController.setup(this);

        layoutProjects = findViewById(R.id.layoutProjects);
        btnCrearProyecto = findViewById(R.id.btnCrearProyecto);

        // Acción del botón crear
        btnCrearProyecto.setOnClickListener(v -> {
            Intent intent = new Intent(this, createProyect.class);
            startActivity(intent);
        });

        // Cargar proyectos del backend
        cargarProyectosDesdeBackend();
    }

    // =====================================================
    // Cargar proyectos del backend (con verificación de JWT)
    // =====================================================
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

        ProyectoService service = ApiClient.getClient(this).create(ProyectoService.class);

        service.getMisProyectos().enqueue(new Callback<List<Proyecto>>() {
            @Override
            public void onResponse(Call<List<Proyecto>> call, Response<List<Proyecto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Proyecto> proyectos = response.body();
                    layoutProjects.removeAllViews();

                    if (proyectos.isEmpty()) {
                        mostrarSinProyectos();
                    } else {
                        for (Proyecto p : proyectos) {
                            agregarProyecto(p);
                        }
                    }
                } else {
                    Toast.makeText(ProyectosActivity.this, "Error al cargar proyectos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Proyecto>> call, Throwable t) {
                Toast.makeText(ProyectosActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // =====================================================
    // Agregar cada proyecto visualmente con botones extra
    // =====================================================
    private void agregarProyecto(Proyecto proyecto) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_proyecto, layoutProjects, false);

        TextView tvNombre = view.findViewById(R.id.tvNombreProyecto);
        TextView tvDesc = view.findViewById(R.id.tvDescripcionProyecto);
        Button btnIr = view.findViewById(R.id.btnIrProyecto);
        Button btnEditar = view.findViewById(R.id.btnEditarProyecto);
        Button btnEliminar = view.findViewById(R.id.btnEliminarProyecto);

        tvNombre.setText(proyecto.getNombre());
        tvDesc.setText(proyecto.getDescripcion());

        // → Ver sprints
        btnIr.setOnClickListener(v -> {
            Intent intent = new Intent(this, SprintsActivity.class);
            intent.putExtra("idProyecto", proyecto.getId());
            intent.putExtra("nombreProyecto", proyecto.getNombre());
            intent.putExtra("descripcionProyecto", proyecto.getDescripcion());
            startActivity(intent);
        });

        // ✏ Editar proyecto
        btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(this, updateProyect.class);
            intent.putExtra("idProyecto", proyecto.getId());
            startActivity(intent);
        });

        // 🗑 Eliminar proyecto
        btnEliminar.setOnClickListener(v -> {
            ProyectoService service = ApiClient.getClient(this).create(ProyectoService.class);
            service.deleteProyecto(proyecto.getId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ProyectosActivity.this, "Proyecto eliminado correctamente", Toast.LENGTH_SHORT).show();
                        cargarProyectosDesdeBackend(); // Refrescar lista
                    } else {
                        Toast.makeText(ProyectosActivity.this, "Error al eliminar proyecto", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(ProyectosActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        layoutProjects.addView(view);
    }

    // =====================================================
    // Mensaje si no hay proyectos
    // =====================================================
    private void mostrarSinProyectos() {
        TextView msg = new TextView(this);
        msg.setText("No tienes proyectos");
        msg.setTextSize(18);
        msg.setPadding(0, 24, 0, 24);
        msg.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
        layoutProjects.addView(msg);
    }

    // =====================================================
    // Método alternativo para vincular con layout XML
    // =====================================================
    public void CrearProyecto(View view) {
        Intent intent = new Intent(this, createProyect.class);
        startActivity(intent);
    }
}