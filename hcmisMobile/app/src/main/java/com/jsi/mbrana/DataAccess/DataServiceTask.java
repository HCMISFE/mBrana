package com.jsi.mbrana.DataAccess;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.Helpers.Constants;
import com.jsi.mbrana.Helpers.GlobalVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * Created by Sololia on 6/14/2016.
 */
public class DataServiceTask extends AsyncTask<String, Void, Boolean> {
    JSONArray jsonArray;
    JSONObject jsonObject;
    IDataNotification dataNotification;
    Boolean isApiCallFailed = false;
    Boolean noData = false;
    Boolean showProgressbar = true;
    Boolean returnFullData = false;
    Boolean post = false;
    private boolean running = true;
    private int requestCode = 0;

    public DataServiceTask(IDataNotification dataNotification) {
        this.dataNotification = dataNotification;
    }

    public DataServiceTask(IDataNotification iDataNotification, int requestCode) {
        this.dataNotification = iDataNotification;
        this.requestCode = requestCode;
    }

    public DataServiceTask(IDataNotification iDataNotification, boolean returnFullData) {
        this.dataNotification = iDataNotification;
        this.returnFullData = returnFullData;
    }

    public DataServiceTask(IDataNotification iDataNotification, int requestCode, boolean returnFullData) {
        this.dataNotification = iDataNotification;
        this.returnFullData = returnFullData;
        this.requestCode = requestCode;
    }

    public DataServiceTask(IDataNotification iDataNotification, int requestCode, boolean returnFullData, boolean showProgressbar) {
        this.dataNotification = iDataNotification;
        this.requestCode = requestCode;
        this.showProgressbar = showProgressbar;
        this.returnFullData = returnFullData;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (showProgressbar)
            dataNotification.handelProgressDialog(true, "Connecting to Server...");
    }

    @Override
    protected Boolean doInBackground(String... params) {
        if (running) {
            if (params.length > 2) {
                isBackgroundTaskRunsSuccessfully(params[0], params[1], params[2]);
            } else {
                isBackgroundTaskRunsSuccessfully(Constants.BaseUrl, params[0], params[1]);
            }
        }
        return null;
    }

    @SuppressLint("LongLogTag")
    private boolean isBackgroundTaskRunsSuccessfully(String BaseUrl, String param, String token) {
        boolean status = true;
        isApiCallFailed = false;

        StringBuilder result = new StringBuilder();
        URL url;

        try {
            url = new URL(BaseUrl + param);

            Log.d("URL: ", String.valueOf(url));

            HttpURLConnection urlConnection = null;

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(post);
            urlConnection.setRequestMethod(post ? "POST" : "GET");
            urlConnection.setRequestProperty("Authorization", "Basic " + GlobalVariables.getToken());

            InflaterInputStream in = new InflaterInputStream((urlConnection.getInputStream()), new Inflater(true));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            int requestCode = urlConnection.getResponseCode();

            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            urlConnection.disconnect();

            if (!post) {
                jsonObject = new JSONObject(result.toString());
                Log.d("JsonObject:", String.valueOf(jsonObject));
                if (!returnFullData) {
                    jsonArray = jsonObject.getJSONArray("Data");
                    if (jsonArray.length() == 0 && !returnFullData) {
                        noData = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = false;
            isApiCallFailed = false;
        }

        return status;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        //Handling progress bar
        if (showProgressbar)
            dataNotification.handelProgressDialog(false, "");

        //API status
        if (isApiCallFailed) {
            dataNotification.handelNotification(Constants.error_ApiFail);
            dataNotification.readFromDatabase(requestCode);
        } else if (noData) {
            dataNotification.handelNotification("No Data");
        } else {
            if (returnFullData)
                dataNotification.dataObjectReceived(jsonObject, requestCode);
            else if (jsonArray == null) {
                dataNotification.handelNotification("No Data");
            } else {
                dataNotification.dataReceived(jsonArray, requestCode);
            }
        }
    }

    @Override
    protected void onCancelled() {
        running = false;
    }
}