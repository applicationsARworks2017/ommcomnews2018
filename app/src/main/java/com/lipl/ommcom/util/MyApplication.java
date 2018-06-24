package com.lipl.ommcom.util;

import android.Manifest;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.Parcel;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lipl.ommcom.R;
import com.lipl.ommcom.activity.HomeActivity;
import com.lipl.ommcom.activity.NewsDetailsActivity;
import com.lipl.ommcom.pojo.Comment;
import com.lipl.ommcom.pojo.News;
import com.lipl.ommcom.pojo.NewsImage;
import com.lipl.ommcom.pojo.NewsVideo;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fr.quentinklein.slt.LocationTracker;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Android Luminous on 6/11/2016.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // You need to ask the user to enable the permissions
        } else {
            LocationTracker tracker = new LocationTracker(getApplicationContext()) {
                @Override
                public void onLocationFound(Location location) {
                    String lat = location.getLatitude() + "";
                    String lon = location.getLongitude() + "";
                    getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putString(Config.SP_LATITUDE, lat).commit();
                    getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putString(Config.SP_LONGITUDE, lon).commit();
                }

                @Override
                public void onTimeout() {

                }
            };
            tracker.startListening();
        }
        startService(new Intent(getApplicationContext(), MyAppService.class));
    }
}
