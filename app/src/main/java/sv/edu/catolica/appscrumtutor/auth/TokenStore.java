package sv.edu.catolica.appscrumtutor.auth;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenStore {
    private static final String PREFS = "auth_prefs";
    private static final String KEY_TOKEN = "jwt_token";

    public static void save(Context ctx, String token) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_TOKEN, token).apply();
    }

    public static String get(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return prefs.getString(KEY_TOKEN, null);
    }

    public static void clear(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        prefs.edit().remove(KEY_TOKEN).apply();
    }
}
