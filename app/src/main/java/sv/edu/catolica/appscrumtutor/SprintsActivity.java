package sv.edu.catolica.appscrumtutor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import sv.edu.catolica.appscrumtutor.sprint.Sprint;
import sv.edu.catolica.appscrumtutor.sprint.SprintService;

public class SprintsActivity extends AppCompatActivity {

    private LinearLayout layoutSprints;

    private long idProyecto = -1; // ← campo global

    // En el futuro este valor vendrá del rol del usuario logueado (Product Owner, etc.)
    private boolean esProductOwner = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sprints);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_projects); // ← marca el botón actual

        HeaderController.setup(this);
        NavController.setup(this);


        layoutSprints = findViewById(R.id.layoutSprints);
        Button btnCrearSprint = findViewById(R.id.btnCrearSprint);
        // Solo mostrar botón crear si el usuario tiene permisos

        //btnCrearSprint.setVisibility(esProductOwner ? View.VISIBLE : View.GONE);

        btnCrearSprint.setOnClickListener(v -> {
            // Ir a la misma activity para CREAR un sprint
            Intent intent = new Intent(this, GestionarSprintActivity.class);
            startActivity(intent);
        });

        Button btnVerMiembros = findViewById(R.id.btnVerMiembros);

        btnVerMiembros.setOnClickListener(v -> {
            Intent intent = new Intent(this, MiembrosActivity.class);
            intent.putExtra("idProyecto", idProyecto);
            startActivity(intent);

        });



        idProyecto = getIntent().getLongExtra("idProyecto", -1);
        String nombreProyecto = getIntent().getStringExtra("nombreProyecto");

        if (idProyecto == -1) {
            Toast.makeText(this, "Error: Proyecto no identificado", Toast.LENGTH_SHORT).show();
            Log.e("SprintsActivity", "idProyecto inválido (-1)");
            finish();
            return;
        }

        Log.d("SprintsActivity", "idProyecto recibido: " + idProyecto);


        TextView tvTituloProyecto = findViewById(R.id.tvTituloProyecto);
        if (nombreProyecto != null) {
            tvTituloProyecto.setText("Sprints de " + nombreProyecto);
        }

        cargarSprintsDesdeBackend(idProyecto);
    }
    private void cargarSprintsDesdeBackend(long idProyecto) {
        SprintService service = ApiClient.getClient(this).create(SprintService.class);

        service.listarPorProyecto(idProyecto).enqueue(new retrofit2.Callback<List<Sprint>>() {
            @Override
            public void onResponse(Call<List<Sprint>> call, Response<List<Sprint>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    layoutSprints.removeAllViews();
                    for (Sprint s : response.body()) {
                        Log.d("SprintCheck", "Sprint recibido -> id=" + s.getIdSprint()
                                + ", nombre=" + s.getNombre()
                                + ", estado=" + s.getEstadoDescripcion());
                        agregarSprint(s);
                    }
                } else {
                    Toast.makeText(SprintsActivity.this, "No se pudieron cargar los sprints", Toast.LENGTH_SHORT).show();
                    Log.e("SprintsActivity", "Error HTTP: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Sprint>> call, Throwable t) {
                Toast.makeText(SprintsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("SprintsActivity", "Fallo Retrofit", t);
            }
        });
    }

    private void agregarSprint(Sprint sprint) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_sprint, layoutSprints, false);

        TextView tvNombre = view.findViewById(R.id.tvNombreSprint);
        TextView tvEstado = view.findViewById(R.id.tvEstado);
        TextView tvFechas = view.findViewById(R.id.tvFechas);
        Button btnVer = view.findViewById(R.id.btnVerSprint);

        tvNombre.setText(sprint.getNombre());
        tvEstado.setText(sprint.getEstadoDescripcion() != null ? sprint.getEstadoDescripcion() : "Sin estado");
        tvFechas.setText("📅 " + sprint.getFechaInicio() + " → " + sprint.getFechaFin());

        btnVer.setOnClickListener(v -> {
            Log.d("SprintsActivity", "Abriendo ProyectoDetalleActivity con idProyecto=" + idProyecto
                    + ", idSprint=" + sprint.getIdSprint());

            Intent intent = new Intent(this, ProyectoDetalleActivity.class);
            intent.putExtra("idProyecto", idProyecto);
            intent.putExtra("idSprint", sprint.getIdSprint());
            intent.putExtra("nombreSprint", sprint.getNombre());

            // también pasa el nombre del proyecto
            String nombreProyecto = getIntent().getStringExtra("nombreProyecto");
            if (nombreProyecto != null) {
                intent.putExtra("nombreProyecto", nombreProyecto);
            }
            startActivity(intent);


        });

        layoutSprints.addView(view);
    }
}