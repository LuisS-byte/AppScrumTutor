package sv.edu.catolica.appscrumtutor;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sv.edu.catolica.appscrumtutor.network.ApiClient;
import sv.edu.catolica.appscrumtutor.proyecto.Proyecto;
import sv.edu.catolica.appscrumtutor.proyecto.ProyectoService;
import sv.edu.catolica.appscrumtutor.proyecto.ProyectoUpdateDTO;

public class updateProyect extends AppCompatActivity {

    private EditText etNombre, etDescripcion, etFechaInicio, etFechaFin;
    private Button btnActualizar;
    private Long idProyecto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_proyect);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etNombre = findViewById(R.id.etNombreProyecto);
        etDescripcion = findViewById(R.id.etDescripcionProyecto);
        etFechaInicio = findViewById(R.id.etFechaInicio);
        etFechaFin = findViewById(R.id.etFechaFin);
        btnActualizar = findViewById(R.id.btnActualizarProyecto);

        idProyecto = getIntent().getLongExtra("idProyecto", -1L);

        if (idProyecto != -1L) {
            cargarDatosProyecto(idProyecto);
        } else {
            Toast.makeText(this, "ID del proyecto no recibido", Toast.LENGTH_SHORT).show();
            finish();
        }

        etFechaInicio.setOnClickListener(v -> mostrarCalendario(etFechaInicio));
        etFechaFin.setOnClickListener(v -> mostrarCalendario(etFechaFin));

        btnActualizar.setOnClickListener(v -> actualizarProyecto());
    }

    private void mostrarCalendario(EditText campo) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (DatePicker view, int y, int m, int d) -> {
            String fecha = y + "-" + String.format("%02d", m + 1) + "-" + String.format("%02d", d);
            campo.setText(fecha);
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void cargarDatosProyecto(Long id) {
        ProyectoService service = ApiClient.getClient(this).create(ProyectoService.class);
        service.getById(id).enqueue(new Callback<Proyecto>() {
            @Override
            public void onResponse(Call<Proyecto> call, Response<Proyecto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Proyecto p = response.body();
                    etNombre.setText(p.getNombre());
                    etDescripcion.setText(p.getDescripcion());
                    etFechaInicio.setText(p.getFechaInicio());
                    etFechaFin.setText(p.getFechaFin());
                    Toast.makeText(updateProyect.this, "Proyecto cargado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(updateProyect.this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Proyecto> call, Throwable t) {
                Toast.makeText(updateProyect.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarProyecto() {
        String nombre = etNombre.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String fechaInicio = etFechaInicio.getText().toString().trim();
        String fechaFin = etFechaFin.getText().toString().trim();

        if (nombre.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        ProyectoUpdateDTO dto = new ProyectoUpdateDTO(nombre, descripcion, fechaInicio, fechaFin);

        ProyectoService service = ApiClient.getClient(this).create(ProyectoService.class);
        service.updateProyecto(idProyecto, dto).enqueue(new Callback<Proyecto>() {
            @Override
            public void onResponse(Call<Proyecto> call, Response<Proyecto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(updateProyect.this, "Proyecto actualizado correctamente", Toast.LENGTH_SHORT).show();
                    finish(); // cerrar al terminar
                } else {
                    Toast.makeText(updateProyect.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Proyecto> call, Throwable t) {
                Toast.makeText(updateProyect.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
