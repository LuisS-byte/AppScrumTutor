package sv.edu.catolica.appscrumtutor.network;

import android.content.Context;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://nonimplemental-frowningly-victor.ngrok-free.dev/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient(Context context) {
        // Lee el JWT almacenado localmente
        String token = context.getSharedPreferences("ScrumTutorPrefs", Context.MODE_PRIVATE)
                .getString("jwt", null);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(chain -> {
                    Request.Builder requestBuilder = chain.request().newBuilder();
                    if (token != null && !token.isEmpty()) {
                        requestBuilder.addHeader("Authorization", "Bearer " + token);
                    }
                    return chain.proceed(requestBuilder.build());
                });

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client.build())
                    .build();
        }
        return retrofit;
    }

    public static void clearInstance() {
        retrofit = null;
    }
}
