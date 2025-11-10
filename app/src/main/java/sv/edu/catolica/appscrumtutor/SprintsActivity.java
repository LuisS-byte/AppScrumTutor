package sv.edu.catolica.appscrumtutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SprintsActivity extends AppCompatActivity {

    private LinearLayout layoutSprints;

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



        layoutSprints = findViewById(R.id.layoutSprints);
        Button btnCrearSprint = findViewById(R.id.btnCrearSprint);
        // Solo mostrar botón crear si el usuario tiene permisos
        //btnCrearSprint.setVisibility(esProductOwner ? View.VISIBLE : View.GONE);

        btnCrearSprint.setOnClickListener(v -> {
            // Ir a la misma activity para CREAR un sprint
            Intent intent = new Intent(this, GestionarSprintActivity.class);
            startActivity(intent);
        });

        // Simulamos obtener sprints del proyecto
        String[][] sprints = {
                {"Sprint 1", "Finalizado", "01/10", "15/10"},
                {"Sprint 2", "Activo", "16/10", "31/10"},
                {"Sprint 3", "Planificado", "01/11", "15/11"}
        };

        for (String[] s : sprints) agregarSprint(s[0], s[1], s[2], s[3]);
    }


    private void agregarSprint(String nombre, String estado, String inicio, String fin) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_sprint, layoutSprints, false);

        TextView tvNombre = view.findViewById(R.id.tvNombreSprint);
        TextView tvEstado = view.findViewById(R.id.tvEstado);
        TextView tvFechas = view.findViewById(R.id.tvFechas);
        Button btnVer = view.findViewById(R.id.btnVerSprint);
        Button btnEditar = view.findViewById(R.id.btnEditarSprint); // nuevo botón

        tvNombre.setText(nombre);
        tvEstado.setText(estado);
        tvFechas.setText("📅 " + inicio + " → " + fin);

        // Ver sprint → abre detalle del proyecto con los tabs
        btnVer.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProyectoDetalleActivity.class);
            intent.putExtra("nombreSprint", nombre);
            startActivity(intent);
        });

        // Editar sprint → abre activity de gestión
        btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(this, GestionarSprintActivity.class);
            intent.putExtra("editar", true);
            intent.putExtra("nombre", nombre);
            intent.putExtra("estado", estado);
            intent.putExtra("inicio", inicio);
            intent.putExtra("fin", fin);
            intent.putExtra("objetivo", "Objetivo pendiente de cargar");
            startActivity(intent);
        });
        // Mostrar botón editar solo a Product Owner o Scrum Master
        //btnEditar.setVisibility(esProductOwner ? View.VISIBLE : View.GONE);
        layoutSprints.addView(view);
    }
}