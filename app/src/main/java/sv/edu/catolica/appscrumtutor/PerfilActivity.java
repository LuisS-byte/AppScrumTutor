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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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

        btnCerrarSesion.setOnClickListener(v -> cerrarSesion());

    }

    private void cerrarSesion() {
        // 🔹 1. Borrar token JWT guardado
        getSharedPreferences("ScrumTutorPrefs", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        // 🔹 2. Cerrar sesión de Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("513694165460-dh23r13rstm014momd7svpn4gveqso6f.apps.googleusercontent.com")
                .requestEmail()
                .build();

        GoogleSignIn.getClient(this, gso)
                .revokeAccess() // 🔥 importante: revoca los permisos, no solo cierra sesión
                .addOnCompleteListener(task -> {
                    Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();

                    // 3️⃣ Redirigir al login limpiando el historial
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                });
    }
}