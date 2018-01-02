package com.jsi.sublibrary.util.remote;

import com.jsi.sublibrary.util.remote.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by baba on 11/21/2017.
 */

public interface NotificationService {

    @POST("RegisterUser")
    Call<User> registerUser(@Body User user);


}
