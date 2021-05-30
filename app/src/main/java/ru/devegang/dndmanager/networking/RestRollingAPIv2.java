package ru.devegang.dndmanager.networking;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.devegang.dndmanager.entities.rolls.RollingResult;

public interface RestRollingAPIv2 {
    @GET("v2/roll/str")
    Call<RollingResult> simpleRoll(@Query("formula") String formula);
}
