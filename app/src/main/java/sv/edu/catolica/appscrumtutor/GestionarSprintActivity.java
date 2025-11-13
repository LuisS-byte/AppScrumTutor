package sv.edu.catolica.appscrumtutor;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sv.edu.catolica.appscrumtutor.network.ApiClient;
import sv.edu.catolica.appscrumtutor.sprint.Historia;
import sv.edu.catolica.appscrumtutor.sprint.HistoriaService;
import sv.edu.catolica.appscrumtutor.sprint.Sprint;
import sv.edu.catolica.appscrumtutor.sprint.SprintCreateDTO;
import sv.edu.catolica.appscrumtutor.sprint.SprintService;
import sv.edu.catolica.appscrumtutor.sprint.SprintUpdateDTO;

public class GestionarSprintActivity extends AppCompatActivity {
    private EditText etNombreSprint, etObjetivo, etFechaInicio, etFechaFin;
    private Spinner spEstado;

    private Button btnSeleccionarHistorias;
    private TextView tvHistoriasSeleccionadas;

    // Historias seleccionadas (IDs)
    private List<Integer> historiasSeleccionadas = new ArrayList<>();
    private Button btnGuardar;

    private long idProyecto = -1;
    private long idSprint = -1;
    private boolean esEdicion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestionar_sprint);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etNombreSprint = findViewById(R.id.etNombreSprint);
        etObjetivo = findViewById(R.id.etObjetivoSprint);
        etFechaInicio = findViewById(R.id.etFechaInicio);
        etFechaFin = findViewById(R.id.etFechaFin);
        spEstado = findViewById(R.id.spEstadoSprint);
        btnGuardar = findViewById(R.id.btnGuardarSprint);
        btnSeleccionarHistorias = findViewById(R.id.btnSeleccionarHistorias);
        tvHistoriasSeleccionadas = findViewById(R.id.tvHistoriasSeleccionadas);

        btnSeleccionarHistorias.setOnClickListener(v -> cargarHistoriasNoAsignadas());



        configurarSelectorFecha();

        // Recibir datos
        idProyecto = getIntent().getLongExtra("idProyecto", -1);
        idSprint = getIntent().getLongExtra("idSprint", -1);

        // Editar modo
        esEdicion = (idSprint != -1);

        // Editar o crear
        esEdicion = (idSprint != -1);

        if (esEdicion) {
            // Modo edición
            cargarDatosDeSprint();        // carga nombre, fechas, estado
            btnGuardar.setText("Actualizar Sprint");

            // Deshabilitar selección de historias
            btnSeleccionarHistorias.setEnabled(false);
            btnSeleccionarHistorias.setAlpha(0.4f);

            // Mostrar historias asignadas a este sprint
            cargarHistoriasAsignadas();

        } else {
            // Modo creación
            btnGuardar.setText("Crear Sprint");

            // El estado no se elige en creación
            spEstado.setEnabled(false);
            spEstado.setAlpha(0.4f);
        }

        btnGuardar.setOnClickListener(v -> {
            if (esEdicion)
                actualizarSprint();
            else
                crearSprint();
        });
    }

    // ------------------- FECHAS ---------------------- //


    private void configurarSelectorFecha() {

        etFechaInicio.setFocusable(false);
        etFechaFin.setFocusable(false);

        etFechaInicio.setOnClickListener(v -> mostrarPickerFechaInicio());
        etFechaFin.setOnClickListener(v -> mostrarPickerFechaFin());
    }
    private void cargarHistoriasNoAsignadas() {
        HistoriaService api = ApiClient.getClient(this).create(HistoriaService.class);

        api.listarPorProyecto(idProyecto).enqueue(new Callback<List<Historia>>() {
            @Override
            public void onResponse(Call<List<Historia>> call, Response<List<Historia>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(GestionarSprintActivity.this, "Error cargando historias", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Historia> disponibles = new ArrayList<>();
                for (Historia h : response.body()) {
                    if (h.getIdSprint() == null) {   // solo historias sin sprint
                        disponibles.add(h);
                    }
                }

                mostrarDialogoSeleccion(disponibles);
            }

            @Override
            public void onFailure(Call<List<Historia>> call, Throwable t) {
                Toast.makeText(GestionarSprintActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void cargarHistoriasAsignadas() {
        HistoriaService api = ApiClient.getClient(this).create(HistoriaService.class);

        api.listarPorProyecto(idProyecto).enqueue(new Callback<List<Historia>>() {
            @Override
            public void onResponse(Call<List<Historia>> call, Response<List<Historia>> response) {
                if (!response.isSuccessful() || response.body() == null) return;

                StringBuilder sb = new StringBuilder();

                for (Historia h : response.body()) {
                    if (h.getIdSprint() != null && h.getIdSprint() == idSprint) {
                        sb.append("- ").append(h.getTitulo()).append("\n");
                    }
                }

                if (sb.length() == 0) {
                    tvHistoriasSeleccionadas.setText("No hay historias asignadas");
                } else {
                    tvHistoriasSeleccionadas.setText(sb.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Historia>> call, Throwable t) {}
        });
    }

    private void mostrarPickerFechaInicio() {
        final Calendar c = Calendar.getInstance();
        int año = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dp = new DatePickerDialog(
                this,
                (view, yr, mo, dy) -> {
                    String fecha = String.format(Locale.getDefault(),
                            "%04d-%02d-%02d", yr, mo + 1, dy);

                    etFechaInicio.setText(fecha);
                    etFechaFin.setText("");
                },
                año, mes, dia
        );

        dp.getDatePicker().setMinDate(System.currentTimeMillis());
        dp.show();
    }
    private void mostrarDialogoSeleccion(List<Historia> disponibles) {
        if (disponibles.isEmpty()) {
            Toast.makeText(this, "No hay historias disponibles", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] nombres = new String[disponibles.size()];

        for (int i = 0; i < disponibles.size(); i++) {
            nombres[i] = disponibles.get(i).getTitulo();
        }

        final int[] seleccion = { -1 }; // solo una opción

        new AlertDialog.Builder(this)
                .setTitle("Selecciona una historia")
                .setSingleChoiceItems(nombres, -1, (dialog, which) -> {
                    seleccion[0] = which; // guardar selección
                })
                .setPositiveButton("Aceptar", (dialog, which) -> {

                    historiasSeleccionadas.clear();

                    if (seleccion[0] != -1) {
                        Historia h = disponibles.get(seleccion[0]);
                        historiasSeleccionadas.add(h.getId());
                        tvHistoriasSeleccionadas.setText("- " + h.getTitulo());
                    } else {
                        tvHistoriasSeleccionadas.setText("Ninguna historia seleccionada");
                    }

                })
                .setNegativeButton("Cancelar", null)
                .show();
    }



    private void mostrarPickerFechaFin() {

        String inicioStr = etFechaInicio.getText().toString();
        if (inicioStr.isEmpty()) {
            Toast.makeText(this, "Primero selecciona la fecha de inicio", Toast.LENGTH_SHORT).show();
            return;
        }

        LocalDate inicio = LocalDate.parse(inicioStr);
        long minMillis = inicio.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        LocalDate hoy = LocalDate.now();
        Calendar c = Calendar.getInstance();

        DatePickerDialog dp = new DatePickerDialog(
                this,
                (view, yr, mo, dy) -> {
                    String fecha = String.format(Locale.getDefault(),
                            "%04d-%02d-%02d", yr, mo + 1, dy);
                    etFechaFin.setText(fecha);
                },
                hoy.getYear(),
                hoy.getMonthValue() - 1,
                hoy.getDayOfMonth()
        );

        dp.getDatePicker().setMinDate(minMillis);
        dp.show();
    }

    private void asignarHistoriasAlSprint(long idSprint) {
        HistoriaService api = ApiClient.getClient(this).create(HistoriaService.class);

        for (Integer idHistoria : historiasSeleccionadas) {
            api.asignarSprint(idHistoria, idSprint).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call call, Response response) {}

                @Override
                public void onFailure(Call call, Throwable t) {}
            });
        }
    }

    // ------------------- CARGAR SPRINT EXISTENTE ---------------------- //

    private void cargarDatosDeSprint() {
        SprintService api = ApiClient.getClient(this).create(SprintService.class);

        api.detalle(idSprint).enqueue(new Callback<Sprint>() {
            @Override
            public void onResponse(Call<Sprint> call, Response<Sprint> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(GestionarSprintActivity.this, "Error cargando sprint", Toast.LENGTH_SHORT).show();
                    return;
                }

                Sprint s = response.body();

                etNombreSprint.setText(s.getNombre());
                etObjetivo.setText(s.getObjetivo());
                etFechaInicio.setText(s.getFechaInicio());
                etFechaFin.setText(s.getFechaFin());
                spEstado.setSelection(estadoAIndex(s.getEstadoDescripcion()));
            }

            @Override
            public void onFailure(Call<Sprint> call, Throwable t) {
                Toast.makeText(GestionarSprintActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int estadoAIndex(String estado) {
        if (estado == null) return 0;

        switch (estado.toUpperCase()) {
            case "PLANIFICADO": return 0;
            case "EN CURSO": return 1;
            case "CERRADO": return 2;
            default: return 0;
        }
    }

    // ------------------- CREAR ---------------------- //

    private void crearSprint() {

        String nombre = etNombreSprint.getText().toString().trim();
        String objetivo = etObjetivo.getText().toString().trim();
        String inicio = etFechaInicio.getText().toString().trim();
        String fin = etFechaFin.getText().toString().trim();

        if (nombre.isEmpty() || inicio.isEmpty() || fin.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        SprintCreateDTO dto = new SprintCreateDTO(
                idProyecto,
                nombre,
                objetivo,
                inicio,
                fin,
                1 // backend lo recalcula automáticamente
        );

        SprintService api = ApiClient.getClient(this).create(SprintService.class);

        api.crearSprint(dto).enqueue(new Callback<Sprint>() {
            @Override
            public void onResponse(Call<Sprint> call, Response<Sprint> response) {
                if (response.isSuccessful()) {
                    Sprint nuevoSprint = response.body();

                    if (!historiasSeleccionadas.isEmpty()) {
                        asignarHistoriasAlSprint(nuevoSprint.getIdSprint());
                    }

                    Toast.makeText(GestionarSprintActivity.this, "Sprint creado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Sprint> call, Throwable t) {
                Toast.makeText(GestionarSprintActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // ------------------- ACTUALIZAR ---------------------- //

    private void actualizarSprint() {

        String nombre = etNombreSprint.getText().toString().trim();
        String objetivo = etObjetivo.getText().toString().trim();
        String inicio = etFechaInicio.getText().toString().trim();
        String fin = etFechaFin.getText().toString().trim();

        int idEstado = spEstado.getSelectedItemPosition() + 1;

        SprintUpdateDTO dto = new SprintUpdateDTO(
                nombre,
                objetivo,
                inicio,
                fin,
                idEstado
        );

        SprintService api = ApiClient.getClient(this).create(SprintService.class);

        api.actualizarSprint(idSprint, dto).enqueue(new Callback<Sprint>() {
            @Override
            public void onResponse(Call<Sprint> call, Response<Sprint> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(GestionarSprintActivity.this, "Sprint actualizado", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(GestionarSprintActivity.this, "Error HTTP: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Sprint> call, Throwable t) {
                Toast.makeText(GestionarSprintActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}