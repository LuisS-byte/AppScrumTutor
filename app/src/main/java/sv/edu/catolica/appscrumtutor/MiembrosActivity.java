package sv.edu.catolica.appscrumtutor;

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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sv.edu.catolica.appscrumtutor.miembros.Miembro;
import sv.edu.catolica.appscrumtutor.miembros.MiembroService;
import sv.edu.catolica.appscrumtutor.network.ApiClient;

public class MiembrosActivity extends AppCompatActivity {

    private LinearLayout layoutMiembros;
    private Button btnAgregarMiembro;
    private long idProyecto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_miembros);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        layoutMiembros = findViewById(R.id.layoutMiembros);
        btnAgregarMiembro = findViewById(R.id.btnAgregarMiembro);

        idProyecto = getIntent().getLongExtra("idProyecto", -1L);

        if (idProyecto == -1L) {
            Toast.makeText(this, "ID de proyecto no recibido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cargarMiembros();

        btnAgregarMiembro.setOnClickListener(v ->
                Toast.makeText(this, "Función agregar miembro (pendiente)", Toast.LENGTH_SHORT).show()
        );
    }

    private void cargarMiembros() {
        MiembroService service = ApiClient.getClient(this).create(MiembroService.class);
        service.getMiembros(idProyecto).enqueue(new Callback<List<Miembro>>() {
            @Override
            public void onResponse(Call<List<Miembro>> call, Response<List<Miembro>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    layoutMiembros.removeAllViews();
                    List<Miembro> miembros = response.body();

                    if (miembros.isEmpty()) {
                        mostrarSinMiembros();
                    } else {
                        for (Miembro m : miembros) {
                            agregarMiembro(m.getUsuarioNombre(), m.getRolDescripcion(), "Activo");
                        }
                    }
                } else {
                    Toast.makeText(MiembrosActivity.this, "Error al cargar miembros", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Miembro>> call, Throwable t) {
                Toast.makeText(MiembrosActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarSinMiembros() {
        TextView msg = new TextView(this);
        msg.setText("No hay miembros en este proyecto");
        msg.setTextSize(16);
        msg.setPadding(0, 24, 0, 24);
        msg.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
        layoutMiembros.addView(msg);
    }

    private void agregarMiembro(String nombre, String rol, String estado) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_miembro, layoutMiembros, false);

        TextView tvNombre = view.findViewById(R.id.tvNombreMiembro);
        TextView tvRol = view.findViewById(R.id.tvRolMiembro);
        TextView tvEstado = view.findViewById(R.id.tvEstadoMiembro);
        Button btnEliminar = view.findViewById(R.id.btnEliminarMiembro);

        tvNombre.setText(nombre);
        tvRol.setText("Rol: " + rol);
        tvEstado.setText(estado);
        tvEstado.setTextColor(estado.equalsIgnoreCase("inactivo") ? 0xFFF44336 : 0xFF4CAF50);

        btnEliminar.setOnClickListener(v -> {
            Toast.makeText(this, "Eliminar miembro pendiente de implementar", Toast.LENGTH_SHORT).show();
        });

        layoutMiembros.addView(view);
    }
}
