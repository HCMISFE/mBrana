package com.jsi.mbrana.DataAccess;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Sololia on 6/14/2016.
 */
public interface IDataNotification {
    void dataObjectReceived(JSONObject jsonObject, int requestCode);
    void dataReceived(JSONArray jsonArray, int requestCode);
    void handelNotification(String message);
    void handelProgressDialog(Boolean showProgress, String Message);
    void readFromDatabase(int requestCode);
}
