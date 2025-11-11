// sv/edu/catolica/appscrumtutor/auth/DeepLinkActivity.java
package sv.edu.catolica.appscrumtutor.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import sv.edu.catolica.appscrumtutor.ProyectosActivity;

public class DeepLinkActivity extends AppCompatActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri data = getIntent().getData();
        if (data != null) {
            // Esperamos: scrumbtutor://oauth2/callback?token=XYZ
            String token = data.getQueryParameter("token");
            if (token != null && !token.isEmpty()) {
                TokenStore.save(this, token);
            }
        }

        // Ir a la pantalla principal (o donde quieras)
        startActivity(new Intent(this, ProyectosActivity.class));
        finish();
    }
}
