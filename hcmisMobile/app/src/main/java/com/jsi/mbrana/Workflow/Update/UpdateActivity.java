package com.jsi.mbrana.Workflow.Update;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.R;

import java.io.File;

public class UpdateActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;
    private static final int PROGRESS_DELAY = 250;
    long download_reference_id;
    Toolbar toolbar;
    TextView tv_latest_apk_version;
    TextView tv_percent;
    String apk_update_url, apk_file_name, apk_latest_version_code;
    Cursor cursor;
    Handler handler = new Handler();
    DownloadManager manager;
    private boolean isProgressCheckerRunning = false;
    private Tracker _mTracker;
    /**
     * Checks download progress and updates status, then re-schedules itself.
     */
    private Runnable ProgressChecker = new Runnable() {
        @Override
        public void run() {
            try {
                checkProgress();
            } finally {
                handler.postDelayed(ProgressChecker, PROGRESS_DELAY);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apk_update);
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("mBrana Update");
        // Check for permission
        doCheckPermission_WriteExternalStorage();
        // Progress in percent
        tv_percent = (TextView) findViewById(R.id.tv_percent);
        // Start the download
        StartDownload();
        // when the first download starts
        startProgressChecker();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("GA-Screen", this.getLocalClassName());
        _mTracker.setScreenName("Workflow-Main: Update");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    /**
     * Checks download progress.
     */
    private void checkProgress() {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(download_reference_id);
        Cursor cursor = manager.query(query);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return;
        }
        do {
            // Download Progress
            int byte_downloaded = (int) cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            int byte_total = (int) cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            int percent = (int) (((double) byte_downloaded / (double) byte_total) * 100);
            tv_percent.setText(percent + "%");
        } while (cursor.moveToNext());
        cursor.close();
    }

    /**
     * Starts watching download progress.
     * <p>
     * This method is safe to call multiple times. Starting an already running progress checker is a no-op.
     */
    private void startProgressChecker() {
        if (!isProgressCheckerRunning) {
            ProgressChecker.run();
            isProgressCheckerRunning = true;
        }
    }

    /**
     * Stops watching download progress.
     */
    private void stopProgressChecker() {
        handler.removeCallbacks(ProgressChecker);
        isProgressCheckerRunning = false;
    }

    private void doCheckPermission_WriteExternalStorage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            doRequestPermission_WriteExternalStorage();
        }
    }

    private void doRequestPermission_WriteExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission Request")
                    .setMessage(Html.fromHtml("Allow <b>mFlow</b> to write to external storage."))
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(UpdateActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(UpdateActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    public void StartDownload() {
        // Show the version text
        Long timeStamp = System.currentTimeMillis() / 1000;
        apk_file_name = "mflow-" + timeStamp.toString() + ".apk";
        apk_update_url = getIntent().getSerializableExtra("APK_UPDATE_URL").toString();
        apk_latest_version_code = getIntent().getSerializableExtra("APK_LATEST_VERSION_CODE").toString();
        tv_latest_apk_version = (TextView) findViewById(R.id.tv_latest_version);
        tv_latest_apk_version.setText("Latest version: " + apk_latest_version_code);

        // Setting the download settings
        Uri update_uri = Uri.parse(apk_update_url);
        DownloadManager.Request request = new DownloadManager.Request(update_uri);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, apk_file_name);
        request.setVisibleInDownloadsUi(false);
        request.setNotificationVisibility(0);

        // Set the Download reference
        manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        download_reference_id = manager.enqueue(request);
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(download_reference_id);
        cursor = manager.query(query);

        // Listener when finished installing
        IntentFilter filter_complete = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        BroadcastReceiver receiver_complete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // when the last download finishes stop checking the download progress
                stopProgressChecker();

                // Download Reference ID
                long reference_id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                if (reference_id != download_reference_id) {
                    // Ignoring unrelated downloads
                    return;
                } else {
                    SharedPreferences.Editor editor = getSharedPreferences("userCredentials", 0).edit();
                    editor.putString("VersionStatus", "STARTED");
                    editor.apply();
                }

                // Download Manager
                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterByStatus(DownloadManager.STATUS_FAILED | DownloadManager.STATUS_PAUSED | DownloadManager.STATUS_SUCCESSFUL | DownloadManager.STATUS_RUNNING | DownloadManager.STATUS_PENDING);
                Cursor cursor = manager.query(query);

                // It shouldn't be empty, but just in case
                if (!cursor.moveToFirst()) {
                    // Empty row
                    return;
                }

                // Download Status
                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                if (DownloadManager.STATUS_FAILED == status) {
                    // Download Failed
                    SharedPreferences.Editor editor = getSharedPreferences("userCredentials", 0).edit();
                    editor.putString("VersionStatus", "FAILED");
                    editor.apply();
                    return;
                } else if (DownloadManager.STATUS_SUCCESSFUL == status) {
                    // Check if the downloaded file
                    if (download_reference_id == reference_id) {
                        // Download Successful
                        SharedPreferences.Editor editor = getSharedPreferences("userCredentials", 0).edit();
                        editor.putString("VersionStatus", "SUCCESSFUL");
                        editor.apply();

                        // Start the Installer
                        Intent install_intent = new Intent(Intent.ACTION_VIEW);
                        install_intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/Download/" + apk_file_name)), "application/vnd.android.package-archive");
                        install_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
                        startActivity(install_intent);
                        finish();
                    }
                    return;
                }
                cursor.close();
            }
        };
        registerReceiver(receiver_complete, filter_complete);
        cursor.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (manager != null)
            manager.remove(download_reference_id);
    }
}