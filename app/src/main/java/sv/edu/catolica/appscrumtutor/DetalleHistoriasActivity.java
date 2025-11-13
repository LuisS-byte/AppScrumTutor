package sv.edu.catolica.appscrumtutor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sv.edu.catolica.appscrumtutor.network.ApiClient;
import sv.edu.catolica.appscrumtutor.sprint.Historia;
import sv.edu.catolica.appscrumtutor.sprint.HistoriaService;
import sv.edu.catolica.appscrumtutor.sprint.Sprint;
import sv.edu.catolica.appscrumtutor.sprint.SprintService;

public class DetalleHistoriasActivity extends AppCompatActivity {
    private TextView tvTituloHistoria, tvDescripcionHistoria, tvPrioridad, tvEstado, tvCriteriosAceptacion;
    private Button btnEditarHistoria, btnEliminar, btnAsignarSprint;

    private int idHistoria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle_historias);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
            return insets;
        });

        // Referencias UI
        tvTituloHistoria = findViewById(R.id.tvTituloHistoria);
        tvDescripcionHistoria = findViewById(R.id.tvDescripcionHistoria);
        tvPrioridad = findViewById(R.id.tvPrioridad);
        tvEstado = findViewById(R.id.tvEstado);
        tvCriteriosAceptacion = findViewById(R.id.tvCriteriosAceptacion);

        btnEditarHistoria = findViewById(R.id.btnEditarHistoria);
        btnEliminar = findViewById(R.id.btnEliminarHistoria);
        btnAsignarSprint = findViewById(R.id.btnAsignarSprint);

        // Recuperar ID necesario
        idHistoria = getIntent().getIntExtra("idHistoria", -1);

        if (idHistoria == -1) {
            Toast.makeText(this, "Error: historia no encontrada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cargarDetalle();

        btnEditarHistoria.setOnClickListener(v -> abrirEditor());
        btnEliminar.setOnClickListener(v -> eliminarHistoria());

        btnAsignarSprint.setOnClickListener(v -> mostrarDialogoSprints());
    }


    private void cargarDetalle() {
        HistoriaService api = ApiClient.getClient(this).create(HistoriaService.class);

        api.detalle(idHistoria).enqueue(new Callback<Historia>() {
            @Override
            public void onResponse(Call<Historia> call, Response<Historia> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(DetalleHistoriasActivity.this, "No se pudo cargar el detalle", Toast.LENGTH_SHORT).show();
                    return;
                }

                Historia h = response.body();

                tvTituloHistoria.setText(h.getTitulo());
                tvDescripcionHistoria.setText(h.getDescripcion());
                tvPrioridad.setText("Prioridad: " + h.getPrioridad());
                tvEstado.setText("Estado: " + h.getEstado());
                tvCriteriosAceptacion.setText(h.getCriteriosAceptacion());
            }

            @Override
            public void onFailure(Call<Historia> call, Throwable t) {
                Toast.makeText(DetalleHistoriasActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void abrirEditor() {
        Intent intent = new Intent(this, CrearHistoriasActivity.class);
        intent.putExtra("modo", "editar");
        intent.putExtra("idHistoria", idHistoria);
        intent.putExtra("idProyecto", getIntent().getLongExtra("idProyecto", -1)); // <- AGREGA ESTO
        intent.putExtra("idSprint", getIntent().getIntExtra("idSprint", -1)); // optional
        startActivityForResult(intent, 200);
    }
    private void mostrarDialogoSprints() {
        SprintService sprintApi = ApiClient.getClient(this).create(SprintService.class);

        long idProyecto = getIntent().getLongExtra("idProyecto", -1);
        int sprintActual = getIntent().getIntExtra("idSprint", -1);

        sprintApi.listarPorProyecto(idProyecto).enqueue(new Callback<List<Sprint>>() {
            @Override
            public void onResponse(Call<List<Sprint>> call, Response<List<Sprint>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(DetalleHistoriasActivity.this, "No se pudieron cargar los sprints", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Sprint> sprints = response.body();

                // 1. Quitar sprint actual
                if (sprintActual != -1) {
                    sprints.removeIf(s -> s.getIdSprint() == sprintActual);
                }

                // 2. Quitar sprints cerrados (estado = 3)
                sprints.removeIf(s -> s.getIdEstado() == 3);

                if (sprints.isEmpty()) {
                    Toast.makeText(DetalleHistoriasActivity.this, "No hay sprints disponibles", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lista limpia de nombres
                String[] nombres = new String[sprints.size()];
                for (int i = 0; i < sprints.size(); i++) {
                    nombres[i] = sprints.get(i).getNombre();
                }

                new AlertDialog.Builder(DetalleHistoriasActivity.this)
                        .setTitle("Asignar a Sprint")
                        .setItems(nombres, (dialog, which) -> {
                            Sprint seleccionado = sprints.get(which);
                            asignarHistoriaASprint(seleccionado.getIdSprint());
                        })
                        .show();
            }

            @Override
            public void onFailure(Call<List<Sprint>> call, Throwable t) {
                Toast.makeText(DetalleHistoriasActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void asignarHistoriaASprint(long idSprint) {
        HistoriaService api = ApiClient.getClient(this).create(HistoriaService.class);

        api.asignarSprint(idHistoria, idSprint).enqueue(new Callback<Historia>() {
            @Override
            public void onResponse(Call<Historia> call, Response<Historia> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetalleHistoriasActivity.this, "Asignado correctamente", Toast.LENGTH_SHORT).show();
                    finish(); // vuelve al backlog y refresca
                } else {
                    Toast.makeText(DetalleHistoriasActivity.this, "Error al asignar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Historia> call, Throwable t) {
                Toast.makeText(DetalleHistoriasActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eliminarHistoria() {
        HistoriaService api = ApiClient.getClient(this).create(HistoriaService.class);

        api.eliminar(idHistoria).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetalleHistoriasActivity.this, "Historia eliminada", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(DetalleHistoriasActivity.this, "No se pudo eliminar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(DetalleHistoriasActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == RESULT_OK) {
            finish(); // Cierra esta pantalla
        }
    }
}