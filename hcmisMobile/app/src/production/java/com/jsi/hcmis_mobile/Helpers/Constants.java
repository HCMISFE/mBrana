package com.jsi.mbrana.Helpers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.jsi.mbrana.Models.StatusModel;

/**
 * Created by Sololia on 6/14/2016.
 */
public class Constants {
    // API Constants
    public static final String BaseUrl = "http://api.mbrana.org/api/";
    public static final String JSONUrl = "http://mbrana.org/updates/version.json";

    //ExpressVPN Package Name
    public static String DefaultToken = "6957529B-B90B-430F-B643-E6C0D9257C61";

    // Error messages
    public static int error_ConnectionErrorCode = 101;
    public static int error_ApiFailCode = 102;

    // Error messages
    public static String error_ConnectionError = "Unable to connect: Error 101";
    public static String error_ApiFail = "Unable to connect: Error 102";

    // Stock Status
    public static String StockedOut = "Stocked Out";
    public static String BelowEOP = "Below EOP";
    public static String Normal = "Normal";
    public static String BelowMin = "Below Min";
    public static String Excess = "Excess";
    public static String Draft = "Draft";
    public static String Submitted = "Submitted";
    public static String Picklist = "Picklist";
    public static String Issued = "Issued";
    public static String[] status_list = new String[]{
            "Drft",
            "Sbmt",
            "Pkls",
            "Issd"
    };

    // Hide SoftKeyboard
    public void hideSoftKeyboard(View view, Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // PurchaseOrder Status
    public static class PurchaseOrderStatus {
        public static StatusModel Draft = new StatusModel("Draft", "DRFT");
        public static StatusModel Requested = new StatusModel("Submitted", "RQT");
        public static StatusModel InProcess = new StatusModel("In-Process", "INPC");
        public static StatusModel Canceled = new StatusModel("Canceled", "CNCL");
        public static StatusModel Processed = new StatusModel("Processed", "PCSD");
    }

    // Order Status
    public static class OrderStatus {
        public static StatusModel DRAFT = new StatusModel("Draft", "DRFT");
        public static StatusModel ORDERFILLED = new StatusModel("Submitted", "ORFI");
        public static StatusModel APPROVED = new StatusModel("Approved", "APRD");
        public static StatusModel ISSUED = new StatusModel("Issued", "ISUD");
        public static StatusModel CANCELED = new StatusModel("Canceled", "CNCL");
        public static StatusModel PickListed = new StatusModel("Pick Listed", "PIKL");
        public static StatusModel VOID = new StatusModel("VOID", "VOID");
    }
}
