package sv.edu.catolica.appscrumtutor.network;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.*;
public interface AuthService {
    @POST("api/auth/google")
    Call<AuthResponse> loginWithGoogle(@Body Map<String, String> body);
}
