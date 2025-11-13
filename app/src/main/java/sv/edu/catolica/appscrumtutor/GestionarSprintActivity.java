package sv.edu.catolica.appscrumtutor;

import android.app.DatePickerDialog;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sv.edu.catolica.appscrumtutor.network.ApiClient;
import sv.edu.catolica.appscrumtutor.sprint.Sprint;
import sv.edu.catolica.appscrumtutor.sprint.SprintCreateDTO;
import sv.edu.catolica.appscrumtutor.sprint.SprintService;
import sv.edu.catolica.appscrumtutor.sprint.SprintUpdateDTO;

public class GestionarSprintActivity extends AppCompatActivity {
    private EditText etNombreSprint, etObjetivo, etFechaInicio, etFechaFin;
    private Spinner spEstado;
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

        configurarSelectorFecha();

        // Recibir datos
        idProyecto = getIntent().getLongExtra("idProyecto", -1);
        idSprint = getIntent().getLongExtra("idSprint", -1);

        // Editar modo
        esEdicion = (idSprint != -1);

        if (esEdicion) {
            cargarDatosDeSprint();
            btnGuardar.setText("Actualizar Sprint");
        } else {
            btnGuardar.setText("Crear Sprint");
            spEstado.setEnabled(false); // backend lo asigna automáticamente
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
                    Toast.makeText(GestionarSprintActivity.this, "Sprint creado", Toast.LENGTH_SHORT).show();
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