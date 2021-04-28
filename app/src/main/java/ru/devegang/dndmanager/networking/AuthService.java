package ru.devegang.dndmanager.networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthService {
    private static AuthService mInstance;
    private static String BASE_URL = "https://flexcharactersheet.herokuapp.com/api/";
    private Retrofit retrofit;
    private AuthService() {
        retrofit = new  Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static AuthService getInstance() {
        if (mInstance == null) {
            mInstance = new AuthService();
        }
        return mInstance;
    }

    public RestUserAPI getRestUserAPI() {
        return retrofit.create(RestUserAPI.class);
    }
}
