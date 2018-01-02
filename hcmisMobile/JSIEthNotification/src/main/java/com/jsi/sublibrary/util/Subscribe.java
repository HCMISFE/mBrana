package com.jsi.sublibrary.util;

        import android.util.Log;
        import android.widget.Toast;
        import com.jsi.sublibrary.util.remote.ApiUtils;
        import com.jsi.sublibrary.util.remote.NotificationService;
        import com.jsi.sublibrary.util.remote.model.User;

        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;

/**
 * Created by Nebyu Dawit on 8/23/17.
 *
 */
public class Subscribe {

    public static final String TAG = "Subscribe";
    private NotificationService daguService = ApiUtils.getDaguService();


    /**
     *  Register the user specified to Jsi Notification server
     *
     * @param username
     * @param userToken
     * @param applicationCode
     * @param applicationName
     */
    public void subscribe(String username,String userToken,String applicationCode,String applicationName) {
        User user = new User(username,userToken,applicationCode,applicationName);
        daguService.registerUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                showResponse("onResponse method");
                if(response.isSuccessful()) {
                    // TODO implementation for success response
                    showResponse("is successful : " + response.body().toString());
                }else if(response.code() == 200) {
                    showResponse("is successful : " + response.body().toString());
                }
                showResponse(response.code() + " : ");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showResponse("Failed to subscribe user to Dagu server.");
            }
        });
    }

    public void showResponse(String response) {
        Log.d("Subscribe",response);
    }

}
