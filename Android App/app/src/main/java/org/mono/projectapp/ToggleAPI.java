package org.mono.projectapp;

/**
 * Created by ManajitPal on 31-03-2017.
 */

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;


public interface ToggleAPI {
    @FormUrlEncoded
    @POST("/smartirrigation/toggle_switch.php") //Send toggledata to the server
    public void insert(
            @Field("switch") int toggle,
            Callback<Response> callback);

}
