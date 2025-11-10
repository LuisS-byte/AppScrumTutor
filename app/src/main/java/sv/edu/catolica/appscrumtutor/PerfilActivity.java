package sv.edu.catolica.appscrumtutor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PerfilActivity extends AppCompatActivity {
    private ImageView imgPerfil;
    private TextView tvNombreUsuario, tvCorreo;
    private Button btnEditarPerfil, btnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_profile); // ← marca el botón actual

        HeaderController.setup(this);
        NavController.setup(this);

        imgPerfil = findViewById(R.id.imgPerfil);
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        tvCorreo = findViewById(R.id.tvCorreo);
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        tvNombreUsuario.setText("José Mendoza");
        tvCorreo.setText("jose@correo.com");

        btnEditarPerfil.setOnClickListener(v ->
                Toast.makeText(this, "Editar perfil (mock)", Toast.LENGTH_SHORT).show());

        btnCerrarSesion.setOnClickListener(v -> {
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}