package com.jsi.mbrana.CommonUIComponents;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Surafel Nigussie on 12/13/2017.
 */

public class mBranaProgressDialog {
    private ProgressDialog progressDialog;

    public mBranaProgressDialog(Context context) {
        progressDialog = new ProgressDialog(context);
    }

    public void showProgressDialog() {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    public void showProgressDialogWithMessage(String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog.isShowing())
            progressDialog.hide();
    }
}