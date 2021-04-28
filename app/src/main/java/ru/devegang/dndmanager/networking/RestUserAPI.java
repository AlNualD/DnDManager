package ru.devegang.dndmanager.networking;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.devegang.dndmanager.entities.User;

public interface RestUserAPI {
    @GET("v1/users/login/{login}")
    Call<User> loginUser(@Path("login") String login);

    @POST("v1/users")
    Call<ResponseBody> registerUser(@Body User user);


}
