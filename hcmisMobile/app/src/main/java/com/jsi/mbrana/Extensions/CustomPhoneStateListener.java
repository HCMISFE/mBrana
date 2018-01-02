package com.jsi.mbrana.Extensions;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

/**
 * Created by user on 3/13/2017.
 */

public class CustomPhoneStateListener extends Service {
    int signalStrengthValue;
    MyPhoneStateListener _listener;
    TelephonyManager _manager;

    @Override
    public void onCreate() {
        _listener = new MyPhoneStateListener();
        _manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        _manager.listen(_listener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (_listener != null)
            _manager.listen(_listener, PhoneStateListener.LISTEN_NONE);
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);

            Intent intent = new Intent("SignalUpdateFilter");
            if (signalStrength.isGsm()) {
                int asu = signalStrength.getGsmSignalStrength();
                if (asu == 0 || asu == 99) {
                    signalStrengthValue = 0;
                    intent.putExtra("Signal", signalStrengthValue);
                } else if (asu < 5) {
                    signalStrengthValue = 1;
                    intent.putExtra("Signal", signalStrengthValue);
                } else if (asu >= 5 && asu < 8) {
                    signalStrengthValue = 2;
                    intent.putExtra("Signal", signalStrengthValue);
                } else if (asu >= 8 && asu < 12) {
                    signalStrengthValue = 3;
                    intent.putExtra("Signal", signalStrengthValue);
                } else if (asu >= 12 && asu < 14) {
                    signalStrengthValue = 4;
                    intent.putExtra("Signal", signalStrengthValue);
                } else if (asu >= 14) {
                    signalStrengthValue = 5;
                    intent.putExtra("Signal", signalStrengthValue);
                }
            } else {
                int cdmaDbm = signalStrength.getCdmaDbm();
                if (cdmaDbm >= -89) {
                    signalStrengthValue = 5;
                    intent.putExtra("Signal", signalStrengthValue);
                } else if (cdmaDbm >= -97) {
                    signalStrengthValue = 4;
                    intent.putExtra("Signal", signalStrengthValue);
                } else if (cdmaDbm >= -103) {
                    signalStrengthValue = 3;
                    intent.putExtra("Signal", signalStrengthValue);
                } else if (cdmaDbm >= -107) {
                    signalStrengthValue = 2;
                    intent.putExtra("Signal", signalStrengthValue);
                } else if (cdmaDbm >= -109) {
                    signalStrengthValue = 1;
                    intent.putExtra("Signal", signalStrengthValue);
                } else {
                    signalStrengthValue = 0;
                    intent.putExtra("Signal", signalStrengthValue);
                }
            }
            sendBroadcast(intent);
        }
    }
}
