package sv.edu.catolica.appscrumtutor;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sv.edu.catolica.appscrumtutor.network.ApiClient;
import sv.edu.catolica.appscrumtutor.sprint.Historia;
import sv.edu.catolica.appscrumtutor.sprint.HistoriaCreateDTO;
import sv.edu.catolica.appscrumtutor.sprint.HistoriaService;
import sv.edu.catolica.appscrumtutor.sprint.HistoriaUpdateDTO;

public class CrearHistoriasActivity extends AppCompatActivity {


    private EditText etTitulo, etDescripcion, etCriterios;
    private Spinner spPrioridad, spEstado;
    private Button btnGuardar;

    private long idProyecto;
    private Integer idSprint;
    private Integer idHistoria;     // para edición
    private boolean modoEdicion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_historias);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);
        etCriterios = findViewById(R.id.etCriterios);
        spPrioridad = findViewById(R.id.spPrioridad);
        spEstado = findViewById(R.id.spEstado);
        btnGuardar = findViewById(R.id.btnGuardarHistoria);

        idProyecto = getIntent().getLongExtra("idProyecto", -1);
        idSprint = getIntent().getIntExtra("idSprint", -1);
        if (idSprint == -1) idSprint = null;

        // MODO EDICIÓN
        if ("editar".equals(getIntent().getStringExtra("modo"))) {
            modoEdicion = true;
            idHistoria = getIntent().getIntExtra("idHistoria", -1);
            btnGuardar.setText("Guardar cambios");
            cargarDatosParaEditar();
        }

        // ESTE ES EL LISTENER CORRECTO
        btnGuardar.setOnClickListener(v -> {
            if (modoEdicion) actualizarHistoria();
            else crearHistoria();
        });



    }

    private void cargarDatosParaEditar() {
        HistoriaService api = ApiClient.getClient(this).create(HistoriaService.class);

        api.detalle(idHistoria).enqueue(new Callback<Historia>() {
            @Override
            public void onResponse(Call<Historia> call, Response<Historia> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(CrearHistoriasActivity.this, "Error al cargar historia", Toast.LENGTH_SHORT).show();
                    return;
                }

                Historia h = response.body();

                etTitulo.setText(h.getTitulo());
                etDescripcion.setText(h.getDescripcion());
                etCriterios.setText(h.getCriteriosAceptacion());

                spPrioridad.setSelection(h.getIdPrioridad() - 1);
                spEstado.setSelection(h.getIdEstado() - 1);
            }

            @Override
            public void onFailure(Call<Historia> call, Throwable t) {
                Toast.makeText(CrearHistoriasActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void crearHistoria() {
        HistoriaCreateDTO dto = new HistoriaCreateDTO(
                idProyecto,
                idSprint,
                etTitulo.getText().toString(),
                etDescripcion.getText().toString(),
                spPrioridad.getSelectedItemPosition() + 1,
                etCriterios.getText().toString(),
                spEstado.getSelectedItemPosition() + 1
        );

        HistoriaService api = ApiClient.getClient(this).create(HistoriaService.class);

        api.crear(dto).enqueue(new Callback<Historia>() {
            @Override
            public void onResponse(Call<Historia> call, Response<Historia> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CrearHistoriasActivity.this, "Historia creada", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CrearHistoriasActivity.this, "Error al crear historia", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Historia> call, Throwable t) {
                Toast.makeText(CrearHistoriasActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void actualizarHistoria() {

        HistoriaUpdateDTO dto = new HistoriaUpdateDTO(
                etTitulo.getText().toString(),
                etDescripcion.getText().toString(),
                idSprint,
                spPrioridad.getSelectedItemPosition() + 1,
                etCriterios.getText().toString(),
                spEstado.getSelectedItemPosition() + 1
        );

        HistoriaService api = ApiClient.getClient(this).create(HistoriaService.class);

        api.actualizar(idHistoria, dto).enqueue(new Callback<Historia>() {
            @Override
            public void onResponse(Call<Historia> call, Response<Historia> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CrearHistoriasActivity.this, "Cambios guardados", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CrearHistoriasActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Historia> call, Throwable t) {
                Toast.makeText(CrearHistoriasActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}