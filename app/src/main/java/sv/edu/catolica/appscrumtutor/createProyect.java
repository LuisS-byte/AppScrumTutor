package sv.edu.catolica.appscrumtutor;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import sv.edu.catolica.appscrumtutor.proyecto.ProyectoCreateDTO;
import sv.edu.catolica.appscrumtutor.proyecto.ProyectoService;

public class createProyect extends AppCompatActivity {

    private EditText etNombre, etDescripcion, etFechaInicio, etFechaFin;
    private Button btnGuardar, btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_proyect);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etNombre = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);
        etFechaInicio = findViewById(R.id.etFechaInicio);
        etFechaFin = findViewById(R.id.etFechaFin);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);

        etFechaInicio.setOnClickListener(v -> mostrarCalendario(etFechaInicio));
        etFechaFin.setOnClickListener(v -> mostrarCalendario(etFechaFin));

        btnGuardar.setOnClickListener(this::crearProyecto);
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void mostrarCalendario(EditText target) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dp = new DatePickerDialog(this,
                (DatePicker view, int y, int m, int d) -> {
                    String fecha = y + "-" + String.format("%02d", (m + 1)) + "-" + String.format("%02d", d);
                    target.setText(fecha);
                }, year, month, day);
        dp.show();
    }

    private void crearProyecto(View v) {
        String nombre = etNombre.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String fechaInicio = etFechaInicio.getText().toString().trim();
        String fechaFin = etFechaFin.getText().toString().trim();

        if (nombre.isEmpty()) {
            etNombre.setError("El nombre es obligatorio");
            return;
        }
        if (fechaInicio.isEmpty()) {
            etFechaInicio.setError("Fecha de inicio obligatoria");
            return;
        }
        if (fechaFin.isEmpty()) {
            etFechaFin.setError("Fecha de fin obligatoria");
            return;
        }

        ProyectoCreateDTO dto = new ProyectoCreateDTO(nombre, descripcion, fechaInicio, fechaFin);

        ProyectoService service = ApiClient.getClient(this).create(ProyectoService.class);
        Call<Proyecto> call = service.crearProyecto(dto);

        call.enqueue(new Callback<Proyecto>() {
            @Override
            public void onResponse(Call<Proyecto> call, Response<Proyecto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(createProyect.this, "Proyecto creado correctamente", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(createProyect.this, ProyectosActivity.class));
                    finish();
                } else {
                    Toast.makeText(createProyect.this, "Error al crear proyecto (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Proyecto> call, Throwable t) {
                Toast.makeText(createProyect.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
