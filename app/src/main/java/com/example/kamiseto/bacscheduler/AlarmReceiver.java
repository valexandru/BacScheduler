package com.example.kamiseto.bacscheduler;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class AlarmReceiver extends BroadcastReceiver {

    //getting context for shared preference by parsing it from AboutFragment
    public static Context ContextSP;
    public static String Subject;

    //@TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context arg0, Intent arg1) {

        // Making the recuring task for auto-payment
        Toast.makeText(arg0, "Spor la lucru la "+Subject, Toast.LENGTH_SHORT).show();

        WifiManager wifi = (WifiManager) ContextSP.getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(false);
    }
}
