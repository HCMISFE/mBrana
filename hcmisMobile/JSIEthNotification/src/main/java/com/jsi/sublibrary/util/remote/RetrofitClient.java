package com.jsi.sublibrary.util.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by baba on 11/21/2017.
 */

public class RetrofitClient {

    private static Retrofit retrofit = null;
    private static String baseUrl = "";

    public static Retrofit getClient(String baseUrl) {
        if(retrofit == null) {
            getRetrofitInstance(baseUrl);
        }else if(!RetrofitClient.baseUrl.equals(baseUrl)) {
            getRetrofitInstance(baseUrl);
        }
        return retrofit;
    }

    private static Retrofit getRetrofitInstance(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitClient.baseUrl = baseUrl;
        return retrofit;
    }

}
