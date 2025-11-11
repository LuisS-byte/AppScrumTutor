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
import sv.edu.catolica.appscrumtutor.auth.TokenStore;
import sv.edu.catolica.appscrumtutor.net.ApiService;
import sv.edu.catolica.appscrumtutor.net.RetrofitClient;
import sv.edu.catolica.appscrumtutor.net.dto.ProyectoDto;

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
        bottomNav.setSelectedItemId(R.id.nav_projects);

        HeaderController.setup(this);
        NavController.setup(this);

        layoutProjects = findViewById(R.id.layoutProjects);
        btnCrearProyecto = findViewById(R.id.btnCrearProyecto);

        // ======== 🔑 TOKEN DEL LOGIN =========
        String token = TokenStore.get(this);
        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "⚠️ No hay token, inicia sesión primero", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        // Mostrar token solo para verificar (puedes quitar este Toast)
        Toast.makeText(this, "🔐 Token actual: " + token.substring(0, Math.min(20, token.length())) + "...", Toast.LENGTH_LONG).show();

        // ======== 🔄 CARGAR PROYECTOS DESDE BACKEND =========
        cargarProyectosDesdeBackend(token);

        btnCrearProyecto.setOnClickListener(v ->
                Toast.makeText(this, "Crear nuevo proyecto (mock)", Toast.LENGTH_SHORT).show()
        );
    }

    private void cargarProyectosDesdeBackend(String token) {
        ApiService api = RetrofitClient.get(this).create(ApiService.class);
        Call<List<ProyectoDto>> call = api.listarMisProyectos("Bearer " + token);

        call.enqueue(new Callback<List<ProyectoDto>>() {
            @Override
            public void onResponse(Call<List<ProyectoDto>> call, Response<List<ProyectoDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ProyectoDto> proyectos = response.body();
                    if (proyectos.isEmpty()) {
                        mostrarSinProyectos();
                    } else {
                        for (ProyectoDto p : proyectos) {
                            //agregarProyecto(p.getNombre(), p.getDescripcion());
                        }
                    }
                } else {
                    Toast.makeText(ProyectosActivity.this, "Error cargando proyectos (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProyectoDto>> call, Throwable t) {
                Toast.makeText(ProyectosActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void agregarProyecto(String nombre, String descripcion) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_proyecto, layoutProjects, false);

        TextView tvNombre = view.findViewById(R.id.tvNombreProyecto);
        TextView tvDesc = view.findViewById(R.id.tvDescripcionProyecto);
        Button btnIr = view.findViewById(R.id.btnIrProyecto);

        tvNombre.setText(nombre);
        tvDesc.setText(descripcion);
        btnIr.setOnClickListener(v -> {
            Intent intent = new Intent(this, SprintsActivity.class);
            intent.putExtra("nombreProyecto", nombre);
            intent.putExtra("descripcionProyecto", descripcion);
            startActivity(intent);
        });

        layoutProjects.addView(view);
    }

    private void mostrarSinProyectos() {
        TextView msg = new TextView(this);
        msg.setText("No tienes proyectos");
        msg.setTextSize(18);
        msg.setPadding(0, 24, 0, 24);
        msg.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
        layoutProjects.addView(msg);
    }
}
