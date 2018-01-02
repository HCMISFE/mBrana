package com.jsi.mbrana.Workflow.Main;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.Helpers.EthiopianDateConversion.EthiopianDateConversion;
import com.jsi.mbrana.Helpers.GlobalVariables;
import com.jsi.mbrana.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Implementation of App Widget functionality.
 */
public class MainAppWidget extends AppWidgetProvider implements IDataNotification {
    public static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager, final int appWidgetId) {
        updateContent(context, appWidgetManager, appWidgetId);

        if(!GlobalVariables.isWidgetUpdateRunning()) {
            Log.d("upate thread","started==>");

            GlobalVariables.setIsWidgetUpdateRunning(true);
            final android.os.Handler handler = new android.os.Handler();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("upate thread","running==>");
                    updateContent(context, appWidgetManager, appWidgetId);
                    handler.postDelayed(this, 60000);
                }
            });
        }
    }

    public static void updateContent(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        CharSequence widgetText = context.getString(R.string.mytest);

        String printedDateStringValue = "";
        printedDateStringValue = EthiopianDateConversion.GregorianDateToEthiopianDate_IssueDateFormat(Calendar.getInstance().getTime());

        // Shared preference
        SharedPreferences prefs_environment = context.getSharedPreferences("Environment", 0);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main_app_widget);
        views.setTextViewText(R.id.appwidget_date, "mFlow " + printedDateStringValue);
        views.setTextViewText(R.id.appwidget_name, prefs_environment.getString("Environment", "NA"));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void dataObjectReceived(JSONObject jsonObject, int requestCode) {

    }

    @Override
    public void dataReceived(JSONArray jsonArray, int requestCode) {

    }

    @Override
    public void handelNotification(String message) {

    }

    @Override
    public void handelProgressDialog(Boolean showProgress, String Message) {

    }

    @Override
    public void readFromDatabase(int requestCode) {

    }
}

