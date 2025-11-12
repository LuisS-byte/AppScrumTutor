package sv.edu.catolica.appscrumtutor.network;

import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

public class JwtUtils {

    public static boolean isTokenExpired(String token) {
        try {
            if (token == null || token.isEmpty()) return true;

            String[] parts = token.split("\\.");
            if (parts.length < 2) return true;

            String payloadJson = new String(Base64.decode(parts[1], Base64.URL_SAFE));
            JSONObject payload = new JSONObject(payloadJson);

            long exp = payload.getLong("exp"); // segundos UNIX
            long now = System.currentTimeMillis() / 1000; // ahora en segundos

            return now >= exp;
        } catch (Exception e) {
            Log.e("JwtUtils", "Error leyendo token: " + e.getMessage());
            return true; // por seguridad, si falla, lo tratamos como expirado
        }
    }
}
