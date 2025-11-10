package sv.edu.catolica.appscrumtutor;

import android.app.Activity;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class HeaderController {

    public static void setup(Activity activity) {
        EditText etBuscar = activity.findViewById(R.id.etBuscar);
        ImageView ivPerfil = activity.findViewById(R.id.ivPerfil);

        if (etBuscar != null) {
            etBuscar.setOnEditorActionListener((v, actionId, event) -> {
                String query = etBuscar.getText().toString().trim();
                if (!query.isEmpty()) {
                    Toast.makeText(activity, "Buscando: " + query, Toast.LENGTH_SHORT).show();
                }
                return true;
            });
        }

        if (ivPerfil != null) {
            ivPerfil.setOnClickListener(v -> {
                Intent intent = new Intent(activity, PerfilActivity.class);
                activity.startActivity(intent);
            });
        }
    }
}
