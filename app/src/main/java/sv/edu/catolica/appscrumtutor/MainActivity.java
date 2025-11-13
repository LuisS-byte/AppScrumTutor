package sv.edu.catolica.appscrumtutor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import sv.edu.catolica.appscrumtutor.network.AuthResponse;
import sv.edu.catolica.appscrumtutor.network.AuthService;
import sv.edu.catolica.appscrumtutor.network.JwtUtils;
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "GoogleLogin";
    private GoogleSignInClient googleSignInClient;

    // Tu Web Client ID (mismo del backend)
    private static final String WEB_CLIENT_ID = "513694165460-dh23r13rstm014momd7svpn4gveqso6f.apps.googleusercontent.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Verificar si ya hay sesión guardada
        String savedJwt = getSharedPreferences("ScrumTutorPrefs", MODE_PRIVATE)
                .getString("jwt", null);

        if (savedJwt != null && !savedJwt.isEmpty() && !JwtUtils.isTokenExpired(savedJwt)) {
            Log.d(TAG, "Sesión válida detectada, saltando login");
            Intent intent = new Intent(this, ProyectosActivity.class);
            startActivity(intent);
            finish();
            return;
        } else {
            // Si el token está expirado o vacío, lo borramos
            getSharedPreferences("ScrumTutorPrefs", MODE_PRIVATE)
                    .edit()
                    .remove("jwt")
                    .apply();
            Log.d(TAG, "Token expirado o inválido, pidiendo nuevo login");
        }


        ImageButton btnGoogle = findViewById(R.id.btnGoogle);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(WEB_CLIENT_ID)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);


        btnGoogle.setOnClickListener(v -> signIn());
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        signInLauncher.launch(signInIntent);
    }

    private final ActivityResultLauncher<Intent> signInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getData() != null) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    handleSignInResult(task);
                }
            });

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            Log.d(TAG, "Token: " + idToken);
            sendTokenToBackend(idToken);
        } catch (ApiException e) {
            Log.w(TAG, "Fallo en Google Sign-In", e);
            Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendTokenToBackend(String idToken) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://nonimplemental-frowningly-victor.ngrok-free.dev/") //backend expuesto
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AuthService service = retrofit.create(AuthService.class);
        Map<String, String> body = new HashMap<>();
        body.put("idToken", idToken);
        Call<AuthResponse> call = service.loginWithGoogle(body);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    String jwt = authResponse.getJwt();

                    if (jwt != null && !jwt.isEmpty()) {
                        //Limpia cualquier token anterior y guarda el nuevo
                        getSharedPreferences("ScrumTutorPrefs", MODE_PRIVATE)
                                .edit()
                                .clear()
                                .putString("jwt", jwt)
                                .apply();

                        Log.d(TAG, "Nuevo JWT guardado: " + jwt);
                        Toast.makeText(MainActivity.this, "Login exitoso", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this, ProyectosActivity.class);
                        startActivity(intent);
                        finish(); // evita volver al login
                    } else {
                        Toast.makeText(MainActivity.this, "Token JWT no recibido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Simulado(View view) {

        // Simulación de login correcto
        Intent intent = new Intent(this, ProyectosActivity.class);
        startActivity(intent);
        finish(); // opcional: evita volver al login con "back"

    }
}