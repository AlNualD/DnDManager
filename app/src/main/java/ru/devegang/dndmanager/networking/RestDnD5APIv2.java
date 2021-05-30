package ru.devegang.dndmanager.networking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import ru.devegang.dndmanager.entities.Attribute;
import ru.devegang.dndmanager.entities.Character;

public interface RestDnD5APIv2 {
    @POST("v2/dnd5/ru/character/basic")
    Call<Character> createDnDCharacterStep1(@Query("user_id") long user_id, @Body Character character);
    @PUT("v2/dnd5/ru/character/step2")
    Call<Character> createDnDCharacterStep2(@Query("character_id") long character_id, @Query("race") String race, @Query("cclass") String cclass);
    @GET("v2/dnd5/races/ru")
    Call<List<String>> getListOfRaces();
    @GET("v2/dnd5/classes/ru")
    Call<List<String>> getListOfClasses();
    @GET("v2/dnd5/attributes/ru/basic")
    Call<List<Attribute>> getBasicAttributes();
}
