package sv.edu.catolica.appscrumtutor;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sv.edu.catolica.appscrumtutor.network.ApiClient;
import sv.edu.catolica.appscrumtutor.sprint.ProyectoMiembroDto;
import sv.edu.catolica.appscrumtutor.sprint.ProyectoMiembroService;
import sv.edu.catolica.appscrumtutor.sprint.Tarea;
import sv.edu.catolica.appscrumtutor.sprint.TareaCreateDTO;
import sv.edu.catolica.appscrumtutor.sprint.TareaService;
import sv.edu.catolica.appscrumtutor.sprint.TareaUpdateDTO;

public class CrearTareaActivity extends AppCompatActivity {
    private EditText etTituloTarea, etDescripcionTarea, etFechaLimite;
    private Spinner spEstadoTarea, spResponsable;
    private Button btnGuardarTarea;

    private long idProyecto;
    private long idTareaEditar = -1;
    private boolean esEdicion = false;
    private long idSprint;

    private List<ProyectoMiembroDto> listaMiembros = new ArrayList<>();
    private List<String> nombresMiembros = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_tarea);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etTituloTarea = findViewById(R.id.etTituloTarea);
        etDescripcionTarea = findViewById(R.id.etDescripcionTarea);
        etFechaLimite = findViewById(R.id.etFechaLimite);
        spEstadoTarea = findViewById(R.id.spEstadoTarea);
        spResponsable = findViewById(R.id.spResponsable);
        btnGuardarTarea = findViewById(R.id.btnGuardarTarea);

        idProyecto = getIntent().getLongExtra("idProyecto", -1);
        idSprint = getIntent().getLongExtra("idSprint", -1);

        idTareaEditar = getIntent().getIntExtra("idTarea", -1);
        esEdicion = idTareaEditar != -1;

        configurarFecha();
        cargarMiembrosProyecto();

        if (esEdicion) {
            btnGuardarTarea.setText("Actualizar tarea");

        }




        btnGuardarTarea.setOnClickListener(v ->{if (esEdicion) actualizar();
        else crear();
    });


    } private void configurarFecha() {
        etFechaLimite.setFocusable(false);
        etFechaLimite.setOnClickListener(view -> mostrarDatePicker());
    }

    private void mostrarDatePicker() {
        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dlg = new DatePickerDialog(this, (view, year, month, day) -> {
            String fecha = String.format(Locale.getDefault(), "%04d-%02d-%02d",
                    year, month + 1, day);
            etFechaLimite.setText(fecha);
        }, y, m, d);

        dlg.show();
    }
    private void cargarMiembrosProyecto() {
        ProyectoMiembroService service =
                ApiClient.getClient(this).create(ProyectoMiembroService.class);

        service.listar(idProyecto).enqueue(new Callback<List<ProyectoMiembroDto>>() {
            @Override
            public void onResponse(Call<List<ProyectoMiembroDto>> call,
                                   Response<List<ProyectoMiembroDto>> response) {

                if (!response.isSuccessful() || response.body() == null) return;

                listaMiembros.clear();
                nombresMiembros.clear();

                for (ProyectoMiembroDto m : response.body()) {
                    listaMiembros.add(m);
                    nombresMiembros.add(m.getUsuarioNombre());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        CrearTareaActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        nombresMiembros
                );

                spResponsable.setAdapter(adapter);

                // ←──────── IMPORTANTE
                // SI ES EDICIÓN → CARGAR LOS DATOS SOLO DESPUÉS DE CARGAR MIEMBROS
                if (esEdicion) {
                    cargarDatosParaEditar();
                }
            }

            @Override
            public void onFailure(Call<List<ProyectoMiembroDto>> call, Throwable t) {
                Toast.makeText(CrearTareaActivity.this,
                        "Error cargando miembros: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarDatosParaEditar() {
        TareaService api = ApiClient.getClient(this).create(TareaService.class);

        api.detalle(idTareaEditar).enqueue(new Callback<Tarea>() {
            @Override
            public void onResponse(Call<Tarea> call, Response<Tarea> response) {
                if (!response.isSuccessful() || response.body() == null) return;

                Tarea t = response.body();

                etTituloTarea.setText(t.getTitulo());
                etDescripcionTarea.setText(t.getDescripcion());
                etFechaLimite.setText(t.getFechaLimite());

                spEstadoTarea.setSelection(t.getIdEstado() - 1);

                if (t.getIdAsignadoA() != null) {
                    for (int i = 0; i < listaMiembros.size(); i++) {
                        if (listaMiembros.get(i).getIdUsuario().equals(t.getIdAsignadoA())) {
                            spResponsable.setSelection(i);
                            break;
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<Tarea> call, Throwable t) {
                Toast.makeText(CrearTareaActivity.this, "Error cargando tarea", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void crear() {
        String titulo = etTituloTarea.getText().toString().trim();
        if (titulo.isEmpty()) {
            Toast.makeText(this, "Título requerido", Toast.LENGTH_SHORT).show();
            return;
        }

        String desc = etDescripcionTarea.getText().toString().trim();
        String fecha = etFechaLimite.getText().toString().trim();
        int estado = spEstadoTarea.getSelectedItemPosition() + 1;

        Long idAsignado = listaMiembros.isEmpty()
                ? null
                : listaMiembros.get(spResponsable.getSelectedItemPosition()).getIdUsuario();

        TareaCreateDTO dto = new TareaCreateDTO(
                (int) idSprint,
                null,
                titulo,
                desc,
                estado,
                fecha.isEmpty() ? null : fecha,
                idAsignado
        );

        TareaService api = ApiClient.getClient(this).create(TareaService.class);

        api.crear(dto).enqueue(new Callback<Tarea>() {
            @Override
            public void onResponse(Call<Tarea> call, Response<Tarea> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CrearTareaActivity.this, "Tarea creada", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CrearTareaActivity.this, "Error al crear", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tarea> call, Throwable t) {
                Toast.makeText(CrearTareaActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizar() {
        String titulo = etTituloTarea.getText().toString().trim();
        if (titulo.isEmpty()) {
            Toast.makeText(this, "Título requerido", Toast.LENGTH_SHORT).show();
            return;
        }

        String desc = etDescripcionTarea.getText().toString().trim();
        String fecha = etFechaLimite.getText().toString().trim();
        int estado = spEstadoTarea.getSelectedItemPosition() + 1;

        Long idAsignado = listaMiembros.isEmpty()
                ? null
                : listaMiembros.get(spResponsable.getSelectedItemPosition()).getIdUsuario();

        TareaUpdateDTO dto = new TareaUpdateDTO(
                null,
                titulo,
                desc,
                estado,
                fecha.isEmpty() ? null : fecha,
                idAsignado
        );

        TareaService api = ApiClient.getClient(this).create(TareaService.class);

        api.actualizar(idTareaEditar, dto).enqueue(new Callback<Tarea>() {
            @Override
            public void onResponse(Call<Tarea> call, Response<Tarea> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CrearTareaActivity.this, "Tarea actualizada", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CrearTareaActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tarea> call, Throwable t) {
                Toast.makeText(CrearTareaActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}