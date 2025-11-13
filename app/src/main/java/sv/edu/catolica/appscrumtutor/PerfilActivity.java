package sv.edu.catolica.appscrumtutor;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sv.edu.catolica.appscrumtutor.network.ApiClient;
import sv.edu.catolica.appscrumtutor.rol.Rol;
import sv.edu.catolica.appscrumtutor.rol.RolService;
import sv.edu.catolica.appscrumtutor.usuario.Usuario;
import sv.edu.catolica.appscrumtutor.usuario.UsuarioService;
import sv.edu.catolica.appscrumtutor.usuario.UsuarioUpdateDTO;

public class PerfilActivity extends AppCompatActivity {

    private ImageView imgPerfil;
    private TextView tvNombreUsuario, tvCorreo, tvRol, tvEstado;
    private Button btnEditarPerfil, btnCerrarSesion;

    private long idUsuarioActual;
    private Usuario usuarioActual;

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

        imgPerfil = findViewById(R.id.imgPerfil);
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        tvCorreo = findViewById(R.id.tvCorreo);
        tvRol = findViewById(R.id.tvRol);
        tvEstado = findViewById(R.id.tvEstado);
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        cargarImagenGoogle();
        cargarDatosUsuario();

        btnEditarPerfil.setOnClickListener(v -> mostrarModalEditarPerfil());
        btnCerrarSesion.setOnClickListener(v -> cerrarSesion());
    }

    /** 🔹 Mostrar imagen Google o por defecto **/
    private void cargarImagenGoogle() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null && account.getPhotoUrl() != null) {
            Uri fotoUri = account.getPhotoUrl();
            Glide.with(this)
                    .load(fotoUri)
                    .placeholder(R.drawable.logo_default)
                    .circleCrop()
                    .into(imgPerfil);
        } else {
            imgPerfil.setImageResource(R.drawable.logo_default);
        }
    }

    /** 🔹 Cargar datos del usuario autenticado **/
    private void cargarDatosUsuario() {
        try {
            String jwt = getSharedPreferences("ScrumTutorPrefs", MODE_PRIVATE)
                    .getString("jwt", null);
            if (jwt == null || jwt.isEmpty()) return;

            String[] partes = jwt.split("\\.");
            String payloadJson = new String(Base64.decode(partes[1], Base64.URL_SAFE), StandardCharsets.UTF_8);
            JSONObject payload = new JSONObject(payloadJson);
            String correo = payload.getString("sub");

            UsuarioService service = ApiClient.getClient(this).create(UsuarioService.class);
            service.getUsuarioPorCorreo(correo).enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        usuarioActual = response.body();
                        idUsuarioActual = usuarioActual.getIdUsuario();

                        tvNombreUsuario.setText(usuarioActual.getNombre());
                        tvCorreo.setText(usuarioActual.getCorreo());

                        String rolNombre = usuarioActual.getRol() != null ? usuarioActual.getRol().getNombre() : "Sin rol";
                        String estado = usuarioActual.isActivo() ? "Activo ✅" : "Inactivo ⚠️";

                        tvRol.setText("Rol: " + rolNombre);
                        tvEstado.setText("Estado: " + estado);
                    } else {
                        Toast.makeText(PerfilActivity.this, "Error al obtener usuario", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    Toast.makeText(PerfilActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error al procesar sesión: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /** 🔹 Mostrar modal para editar nombre y rol **/
    private void mostrarModalEditarPerfil() {
        if (usuarioActual == null) {
            Toast.makeText(this, "Datos no disponibles", Toast.LENGTH_SHORT).show();
            return;
        }

        // Inflar vista personalizada
        LayoutInflater inflater = LayoutInflater.from(this);
        android.view.View dialogView = inflater.inflate(R.layout.dialog_editar_perfil, null);

        EditText etNombre = dialogView.findViewById(R.id.etNombre);
        Spinner spRol = dialogView.findViewById(R.id.spRol);
        Button btnGuardar = dialogView.findViewById(R.id.btnGuardar);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);

        etNombre.setText(usuarioActual.getNombre());

        RolService rolService = ApiClient.getClient(this).create(RolService.class);
        rolService.getRoles().enqueue(new Callback<List<Rol>>() {
            @Override
            public void onResponse(Call<List<Rol>> call, Response<List<Rol>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Rol> roles = response.body();

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            PerfilActivity.this,
                            android.R.layout.simple_spinner_item,
                            roles.stream().map(Rol::getNombre).collect(Collectors.toList())
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spRol.setAdapter(adapter);

                    if (usuarioActual.getRol() != null) {
                        for (int i = 0; i < roles.size(); i++) {
                            if (roles.get(i).getIdRol().equals(usuarioActual.getRol().getIdRol())) {
                                spRol.setSelection(i);
                                break;
                            }
                        }
                    }

                    AlertDialog dialog = new AlertDialog.Builder(PerfilActivity.this)
                            .setView(dialogView)
                            .setCancelable(false)
                            .create();

                    btnCancelar.setOnClickListener(v -> dialog.dismiss());
                    btnGuardar.setOnClickListener(v -> {
                        String nuevoNombre = etNombre.getText().toString().trim();
                        Rol rolSeleccionado = roles.get(spRol.getSelectedItemPosition());
                        actualizarPerfil(nuevoNombre, rolSeleccionado.getIdRol());
                        dialog.dismiss();
                    });

                    dialog.show();
                } else {
                    Toast.makeText(PerfilActivity.this, "Error al cargar roles", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Rol>> call, Throwable t) {
                Toast.makeText(PerfilActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** 🔹 PUT al backend para actualizar **/
    private void actualizarPerfil(String nombre, Long idRol) {
        UsuarioService usuarioService = ApiClient.getClient(this).create(UsuarioService.class);
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO(nombre, idRol);

        usuarioService.actualizarUsuario(idUsuarioActual, dto).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PerfilActivity.this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show();
                    cargarDatosUsuario();
                } else {
                    Toast.makeText(PerfilActivity.this, "Error al actualizar perfil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(PerfilActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
