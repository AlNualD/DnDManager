package ru.devegang.dndmanager.networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService mInstance;
    private static String BASE_URL = "https://flexcharactersheet.herokuapp.com/api/";
    private Retrofit retrofit;
    private NetworkService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public RestCharacterAPI getRestCharacterAPI() {
        return  retrofit.create(RestCharacterAPI.class);
    }


}
