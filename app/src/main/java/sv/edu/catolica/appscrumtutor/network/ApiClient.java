package sv.edu.catolica.appscrumtutor.network;

import android.content.Context;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // private static final String BASE_URL = "https://lon-macropodous-yanira.ngrok-free.dev/";
    private static final String BASE_URL = "https://nonimplemental-frowningly-victor.ngrok-free.dev/";
    private static Retrofit retrofit = null;
    private static String lastToken = null;

    public static Retrofit getClient(Context context) {

        String token = context.getSharedPreferences("ScrumTutorPrefs", Context.MODE_PRIVATE)
                .getString("jwt", null);

        if (retrofit == null || (token != null && !token.equals(lastToken))) {
            lastToken = token;

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(chain -> {
                        Request.Builder builder = chain.request().newBuilder();
                        if (token != null && !token.isEmpty()) {
                            builder.addHeader("Authorization", "Bearer " + token);
                        }
                        return chain.proceed(builder.build());
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }

        return retrofit;
    }

    public static void clearInstance() {

        retrofit = null;
        lastToken = null;

    }
}
