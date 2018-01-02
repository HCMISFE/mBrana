package com.jsi.mbrana.CommonUIComponents;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.jsi.mbrana.R;

/**
 * Created by Surafel Nigussie on 12/13/2017.
 */

public class mBranaNotification {
    private Snackbar snackbar;
    private Context context;
    private View view;

    //
    public mBranaNotification(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    public void showLoadingNotification() {
        snackbar = Snackbar.make(view, "Loading...", Snackbar.LENGTH_LONG);
        //Editing the SnackBar
        View snackBarView = snackbar.getView();
        //Edit the text
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(context.getResources().getColor(R.color.white));
        //Edit the background
        snackBarView.setBackgroundColor(context.getResources().getColor(R.color.PrimaryDark));
        //Show the snack
        snackbar.show();
    }

    public void showSuccessNotification(String message) {
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        //Editing the SnackBar
        View snackBarView = snackbar.getView();
        //Edit the text
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(context.getResources().getColor(R.color.white));
        //Edit the background
        snackBarView.setBackgroundColor(context.getResources().getColor(R.color.notification_success));
        //Show the snack
        snackbar.show();
    }

    public void showWarningNotification(String message) {
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        //Editing the SnackBar
        View snackBarView = snackbar.getView();
        //Edit the text
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(context.getResources().getColor(R.color.black));
        //Edit the background
        snackBarView.setBackgroundColor(context.getResources().getColor(R.color.notification_warning));
        //Show the snack
        snackbar.show();
    }

    public void showFailureNotification(String message) {
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        //Editing the SnackBar
        View snackBarView = snackbar.getView();
        //Edit the text
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(context.getResources().getColor(R.color.white));
        //Edit the background
        snackBarView.setBackgroundColor(context.getResources().getColor(R.color.notification_failure));
        //Show the snack
        snackbar.show();
    }

    //Network Notification
    public void showHttp400Notification() {
        snackbar = Snackbar.make(view, "Bad Request!", Snackbar.LENGTH_LONG);
        //Editing the SnackBar
        View snackBarView = snackbar.getView();
        //Edit the text
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(context.getResources().getColor(R.color.black));
        //Edit the background
        snackBarView.setBackgroundColor(context.getResources().getColor(R.color.notification_warning));
        //Show the snack
        snackbar.show();
    }

    public void showHttp401Notification() {
        snackbar = Snackbar.make(view, "Unauthorized!", Snackbar.LENGTH_LONG);
        //Editing the SnackBar
        View snackBarView = snackbar.getView();
        //Edit the text
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(context.getResources().getColor(R.color.black));
        //Edit the background
        snackBarView.setBackgroundColor(context.getResources().getColor(R.color.notification_warning));
        //Show the snack
        snackbar.show();
    }

    public void showHttp406Notification() {
        snackbar = Snackbar.make(view, "Connection Problem!", Snackbar.LENGTH_LONG);
        //Editing the SnackBar
        View snackBarView = snackbar.getView();
        //Edit the text
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(context.getResources().getColor(R.color.black));
        //Edit the background
        snackBarView.setBackgroundColor(context.getResources().getColor(R.color.notification_warning));
        //Show the snack
        snackbar.show();
    }

    public void showNoInternetNotification() {
        snackbar = Snackbar.make(view, "Unable to connect!", Snackbar.LENGTH_LONG);
        //Editing the SnackBar
        View snackBarView = snackbar.getView();
        //Edit the text
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(context.getResources().getColor(R.color.black));
        //Edit the background
        snackBarView.setBackgroundColor(context.getResources().getColor(R.color.notification_failure));
        //Show the snack
        snackbar.show();
    }
}