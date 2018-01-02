package com.jsi.mbrana.DataAccess;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.Helpers.Constants;
import com.jsi.mbrana.Helpers.GlobalVariables;
import com.jsi.mbrana.Helpers.NetworkTypeHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sololia on 6/14/2016.
 */
public class DataServiceTaskForObjects extends AsyncTask<String, Void, Boolean> {
    IDataNotification dataNotification;
    Boolean isApiCallFailed = false;
    Boolean post = false;
    String jsonParameter = "";
    int requestStatusCode = 500;
    JSONArray jsonArray;
    JSONObject jsonObject;
    Boolean noData = false;
    Boolean returnFullData = false;
    Boolean showProgress = true;
    private int requestCode = 5;

    public DataServiceTaskForObjects(IDataNotification dataNotification, String jsonParameter, int requestCode) {
        Log.d("jsonParameter", jsonParameter);
        this.dataNotification = dataNotification;
        this.jsonParameter = jsonParameter;
        post = true;
        this.requestCode = requestCode;
    }

    public DataServiceTaskForObjects(IDataNotification dataNotification, String jsonParameter, int requestCode, Boolean showProgress) {
        Log.d("jsonParameter", jsonParameter);
        this.dataNotification = dataNotification;
        this.jsonParameter = jsonParameter;
        post = true;
        this.requestCode = requestCode;
        this.showProgress = showProgress;
    }


    public DataServiceTaskForObjects(IDataNotification dataNotification, String jsonParameter, int requestCode, Boolean returnFullData, Boolean showProgress) {
        Log.d("jsonParameter", jsonParameter);
        this.dataNotification = dataNotification;
        this.jsonParameter = jsonParameter;
        post = true;
        this.requestCode = requestCode;
        this.showProgress = showProgress;
        this.returnFullData = returnFullData;
    }

    public DataServiceTaskForObjects(IDataNotification dataNotification, String jsonParameter) {
        Log.d("jsonParameter", jsonParameter);
        this.dataNotification = dataNotification;
        this.jsonParameter = jsonParameter;
        post = true;
    }

    public AsyncTask<String, Void, Boolean> setPostRequest() {
        this.post = true;
        return this;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (showProgress)
            dataNotification.handelProgressDialog(true, "Connecting to Server...");
    }

    @Override
    protected Boolean doInBackground(String... params) {
        Log.d("params1", String.valueOf(params[0]));
        Log.d("params2", String.valueOf(params[1]));
        isBackgroundTaskRunsSuccessfully(params[0], params[1]);
        return null;
    }

    @SuppressLint("LongLogTag")
    public boolean isBackgroundTaskRunsSuccessfully(String param, String token) {
        boolean status = true;
        isApiCallFailed = false;

        StringBuilder result = new StringBuilder();
        URL url = null;

        try {
            url = new URL(Constants.BaseUrl + param);

            Log.d("URL: ", url.toString());

            HttpURLConnection urlConnection = null;

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(post);
            urlConnection.setRequestMethod(post ? "POST" : "GET");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            urlConnection.setRequestProperty("Authorization", "Basic " + GlobalVariables.getToken());

            if (jsonParameter.length() != 0) {
                OutputStream os = urlConnection.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                osw.write(jsonParameter);
                osw.flush();
                osw.close();
            }

            requestStatusCode = urlConnection.getResponseCode();

            Log.d("Response: ", urlConnection.getResponseCode() + "");

            result.append("");

            InputStream in;

            if (requestStatusCode < 400)
                in = new BufferedInputStream(NetworkTypeHelper.getInputStream(urlConnection));
            else
                in = new BufferedInputStream(NetworkTypeHelper.getErrorStream(urlConnection));

            Log.d("RequestMethod: ", String.valueOf(new InputStreamReader(in)));

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));


            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            Log.d("Result: ", result.toString());

            urlConnection.disconnect();

            if (result.length() > 0) {
                jsonObject = new JSONObject(result.toString());

                if (!returnFullData && requestStatusCode < 400) {
                    jsonArray = jsonObject.getJSONArray("Data");
                    if (jsonArray.length() == 0 && !returnFullData) noData = true;
                }
            } else {
                jsonObject = new JSONObject();
                jsonArray = new JSONArray();
            }

            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ReturnFullData: ", e.toString());
            isApiCallFailed = true;
            status = false;
        }

        return status;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        if (showProgress)
            dataNotification.handelProgressDialog(false, "");

        String message = "";

        if (requestStatusCode == 200) {
            message = "Change is successful.";
            if (jsonArray != null) {
                dataNotification.dataReceived(jsonArray, requestCode);
            } else {
                dataNotification.dataObjectReceived(jsonObject, requestCode);
            }
        } else if (requestStatusCode == 401)
            message = "Invalid username or password!";
        else
            message = "Error while connecting!";

        dataNotification.handelNotification(message);
    }
}


