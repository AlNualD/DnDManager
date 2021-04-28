package ru.devegang.dndmanager.networking;

import java.util.List;

import lombok.experimental.Delegate;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.devegang.dndmanager.entities.Character;
import ru.devegang.dndmanager.entities.Item;
import ru.devegang.dndmanager.entities.Skill;
import ru.devegang.dndmanager.entities.Spell;

public interface RestCharacterAPI {
    @GET("v1/users/characters/{id}")
    Call<List<Character>> getCharactersList(@Path("id") long id);

    @POST("v1/characters")
    Call<ResponseBody> postCharacter(@Query("id") long userId, @Body Character character);

    @GET("v1/characters/{id}")
    Call<Character> getCharacter(@Path("id") long characterId);

    @PUT("v1/characters/")
    Call<ResponseBody> updateCharacter(@Body Character character);

    @DELETE("v1/characters/")
    Call<ResponseBody> deleteCharacter(@Query("characterID") long characterId);

    @GET("vi/characters/spells")
    Call<List<Spell>> getCharacterSpells(@Query("id") long characterId);

    @GET("v1/characters/spells/user")
    Call<List<Spell>> getUserSpells(@Query("userID ") long userId);

    @GET("v1/characters/spells/{id}")
    Call<Spell> getSpell(@Path("id") long characterId);

    @POST("v1/characters/spells")
    Call<ResponseBody> createSpell(@Query("id") long characterId, @Body Spell spell);

    @PUT("v1/characters/spells")
    Call<ResponseBody> updateSpell(@Query("id") long spellId, @Body Spell spell);

    @DELETE("v1/characters/spells")
    Call<ResponseBody> deleteSpell(@Query("id") long spellID);

    @GET("v1/characters/skills")
    Call<List<Skill>> getCharacterSkills(@Query("id") long characterId);

    @GET("v1/characters/skills/user")
    Call<List<Skill>> getUserSkills(@Query("userID") long userId);

    @GET("v1/characters/skills/{id}")
    Call<Skill> getSkill(@Path("id") long characterId);

    @POST("v1/characters/skills")
    Call<ResponseBody> createSkill(@Query("id") long characterId, @Body Skill skill);

    @PUT("v1/characters/skills")
    Call<ResponseBody> updateSkill(@Query("id") long skillId, @Body Skill skill);

    @DELETE("v1/characters/skills")
    Call<ResponseBody> deleteSkill(@Query("id") long skillId);

    @PUT("v1/characters/health")
    Call<ResponseBody> updateHealth(@Query("id") long characterId, @Query("newCur") int cur, @Query("newMax") int max);

    @GET("v1/characters/inventory")
    Call<List<Item>> getInventory(@Query("id") long characterId);

    @POST("v1/characters/inventory")
    Call<ResponseBody> addItem(@Query("id") long characterId, @Body Item item);

    @DELETE("v1/characters/inventory")
    Call<ResponseBody> deleteItem(@Query("itemID") long itemId);

}
