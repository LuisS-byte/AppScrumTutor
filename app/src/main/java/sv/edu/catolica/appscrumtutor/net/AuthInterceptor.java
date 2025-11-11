package sv.edu.catolica.appscrumtutor.net;

import android.content.Context;
import androidx.annotation.NonNull;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import sv.edu.catolica.appscrumtutor.auth.TokenStore;

public class AuthInterceptor implements Interceptor {
    private final Context appCtx;

    public AuthInterceptor(Context ctx) {
        this.appCtx = ctx.getApplicationContext();
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        String token = TokenStore.get(appCtx);
        Request req = chain.request();
        if (token != null && !token.isEmpty()) {
            req = req.newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
        }
        return chain.proceed(req);
    }
}
