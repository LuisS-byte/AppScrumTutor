package sv.edu.catolica.appscrumtutor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
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
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sv.edu.catolica.appscrumtutor.network.ApiClient;
import sv.edu.catolica.appscrumtutor.usuario.Usuario;
import sv.edu.catolica.appscrumtutor.usuario.UsuarioService;

public class PerfilActivity extends AppCompatActivity {

    private ImageView imgPerfil;
    private TextView tvNombreUsuario, tvCorreo, tvRol, tvEstado;
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

        // Barra inferior
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_profile);
        HeaderController.setup(this);
        NavController.setup(this);

        // Referencias de vista
        imgPerfil = findViewById(R.id.imgPerfil);
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        tvCorreo = findViewById(R.id.tvCorreo);
        tvRol = findViewById(R.id.tvRol);
        tvEstado = findViewById(R.id.tvEstado);
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        // Cargar info del usuario
        cargarDatosUsuario();

        // Botones
        btnEditarPerfil.setOnClickListener(v ->
                Toast.makeText(this, "Función de edición próximamente", Toast.LENGTH_SHORT).show());

        btnCerrarSesion.setOnClickListener(v -> cerrarSesion());
    }

    /** 🔹 Cargar los datos del usuario autenticado **/
    private void cargarDatosUsuario() {
        try {
            String jwt = getSharedPreferences("ScrumTutorPrefs", MODE_PRIVATE)
                    .getString("jwt", null);

            if (jwt == null || jwt.isEmpty()) {
                Toast.makeText(this, "Sesión expirada", Toast.LENGTH_SHORT).show();
                return;
            }

            // Decodificar JWT
            String[] partes = jwt.split("\\.");
            String payloadJson = new String(Base64.decode(partes[1], Base64.URL_SAFE), StandardCharsets.UTF_8);
            JSONObject payload = new JSONObject(payloadJson);
            String correo = payload.getString("sub");

            UsuarioService service = ApiClient.getClient(this).create(UsuarioService.class);
            service.getUsuarioPorCorreo(correo).enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Usuario usuario = response.body();
                        tvNombreUsuario.setText(usuario.getNombre());
                        tvCorreo.setText(usuario.getCorreo());

                        String rolNombre = usuario.getRol() != null ? usuario.getRol().getNombre() : "Sin rol";
                        String estado = usuario.isActivo() ? "Activo ✅" : "Inactivo ⚠️";

                        tvRol.setText("Rol: " + rolNombre);
                        tvEstado.setText("Estado: " + estado);
                    } else {
                        Toast.makeText(PerfilActivity.this, "Error al obtener datos del perfil", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    Toast.makeText(PerfilActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Error al procesar token: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /** 🔹 Cerrar sesión **/
    private void cerrarSesion() {
        getSharedPreferences("ScrumTutorPrefs", MODE_PRIVATE).edit().clear().apply();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("883005246975-641erk7621jujh9854kk96ecb9d4fh7u.apps.googleusercontent.com")
                .requestEmail()
                .build();

        GoogleSignIn.getClient(this, gso)
                .revokeAccess()
                .addOnCompleteListener(task -> {
                    Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                });
    }
}
