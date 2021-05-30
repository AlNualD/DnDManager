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

    public RestCharacterAPIv2 getRestCharacterAPIv2() {
        return retrofit.create(RestCharacterAPIv2.class);
    }

    public RestDnD5APIv2 getRestDnD5APIv2() {
        return retrofit.create(RestDnD5APIv2.class);
    }

    public RestRollingAPIv2 getRestRollingAPIv2() {
        return retrofit.create(RestRollingAPIv2.class);
    }


}
