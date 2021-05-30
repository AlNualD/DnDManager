package ru.devegang.dndmanager.networking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.devegang.dndmanager.entities.Attribute;
import ru.devegang.dndmanager.entities.Character;
import ru.devegang.dndmanager.entities.Item;
import ru.devegang.dndmanager.entities.Skill;
import ru.devegang.dndmanager.entities.Spell;

public interface RestCharacterAPIv2 {
    @PUT("v2/characters")
    Call<Void> updateCharacter(@Query("character_id") long id, @Body Character character);
    @POST("v2/characters")
    Call<Character> createCharacter(@Query("user_id") long user_id, @Body Character character);
    @DELETE("v2/characters")
    Call<Void> deleteCharacter(@Query("character_id") long id);
    @GET("v2/characters/spells")
    Call<List<Spell>> getCharacterSpells(@Query("character_id") long id);
    @PUT("v2/characters/spells")
    Call<Spell> updateSpell(@Query("spell_id") long spell_id, @Body Spell spell);
    @POST("v2/characters/spells")
    Call<Void> createSpell(@Query("character_id") long character_id, @Body Spell spell);
    @DELETE("v2/characters/spells")
    Call<Void> deleteSpell(@Query("spell_id") long spell_id);
    @GET("v2/characters/skills")
    Call<List<Skill>> getCharacterSkills(@Query("character_id") long character_id);
    @PUT("v2/characters/skills")
    Call<Void> updateSkill(@Query("skill_id") long skill_id, @Body Skill skill);
    @POST("v2/characters/skills")
    Call<Skill> createSkill(@Query("character_is") long character_id, @Body Skill skill);
    @DELETE("v2/characters/skills")
    Call<Void> deleteSkill(@Query("skill_id") long skill_id);
    @GET("v2/characters/skills/attribute")
    Call<Attribute> getSkillAttribute(@Query("skill_id") long skill_id);
    @PUT("v2/characters/skills/attribute")
    Call<Void> addAttributeToSkill(@Query("skill_id") long skill_id, @Query("attribute_id") long attribute_id);
    @GET("v2/characters/inventory")
    Call<List<Item>> getCharacterItems(@Query("character_id") long character_id);
    @PUT("v2/characters/inventory")
    Call<Void> updateItem(@Query("item_id") long item_id, @Body Item item);
    @POST("v2/characters/inventory")
    Call<Item> createItem(@Query("character_id") long character_id, @Body Item item);
    @DELETE("v2/characters/inventory")
    Call<Void> deleteItem(@Query("item_id") long item_id);
    @GET("v2/characters/attributes")
    Call<List<Attribute>> getCharacterAttributes(@Query("character_id") long character_id);
    @PUT("v2/characters/attributes")
    Call<Void> updateCharacterAttribute(@Query("attribute_id") long attribute_id);
    @POST("v2/characters/attributes")
    Call<Attribute> createAttribute(@Query("character_id")long character_id, @Body Attribute attribute);
    @POST("v2/characters/skills/{attribute_id}")
    Call<Skill> createSkill(@Path("attribute_id") long attribute_id, @Query("character_id") long character_id, @Body Skill skill);
    @POST("v2/characters/skills/attributed")
    Call<Skill> createSkill(@Query("character_id") long character_id, @Body Attribute attribute, @Body Skill skill);
    @GET("v2/characters/{id}")
    Call<Character> getCharacter(@Path("id") long character_id);
    @GET("v2/characters/skills/{id}")
    Call<Skill> getSkill(@Path("id") long skill_id);
    @GET("v2/characters/spells/{id}")
    Call<Spell> getSpell(@Path("id") long spell_id);
    @GET("v2/characters/skills/user")
    Call<List<Skill>> getUserSkills(@Query("user_id") long user_id);
    @GET("v2/characters/spells/user")
    Call<List<Spell>> getUserSpells(@Query("user_id") long user_id);

    @GET("v2/users/characters/{user_id}")
    Call<List<Character>> getUserCharacters(@Path("user_id") long user_id);


    @GET("v2/characters/attribute/{id}")
    Call<Attribute> getAttribute(@Path("id") long attribute_id);
    @GET("v2/characters/inventory/{id}")
    Call<Item> getItem(@Path("id") long item_id);
    @DELETE("v2/characters/attribute")
    Call<Void> deleteAttribute(@Query("attribute_id") long attribute_id);

    @PUT("v2/characters/hp")
    Call<Void> changeHp(@Query("character_id") long character_id, @Query("hp") int hp);

    @PUT("v2/characters/lvlup")
    Call<Void> lvlUp(@Query("character_id") long character_id);



}
