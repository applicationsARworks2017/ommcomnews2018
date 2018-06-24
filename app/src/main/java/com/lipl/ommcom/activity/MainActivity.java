package com.lipl.ommcom.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.lipl.ommcom.R;
import com.lipl.ommcom.pojo.BNews;
import com.lipl.ommcom.pojo.BreakingNews;
import com.lipl.ommcom.pojo.NewsDetailsForFlipModel;
import com.lipl.ommcom.util.AnimationUtil;
import com.lipl.ommcom.util.Config;
import com.lipl.ommcom.util.CustomTextView;
import com.lipl.ommcom.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.Arrays;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends Activity {

    private List<String> sss;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.demo_record);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("isFromNotification", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, 0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(getNotificationIcon())
                        .setContentTitle("Breaking News")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Plus II admission: 52335 students will take admission between 28th-30thJuly Kashmir Unrest: Curfew reimposed in Srinagar Kashmir Unrest: Curfew reimposed in Srinagar Kashmir Unrest: Curfew reimposed in Srinagar"))
                        .setContentText("Plus II admission: 52335 students will take admission between 28th-30thJuly Kashmir Unrest: Curfew reimposed in Srinagar Kashmir Unrest: Curfew reimposed in Srinagar Kashmir Unrest: Curfew reimposed in Srinagar")
                        .setDefaults(NotificationCompat.DEFAULT_SOUND)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);
        manager.notify(12, mBuilder.build());
        //manager.cancel(12);
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_small : R.mipmap.ic_launcher;
    }
}
