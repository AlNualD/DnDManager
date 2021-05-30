package ru.devegang.dndmanager.networking;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.devegang.dndmanager.entities.User;

public interface RestUserAPIv2 {

    @PUT
    Call<Void> updateUser(@Query("user_id") long user_id, @Body User user);


    @GET("v2/users/login/{login}")
    Call<User> loginUser(@Path("login") String login);

    @GET("v2/users/{user_id}")
    Call<User> getUser(@Path("user_id") long user_id);

//    @POST("v1/users")
//    Call<ResponseBody> registerUser(@Body User user);
//

    @POST("v2/users")
    Call<User> registerUser(@Body User user);


}
