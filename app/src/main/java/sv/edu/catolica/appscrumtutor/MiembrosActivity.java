package sv.edu.catolica.appscrumtutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.List;

public class MiembrosActivity extends AppCompatActivity {


    private LinearLayout layoutMiembros;
    private Button btnAgregarMiembro;


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

        // Simulación de datos (mock)
        List<String[]> miembros = Arrays.asList(
                new String[]{"José Mendoza", "Product Owner", "Activo"},
                new String[]{"Carlos López", "Scrum Master", "Activo"},
                new String[]{"Ana Pérez", "Desarrollador", "Activo"},
                new String[]{"María Gómez", "Desarrollador", "Inactivo"}
        );

        for (String[] m : miembros) {
            agregarMiembro(m[0], m[1], m[2]);
        }

        // Abrir pantalla para agregar un nuevo miembro
        btnAgregarMiembro.setOnClickListener(v -> {
            View dialogView = LayoutInflater.from(this).inflate(R.layout.agregar_miembro, null);
            EditText etCorreo = dialogView.findViewById(R.id.etCorreoMiembro);
            Spinner spRol = dialogView.findViewById(R.id.spRolMiembro);
            Button btnConfirmar = dialogView.findViewById(R.id.btnConfirmarInvitacion);

            android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this)
                    .setView(dialogView)
                    .create();

            btnConfirmar.setOnClickListener(btn -> {
                String correo = etCorreo.getText().toString().trim();
                String rol = spRol.getSelectedItem().toString();

                if (correo.isEmpty()) {
                    etCorreo.setError("Campo obligatorio");
                    return;
                }

                // Simular agregar miembro
                agregarMiembro(correo.split("@")[0], rol, "Activo");
                Toast.makeText(this, "Miembro invitado: " + correo, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });

            dialog.show();
        });


    }



    // Método auxiliar para inflar la vista de un miembro
    private void agregarMiembro(String nombre, String rol, String estado) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_miembro, layoutMiembros, false);

        TextView tvNombre = view.findViewById(R.id.tvNombreMiembro);
        TextView tvRol = view.findViewById(R.id.tvRolMiembro);
        TextView tvEstado = view.findViewById(R.id.tvEstadoMiembro);
        Button btnEliminar = view.findViewById(R.id.btnEliminarMiembro);

        tvNombre.setText(nombre);
        tvRol.setText("Rol: " + rol);
        tvEstado.setText(estado);

        // Colorear estado
        if (estado.equalsIgnoreCase("inactivo")) {
            tvEstado.setTextColor(0xFFF44336); // rojo
        } else {
            tvEstado.setTextColor(0xFF4CAF50); // verde
        }

        // Simulación: eliminar miembro
        btnEliminar.setOnClickListener(v -> {
            layoutMiembros.removeView(view);
            Toast.makeText(this, nombre + " eliminado (mock)", Toast.LENGTH_SHORT).show();
        });

        layoutMiembros.addView(view);
    }

}