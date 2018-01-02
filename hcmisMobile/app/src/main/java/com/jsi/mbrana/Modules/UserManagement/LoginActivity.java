package com.jsi.mbrana.Modules.UserManagement;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jsi.mbrana.Workflow.Adapter.Lookup.EnvironmentCustomAdapter;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.CommonUIComponents.mBranaNotification;
import com.jsi.mbrana.DataAccessLayer.DataServiceAgent;
import com.jsi.mbrana.DataAccessLayer.DataServiceInterface;
import com.jsi.mbrana.FCM.MyFirebaseMessagingService;
import com.jsi.mbrana.Helpers.Constants;
import com.jsi.mbrana.Helpers.GlobalVariables;
import com.jsi.mbrana.Helpers.Helper;
import com.jsi.mbrana.Helpers.NetworkTypeHelper;
import com.jsi.mbrana.Preferences.PreferenceKey;
import com.jsi.mbrana.Preferences.PreferenceManager;
import com.jsi.mbrana.CommonModels.LoginModel;
import com.jsi.mbrana.CommonModels.UserIdentityModel;
import com.jsi.mbrana.Models.EnvironmentModel;
import com.jsi.mbrana.R;
import com.jsi.mbrana.Extensions.CustomPhoneStateListener;
import com.jsi.mbrana.Workflow.Update.UpdateActivity;
import com.jsi.sublibrary.util.Subscribe;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import test.jinesh.easypermissionslib.EasyPermission;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements EasyPermission.OnPermissionResult {
    PackageInfo pInfo;
    Button mLoginButton;
    Spinner environment_list;
    ArrayList<EnvironmentModel> EnvironmentList = new ArrayList<>();
    int environment_position;
    int _signal_strength = -1;
    AutoCompleteTextView mUserNameView;
    EditText mPasswordView;
    CheckBox showPassword, rememberMeCheckBox;
    String usernameInput = "", passwordInput = "";
    TextView tv_versionLable;
    Tracker _mTracker;
    ImageView Img_JSI;
    BroadcastReceiver _receiver;
    boolean isReceiverRegistered = false;
    ViewGroup login_container;
    DataServiceInterface dataService;
    mBranaNotification notification;
    AVLoadingIndicatorView login_loading;
    EasyPermission easyPermission;
    PreferenceManager preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        //Set Layout for Login
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //EastPermission
        easyPermission = new EasyPermission();

        //
        preference = new PreferenceManager(this);

        //Notification
        notification = new mBranaNotification(this, findViewById(android.R.id.content));

        //Retrofit API
        dataService = DataServiceAgent.getDataService(this);

        //User Account
        try {
            usernameInput = preference.getString(PreferenceKey.Username, "");
            passwordInput = preference.getString(PreferenceKey.Password, "");
        } catch (Exception ex) {
            ex.printStackTrace();
            _mTracker.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(new StandardExceptionParser(LoginActivity.this, null)
                            .getDescription(Thread.currentThread().getName(), ex))
                    .setFatal(false)
                    .build());
        }

        usernameInput = "baakal";
        passwordInput = "pass2pass";

        //Login Container
        login_container = (ViewGroup) findViewById(R.id.login_container);

        //Show version lable
        tv_versionLable = (TextView) findViewById(R.id.versionLable);
        tv_versionLable.setText("Version: " + getVersionName() + "");

        //User name EditText
        mUserNameView = (AutoCompleteTextView) findViewById(R.id.username);
        mUserNameView.setText(usernameInput);

        //Remember Me Checkbox
        rememberMeCheckBox = (CheckBox) findViewById(R.id.checkbox_rememberme);

        //Show Password Checkbox
        showPassword = (CheckBox) findViewById(R.id.showPassword);
        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPasswordView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    mPasswordView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        //Site Selection
        environment_list = (Spinner) findViewById(R.id.EnvironmentListLogin);
        environment_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                environment_position = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Password EditText
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setText(passwordInput);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_ACTION_GO) {
                    //HIde keyboard
                    Helper.hideKeyboard(LoginActivity.this);
                    //noinspection NewApi
                    BuildLoginModel(false);
                    return true;
                }
                return false;
            }
        });

        //Login Button & Loading
        login_loading = (AVLoadingIndicatorView) findViewById(R.id.login_loading);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //HIde keyboard
                Helper.hideKeyboard(LoginActivity.this);
                //noinspection NewApi
                BuildLoginModel(false);
            }

        });

        //JSI Logo
        Img_JSI = (ImageView) findViewById(R.id.JSI_Logo);
        Img_JSI.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (environment_list.getVisibility() == View.GONE) {
                    environment_list.setVisibility(View.VISIBLE);
                } else if (environment_list.getVisibility() == View.VISIBLE) {
                    environment_list.setVisibility(View.GONE);
                }
                return true;
            }
        });

        //Check for registered permissions
        checkAllPermissions();

        //Register PhoneState Listener & Ignore
        if (NetworkTypeHelper.isConnected(this)) {
            if (NetworkTypeHelper.isConnectedWifi(this)) {
                hideLoadingAnimation();
                GlobalVariables.setSignalValueReceived(true);
                isReceiverRegistered = false;
                _signal_strength = 0;
            } else {
                registerPhoneStateListener();
            }
        }

        //Check for update
        CheckAppUpdate();
    }

    public void BuildLoginModel(boolean isFromPreviousLogin) {
        LoginModel login_model = new LoginModel();

        if (NetworkTypeHelper.isConnected(this)) {
            try {
                // Device type
                login_model.setDeviceType("Mobile");

                // Network Type
                NetworkInfo n_info = NetworkTypeHelper.getNetworkInfo(this);
                String NetworkType = NetworkTypeHelper.ConnectionToNetworkType(n_info.getType(), n_info.getSubtype());
                login_model.setNetworkType(NetworkType);

                // Signal Strength
                login_model.setSignalStrength(String.valueOf(_signal_strength));

                // Version Status
                login_model.setStatus(preference.getString(PreferenceKey.VersionStatus, "UNKNOWN"));

                // Phone Model
                String PhoneModel = NetworkTypeHelper.getDeviceName();
                login_model.setPhoneModel(PhoneModel);

                // IMEI
                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (tm != null) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                            int no_SIM = tm.getPhoneCount();

                            if (no_SIM == 1) {
                                login_model.setIMEID1(tm.getDeviceId(0));
                            } else if (no_SIM == 2) {
                                login_model.setIMEID1(tm.getDeviceId(0));
                                login_model.setIMEID2(tm.getDeviceId(1));
                            }
                        } else {
                            checkAllPermissions();
                        }
                    } else {
                        login_model.setIMEID1(tm.getDeviceId());
                    }
                }

                if (isFromPreviousLogin) {
                    //EnvironmentCode
                    if (environment_list.getVisibility() == View.VISIBLE && EnvironmentList.size() > 0) {
                        login_model.setEnvironmentId(EnvironmentList.get(environment_position).getEnvironmentID());
                    }

                    //UserName and Password
                    login_model.setUserName(preference.getString(PreferenceKey.Username, ""));
                    login_model.setPassword(preference.getString(PreferenceKey.Password, ""));
                } else {
                    //EnvironmentCode
                    login_model.setEnvironmentId(preference.getInt(PreferenceKey.EnvironmentId, 0));

                    //UserName and Password
                    usernameInput = mUserNameView.getText().toString();
                    login_model.setUserName(usernameInput);
                    passwordInput = mPasswordView.getText().toString();
                    login_model.setPassword(passwordInput);
                }

                // Android Version
                String androidVersion = String.valueOf(Build.VERSION.RELEASE);
                login_model.setAndroidVersion(androidVersion);

                // Reset errors.
                this.mUserNameView.setError(null);
                this.mPasswordView.setError(null);

                boolean cancel = false;
                View focusView = null;

                // Check for a valid password, if the user entered one.
                if (TextUtils.isEmpty(passwordInput)) {
                    mPasswordView.setError(getString(R.string.error_field_required));
                    focusView = mPasswordView;
                    cancel = true;
                }

                // Check for a valid email address.
                if (TextUtils.isEmpty(usernameInput)) {
                    mUserNameView.setError(getString(R.string.error_field_required));
                    focusView = mUserNameView;
                    cancel = true;
                }

                // Attempt Login
                if (cancel)
                    focusView.requestFocus();
                else {
                    attemptLogin(login_model);
                }
            } catch (Exception e) {
                e.printStackTrace();
                _mTracker.send(new HitBuilders.ExceptionBuilder()
                        .setDescription(new StandardExceptionParser(this, null)
                                .getDescription(Thread.currentThread().getName(), e))
                        .setFatal(false)
                        .build());
            }
        } else {
            notification.showNoInternetNotification();
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(LoginModel model) {
        Authenticate(model);
    }

    private void Authenticate(LoginModel loginModel) {
        //Show the progress dialog
        showLoadingAnimation();
        try {
            dataService.Authenticate(loginModel).enqueue(new Callback<UserIdentityModel>() {
                @Override
                public void onResponse(Call<UserIdentityModel> call, retrofit2.Response<UserIdentityModel> response) {
                    //LoginButton
                    hideLoadingAnimation();
                    //Response from Authenticate
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            //Convert Model
                            UserIdentityModel model = response.body();

                            //Registering User for Notification
                            startService(new Intent(LoginActivity.this, MyFirebaseMessagingService.class));
                            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                            if (refreshedToken != null) {
                                Subscribe subscribe = new Subscribe();
                                subscribe.subscribe(usernameInput, refreshedToken, "MBRANA", "mBrana mobile supply chain application.");
                            }

                            //Store or update the local pref
                            preference.put(PreferenceKey.Key, model.getKey());
                            preference.put(PreferenceKey.isMultipleActivityAllowed, model.getMultipleActivityAllowed());
                            preference.put(PreferenceKey.Address, model.getAddress());
                            preference.put(PreferenceKey.FirstName, model.getFirstName());
                            preference.put(PreferenceKey.EnvironmentName, model.getEnvironmentName());
                            preference.put(PreferenceKey.CreateTime, model.getCreateTime());
                            preference.put(PreferenceKey.RoutineActivityId, model.getRoutineActivityId());
                            preference.put(PreferenceKey.DefaultActivityId, model.getDefaultActivityId());
                            preference.put(PreferenceKey.ExpiryTime, model.getExpiryTime());
                            preference.put(PreferenceKey.EnvironmentTypeId, model.getEnvironmentTypeId());
                            preference.put(PreferenceKey.Mobile, model.getMobile());
                            preference.put(PreferenceKey.CampaignActivityId, model.getCampaignActivityId());
                            preference.put(PreferenceKey.EnvironmentId, model.getEnvironmentId());
                            preference.put(PreferenceKey.Username, model.getUsername());
                            preference.put(PreferenceKey.UserId, model.getUserId());
                            preference.put(PreferenceKey.LastName, model.getLastName());
                            preference.put(PreferenceKey.PreferenceKey, model.getKey());
                            preference.put(PreferenceKey.RememberMe, rememberMeCheckBox.isChecked());
                            preference.put(PreferenceKey.IsFromLogout, false);
                            preference.put(PreferenceKey.Password, passwordInput);

                            //UX Tracker
                            _mTracker.set("&uid", String.valueOf(model.getUserId()));
                            _mTracker.send(new HitBuilders.EventBuilder()
                                    .setCategory("UX")
                                    .setCustomDimension(1, String.valueOf(model.getUserId()))
                                    .setCustomDimension(2, model.getEnvironmentCode())
                                    .setCustomDimension(3, model.getEnvironmentName())
                                    .setAction("User Sign In")
                                    .build());

                            //Start Activity
                            Intent intent = new Intent(LoginActivity.this, MainNavigationActivity.class);
                            startActivity(intent);
                        }
                    } else if (response.code() == 400) {
                        notification.showHttp400Notification();
                    } else if (response.code() == 401) {
                        notification.showHttp401Notification();
                    } else if (response.code() == 406) {
                        notification.showHttp406Notification();
                    }
                }

                @Override
                public void onFailure(Call<UserIdentityModel> call, Throwable t) {
                    //LoginButton
                    hideLoadingAnimation();
                    //Fail Message
                    notification.showFailureNotification(t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideLoadingAnimation() {
        if (mLoginButton != null && login_loading != null) {
            if (mLoginButton.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(login_container);
                mLoginButton.setVisibility(View.VISIBLE);
                login_loading.setVisibility(View.GONE);
            }
        }
    }

    public void showLoadingAnimation() {
        if (mLoginButton != null && login_loading != null) {
            if (mLoginButton.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(login_container);
                mLoginButton.setVisibility(View.GONE);
                login_loading.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Setting the Analytics
        Log.d("GA-Screen", "Login");
        _mTracker.setScreenName("Login");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        // Handling the Signal Receiver
        if (_receiver == null) {
            _receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    _signal_strength = (int) intent.getExtras().get("Signal");
                    if (_signal_strength != -1) {
                        // Unregister Receiver
                        if (_receiver != null) {
                            hideLoadingAnimation();
                            GlobalVariables.setSignalValueReceived(true);
                            isReceiverRegistered = false;
                            try {
                                unregisterReceiver(_receiver);
                            } catch (IllegalArgumentException ex) {
                                //If )receiver is not registered we can't unregister it
                                //so skip the error so that it won't crash
                                ex.printStackTrace();
                                _mTracker.send(new HitBuilders.ExceptionBuilder()
                                        .setDescription(new StandardExceptionParser(LoginActivity.this, null)
                                                .getDescription(Thread.currentThread().getName(), ex))
                                        .setFatal(false)
                                        .build());
                            }
                        }
                        handelLoginForPrviousLoggedInUser();
                    }
                }
            };
        }

        // Registering the Receiver
        isReceiverRegistered = true;
        registerReceiver(_receiver, new IntentFilter("SignalUpdateFilter"));
    }

    private void checkAllPermissions() {
        easyPermission.requestPermission(this, Manifest.permission.READ_PHONE_STATE);
        easyPermission.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        easyPermission.requestPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        easyPermission.requestPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        easyPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionResult(String permission, boolean isGranted) {
        switch (permission) {
            case Manifest.permission.READ_PHONE_STATE:
                if (!isGranted)
                    easyPermission.requestPermission(this, Manifest.permission.READ_PHONE_STATE);
                break;
            case Manifest.permission.ACCESS_FINE_LOCATION:
                if (!isGranted)
                    easyPermission.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
                break;
            case Manifest.permission.ACCESS_COARSE_LOCATION:
                if (!isGranted)
                    easyPermission.requestPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
                break;
            case Manifest.permission.ACCESS_NETWORK_STATE:
                if (!isGranted)
                    easyPermission.requestPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
                break;
        }
    }

    private void registerPhoneStateListener() {
        Intent intent = new Intent(this, CustomPhoneStateListener.class);
        startService(intent);
    }

    private String getVersionName() {
        // Show the version lable
        PackageInfo pInfo;
        try {
            pInfo = getCurrentPackageInfo();
            if (pInfo != null) {
                return pInfo.versionName;
            }
        } catch (Exception e) {
            e.printStackTrace();
            _mTracker.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(new StandardExceptionParser(this, null)
                            .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build());
        }
        return null;
    }

    private PackageInfo getCurrentPackageInfo() {
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo;
        } catch (Exception ex) {
            ex.printStackTrace();
            _mTracker.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(new StandardExceptionParser(this, null)
                            .getDescription(Thread.currentThread().getName(), ex))
                    .setFatal(false)
                    .build());
        }
        return null;
    }

    private void CheckAppUpdate() {
        if (NetworkTypeHelper.isConnectedWifi(this) || NetworkTypeHelper.isConnectedMobile(this)) {
            DownloadJSON jd = new DownloadJSON();
            jd.execute();
        } else {
            notification.showNoInternetNotification();
        }
    }

    public void handelLoginForPrviousLoggedInUser() {
        if (!preference.getBoolean(PreferenceKey.IsFromLogout, true)) {
            if (preference.getBoolean(PreferenceKey.RememberMe, false)) {
                //noinspection NewApi
                BuildLoginModel(true);
            }
        }
    }

    public void dataReceived(JSONArray jsonArray, int requestCode) {
        switch (requestCode) {
            case 3: {
                try {
                    ArrayList<String> siteName = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        EnvironmentModel item = new EnvironmentModel();
                        item.setEnvironment(jo.getString("Environment"));
                        item.setEnvironmentID(jo.getInt("EnvironmentID"));
                        item.setEnvironmentCode(jo.getString("EnvironmentCode"));
                        siteName.add(item.getEnvironment());
                        EnvironmentList.add(item);
                        EnvironmentCustomAdapter adapter = new EnvironmentCustomAdapter(this, R.layout.layout_spinneritemforenvironmentlist, siteName);
                        adapter.setDropDownViewResource(R.layout.layout_environment_list);
                        environment_list.setAdapter(adapter);
                    }
                    LoadEnvironmentLookUpData(jsonArray);

                    for (int f = 0; f < EnvironmentList.size(); f++) {
                        if (EnvironmentList.get(f).getEnvironmentCode().equalsIgnoreCase(preference.getString(PreferenceKey.EnvironmentCode, null))) {
                            environment_list.setSelection(f);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    _mTracker.send(new HitBuilders.ExceptionBuilder()
                            .setDescription(new StandardExceptionParser(this, null)
                                    .getDescription(Thread.currentThread().getName(), e))
                            .setFatal(false)
                            .build());
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void LoadEnvironmentLookUpData(JSONArray jsonArray) {
        try {
            ArrayList<String> siteName = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                EnvironmentModel Environments = new EnvironmentModel();
                Environments.setEnvironmentCode(jo.getString("EnvironmentCode"));
                Environments.setEnvironment(jo.getString("Environment"));
                Environments.setEnvironmentID(jo.getInt("EnvironmentID"));
                siteName.add(Environments.getEnvironment());
                EnvironmentList.add(Environments);
            }
            EnvironmentCustomAdapter environmentListAdapter = new EnvironmentCustomAdapter(this,
                    R.layout.layout_spinneritemforenvironmentlist, siteName);
            environmentListAdapter.setDropDownViewResource(R.layout.layout_environment_list);
            environment_list.setAdapter(environmentListAdapter);

            // set saved environemnt from previous login

            for (int i = 0; i < EnvironmentList.size(); i++) {
                if (EnvironmentList.get(i).getEnvironmentCode().equalsIgnoreCase(preference.getString(PreferenceKey.EnvironmentCode, null))) {
                    environment_list.setSelection(i);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            _mTracker.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(new StandardExceptionParser(this, null)
                            .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build());
        }
    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {
        String apk_update_url, apk_latest_version_name;
        int apk_latest_version_code, current_apk_version_code;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            JSONDownloader jd = new JSONDownloader();
            // Making a request to url and getting response
            String jsonStr = jd.makeServiceCall(Constants.JSONUrl);

            // Parse the string
            if (jsonStr != null) {
                if (jsonStr.length() > 0) {
                    try {
                        // Parse the JSONObject
                        JSONObject jo = new JSONObject(jsonStr);

                        //
                        String packageName = getApplication().getApplicationInfo().packageName;
                        if (packageName.contains(".dev")) {
                            //then do dev stuff
                            apk_update_url = jo.getString("UPDATE_URL_DEV");
                            apk_latest_version_code = jo.getInt("LATEST_VERSION_CODE_DEV");
                            apk_latest_version_name = jo.getString("LATEST_VERSION_NAME_DEV");
                        } else {
                            //we are on the master/release branch
                            apk_update_url = jo.getString("UPDATE_URL");
                            apk_latest_version_code = jo.getInt("LATEST_VERSION_CODE");
                            apk_latest_version_name = jo.getString("LATEST_VERSION_NAME");
                        }

                        // Get the Current Apk detail
                        pInfo = getCurrentPackageInfo();
                        if (pInfo != null) {
                            current_apk_version_code = pInfo.versionCode;
                        }

                        // Compare the two version codes
                        if (current_apk_version_code < apk_latest_version_code) {
                            Intent intent = new Intent(LoginActivity.this, UpdateActivity.class);
                            intent.putExtra("APK_UPDATE_URL", apk_update_url);
                            intent.putExtra("APK_LATEST_VERSION_NAME", apk_latest_version_name);
                            intent.putExtra("APK_LATEST_VERSION_CODE", apk_latest_version_code);
                            startActivity(intent);
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        _mTracker.send(new HitBuilders.ExceptionBuilder()
                                .setDescription(new StandardExceptionParser(getApplicationContext(), null)
                                        .getDescription(Thread.currentThread().getName(), e))
                                .setFatal(false)
                                .build());
                    }
                }
            } else {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    private class JSONDownloader {
        private String makeServiceCall(String reqUrl) {
            String result = "";
            try {
                URL url = new URL(reqUrl);

                Log.d("URL: ", url.toString());

                MyNetworkInterceptor interceptor = new MyNetworkInterceptor();

                OkHttpClient client = new OkHttpClient
                        .Builder()
                        .addInterceptor(interceptor)
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .build();

                Response response = client.newCall(request).execute();

                int requestStatusCode = response.code();

                Log.d("Response Code: ", String.valueOf(requestStatusCode));

                result = response.body().string();

                Log.d("Result: ", String.valueOf(result));
            } catch (Exception e) {
                e.printStackTrace();
                _mTracker.send(new HitBuilders.ExceptionBuilder()
                        .setDescription(new StandardExceptionParser(getApplicationContext(), null)
                                .getDescription(Thread.currentThread().getName(), e))
                        .setFatal(false)
                        .build());
            }
            return result;
        }
    }

    private class MyNetworkInterceptor implements Interceptor {
        private final String TAG = getClass().getSimpleName();

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            if (response.code() != 200) {
                Response r = null;
                try {
                    r = makeTokenRefreshCall(request, chain);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return r;
            }
            Log.d(TAG, "INTERCEPTED:$ " + response.toString());
            return response;
        }

        private Response makeTokenRefreshCall(Request req, Chain chain) throws JSONException, IOException {
            Log.d(TAG, "Retrying new request");
            /* make a new request which is same as the original one, except that its headers now contain a refreshed token */
            Request newRequest;
            newRequest = req.newBuilder().build();
            Response another = chain.proceed(newRequest);
            while (another.code() != 200) {
                makeTokenRefreshCall(newRequest, chain);
            }
            return another;
        }
    }
}