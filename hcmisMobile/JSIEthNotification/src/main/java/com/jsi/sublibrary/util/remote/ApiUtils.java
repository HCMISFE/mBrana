package com.jsi.sublibrary.util.remote;

import retrofit2.Retrofit;

/**
 * Created by baba on 11/21/2017.
 */

public class ApiUtils {

    public static final String BASE_URL = "http://35.184.137.237:8250/api/Subscription/";

    public static NotificationService getDaguService() {
        return RetrofitClient.getClient(BASE_URL).create(NotificationService.class);
    }

}
