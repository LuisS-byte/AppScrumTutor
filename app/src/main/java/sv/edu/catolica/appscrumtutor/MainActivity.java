package sv.edu.catolica.appscrumtutor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import sv.edu.catolica.appscrumtutor.auth.TokenStore;

public class MainActivity extends AppCompatActivity {

    private static final String LOGIN_URL =
            "https://lon-macropodous-yanira.ngrok-free.dev/oauth2/authorization/google";

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

        Toast.makeText(this, "📱 App iniciada", Toast.LENGTH_SHORT).show();

        String token = TokenStore.get(this);
        if (token != null && !token.isEmpty()) {
            Toast.makeText(this, "🔑 Token ya guardado, saltando login", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ProyectosActivity.class));
            finish();
        }
    }

    public void Google(android.view.View view) {
        try {
            Toast.makeText(this, "🌐 Abriendo navegador para login...", Toast.LENGTH_SHORT).show();
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(LOGIN_URL));
        } catch (Exception e) {
            Toast.makeText(this, "⚠️ Error al abrir navegador: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();

        if (uri == null) {
            Toast.makeText(this, "❌ No llegó ningún intent URI", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "📩 URI recibido: " + uri.toString(), Toast.LENGTH_LONG).show();

        if ("appscrumtutor".equals(uri.getScheme())) {
            String token = uri.getQueryParameter("token");
            if (token != null && !token.isEmpty()) {
                Toast.makeText(this, "✅ Token recibido y guardado", Toast.LENGTH_SHORT).show();
                TokenStore.save(this, token);
                startActivity(new Intent(this, ProyectosActivity.class));
                finish();
            } else {
                Toast.makeText(this, "⚠️ Token vacío", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "⚠️ Esquema incorrecto: " + uri.getScheme(), Toast.LENGTH_SHORT).show();
        }
    }

}
