package com.lipl.ommcom.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Parcel;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.lipl.ommcom.Notification.NotificationUtils;
import com.lipl.ommcom.R;
import com.lipl.ommcom.pojo.Advertisement;
import com.lipl.ommcom.pojo.BNews;
import com.lipl.ommcom.pojo.BreakingNews;
import com.lipl.ommcom.pojo.Category;
import com.lipl.ommcom.pojo.CitizenJournalistVideos;
import com.lipl.ommcom.pojo.ConferenceNews;
import com.lipl.ommcom.pojo.News;
import com.lipl.ommcom.pojo.OdishaNews;
import com.lipl.ommcom.util.AnimationUtil;
import com.lipl.ommcom.util.AnimatorPath;
import com.lipl.ommcom.util.Config;
import com.lipl.ommcom.util.CustomTextView;
import com.lipl.ommcom.util.PathEvaluator;
import com.lipl.ommcom.util.PathPoint;
import com.lipl.ommcom.util.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;

//import co.mobiwise.fastgcm.GCMListener;
//import co.mobiwise.fastgcm.GCMManager;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {//}, GCMListener,
        //CustomGCMService.OnConferenceStartListener,
        //CustomGCMService.OnConferenceStopListener,
        //CustomGCMService.OnBreakingNewsNotificationListener {
        private BroadcastReceiver mRegistrationBroadcastReceiver;
    private RelativeLayout layoutLogoImg;
    private Toolbar toolbar;
    private ObjectAnimator anim;
    private LinearLayout mToolbarBottomBorder;
    private NavigationView navigationView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private long News_Change_Speed=3000;
    private String feature_video_replace;
    private String feature_image_replace;
    News news2;

    private CustomTextView tvBreakingNews;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private BreakingNews breaking_news;
    private News mTopVideo;
    private News mViralVideo;
    int add_status;
    Boolean pos1obj1=false,pos1obj2=false,pos1arr1=false,pos1arr2=false;
    Boolean pos2obj1=false,pos2obj2=false,pos2arr1=false,pos2arr2=false;
    Boolean pos3obj1=false,pos3obj2=false,pos3arr1=false,pos3arr2=false;
    Boolean pos4obj1=false,pos4obj2=false,pos4arr1=false,pos4arr2=false;
    Boolean pos5obj1=false,pos5obj2=false,pos5arr1=false,pos5arr2=false;
    Boolean pos6obj1=false,pos6obj2=false,pos6arr1=false,pos6arr2=false;
    String pos1_obj1_name,pos1_obj1_image,pos1_obj1_url,pos1_obj2_name,pos1_obj2_image,pos1_obj2_url;
    String pos2_obj1_name,pos2_obj1_image,pos2_obj1_url,pos2_obj2_name,pos2_obj2_image,pos2_obj2_url;
    String pos3_obj1_name,pos3_obj1_image,pos3_obj1_url,pos3_obj2_name,pos3_obj2_image,pos3_obj2_url;
    String pos4_obj1_name,pos4_obj1_image,pos4_obj1_url,pos4_obj2_name,pos4_obj2_image,pos4_obj2_url;
    String pos5_obj1_name,pos5_obj1_image,pos5_obj1_url,pos5_obj2_name,pos5_obj2_image,pos5_obj2_url;
    String pos6_obj1_name,pos6_obj1_image,pos6_obj1_url,pos6_obj2_name,pos6_obj2_image,pos6_obj2_url;

    String pos1_arr1_name1,pos1_arr1_name2,pos1_arr1_name3;
    String pos1_arr1_image1,pos1_arr1_image2,pos1_arr1_image3;
    String pos1_arr1_url1,pos1_arr1_url2,pos1_arr1_url3;

    String pos1_arr2_name1,pos1_arr2_name2,pos1_arr2_name3;
    String pos1_arr2_image1,pos2_arr2_image2,pos1_arr2_image3;
    String pos1_arr2_url1,pos1_arr2_url2,pos1_arr2_url3;

    String pos2_arr1_name1,pos2_arr1_name2,pos2_arr1_name3;
    String pos2_arr1_image1,pos2_arr1_image2,pos2_arr1_image3;
    String pos2_arr1_url1,pos2_arr1_url3,pos2_arr1_url2;

    String pos2_arr2_name1,pos2_arr2_name2,pos2_arr2_name3;
    String pos2_arr2_image1,pos1_arr2_image2,pos2_arr2_image3;
    String pos2_arr2_url1,pos2_arr2_url2,pos2_arr2_url3;

    String pos3_arr1_name1,pos3_arr1_name2,pos3_arr1_name3;
    String pos3_arr1_image1,pos3_arr1_image2,pos3_arr1_image3;
    String pos3_arr1_url1,pos3_arr1_url3,pos3_arr1_url2;

    String pos3_arr2_name1,pos3_arr2_name2,pos3_arr2_name3;
    String pos3_arr2_image1,pos3_arr2_image2,pos3_arr2_image3;
    String pos3_arr2_url1,pos3_arr2_url2,pos3_arr2_url3;

    String pos4_arr1_name1,pos4_arr1_name2,pos4_arr1_name3;
    String pos4_arr1_image1,pos4_arr1_image2,pos4_arr1_image3;
    String pos4_arr1_url1,pos4_arr1_url3,pos4_arr1_url2;

    String pos4_arr2_name1,pos4_arr2_name2,pos4_arr2_name3;
    String pos4_arr2_image1,pos4_arr2_image2,pos4_arr2_image3;
    String pos4_arr2_url1,pos4_arr2_url2,pos4_arr2_url3;

    String pos5_arr1_name1,pos5_arr1_name2,pos5_arr1_name3;
    String pos5_arr1_image1,pos5_arr1_image2,pos5_arr1_image3;
    String pos5_arr1_url1,pos5_arr1_url3,pos5_arr1_url2;

    String pos5_arr2_name1,pos5_arr2_name2,pos5_arr2_name3;
    String pos5_arr2_image1,pos5_arr2_image2,pos5_arr2_image3;
    String pos5_arr2_url1,pos5_arr2_url2,pos5_arr2_url3;

    String pos6_arr1_name1,pos6_arr1_name2,pos6_arr1_name3;
    String pos6_arr1_image1,pos6_arr1_image2,pos6_arr1_image3;
    String pos6_arr1_url1,pos6_arr1_url3,pos6_arr1_url2;

    String pos6_arr2_name1,pos6_arr2_name2,pos6_arr2_name3;
    String pos6_arr2_image1,pos6_arr2_image2,pos6_arr2_image3;
    String pos6_arr2_url1,pos6_arr2_url2,pos6_arr2_url3;


    private static final int LOADING_TIME = 3000;
    private static final String TAG = "HomeActivity";
    private static final int INIT_CENTER_LOG_MOTION_DURATION = 2000;

    private static final float LAYOUT_WEIGHT_HEADER_ADVERTISEMENT = 0.08f;
    private static final float ODISHAHEAD_HEIGHT = 0.05f;
    private static final float LAYOUT_WEIGHT_TOP_FEATURED_NEWS = 0.2625f;
    private static final float LAYOUT_WEIGHT_TOP_FEATURED_NEWS1= 0.2325f;
    private static final float LAYOUT_WEIGHT_ODISHANEWS = 0.2025f;
    private static final float LAYOUT_WEIGHT_TOP_NEWS = 0.13f;
    private static final float LAYOUT_WEIGHT_CONFERENCE = 0.13f;
    private static final float LAYOUT_WEIGHT_CITIZEN_JOURNALIST = 0.13f;
    private static final float LAYOUT_WEIGHT_BREAKING_NEWS = 0.0775f;
    private static final float LAYOUT_WEIGHT_FOOTER_ADVERTISEMENT = 0.08f;
    private static final float LAYOUT_WEIGHT_CATEGORY_NEWS = 0.2625f;

    private CitizenJournalistVideos citizenJournalistVideos = new CitizenJournalistVideos(Parcel.obtain());
    private News featured_news = new News(Parcel.obtain());
    private List<News> newsList = new ArrayList<News>();
    private ConferenceNews conferenceNews;
    private List<Advertisement> advertisementList = new ArrayList<Advertisement>();
    private List<News> categoryNews = new ArrayList<News>();
    private List<Category> categoryList = new ArrayList<Category>();
    private News odisha_plus_news = null;

    private boolean mIsContentLoaded = false;
    private boolean mIsBreakingNewsLoaded = false;
    private boolean mIsCategoriesLoaded = false;
    private boolean mIsContentDisplayed = false;

    private int ADVERTISEMENT_HEADER = 0;
    private int ADVERTISEMENT_MIDDLE = 0;
    private int ADVERTISEMENT_FOOTER = 0;
    private boolean isFromNotification = false;
    String lang="English";

    int odisanewscount=0;
    private static RelativeLayout[] relativeolder;
    ArrayList<OdishaNews> arrayList_odishanews;
    Boolean odishanews = false;


    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS=1;
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO=1;
    /*String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CALL_PHONE
            ,Manifest.permission.READ_CONTACTS};*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
            lang = getSharedPreferences(Config.SHAREDPREFERENCE_LANGUAGE, 0).getString(Config.LANG, null);

    // if the android version is greated than marsmallow , then it will ask for ermission otherwise not
    /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
        // here it is checking whether the permission is granted previously or not
        if (!hasPermissions(this, PERMISSIONS)){
            //Permission is granted
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
         }
    }*/


        Fabric.with(this, new Crashlytics());
        if(getIntent() != null
                && getIntent().getExtras() != null
                && getIntent().getExtras().containsKey("isFromNotification")) {
            isFromNotification = getIntent().getExtras().getBoolean("isFromNotification");
        }
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(com.lipl.ommcom.Notification.Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(com.lipl.ommcom.Notification.Config.TOPIC_GLOBAL);


                } else if (intent.getAction().equals(com.lipl.ommcom.Notification.Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                 //   Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();


                }
            }
        };

        CustomTextView tvBreakingNewsNo = (CustomTextView) findViewById(R.id.tvBreakingNewsNo);
        tvBreakingNewsNo.setVisibility(View.GONE);
        getOdishanews();
        loadAllData();
        setupToolbar();
        init();
        intro();
        getAdvertisements();
        //GCMManager.getInstance(this).registerListener(this);
        String breaking_news_notifiction_key = getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 1)
                .getString(Config.SP_BREAKING_NEWS_KEY, "No News in preference");
        Log.i("HomeActivity", "Notification " + breaking_news_notifiction_key);
        //SingleTon.getInstance().setBreakingNewsNotificationListener(this);
    }

    private void getOdishanews() {
        arrayList_odishanews=new ArrayList<>();
        OdihaNews odihaNews = new OdihaNews();
        odihaNews.execute();
    }

    private void getAdvertisements() {
        AllAds allAds = new AllAds();
        allAds.execute();
    }

    /*
    * GET ODISHA NEWS
    * */
    /*
* GET  LIST ASYNTASK*/
    private class OdihaNews extends AsyncTask<String, Void, Void> {


        private static final String TAG = "ALL ODISHA NEWS";
        int server_status;


        @Override
        protected void onPreExecute() {


        }

        @Override
        protected Void doInBackground(String... params) {


            try {
                InputStream in = null;
                int resCode = -1;
                String link = null;
                if(lang==null || lang.contentEquals("English")) {
                   // link="https://www.ommcomnews.com/public/api/v0.1/odishaNewsHome"+Config.EN_CONENT;
                    link="https://www.ommcomnews.com/api/v0.1/odishaNewsHome"+Config.EN_CONENT;

                }
                else{
                    //link="https://www.ommcomnews.com/public/api/v0.1/odishaNewsHome"+Config.OD_CONENT;
                    link="https://www.ommcomnews.com/api/v0.1/odishaNewsHome"+Config.OD_CONENT;

                }
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setAllowUserInteraction(false);
                conn.setInstanceFollowRedirects(true);
                Uri.Builder builder = null;
                builder = new Uri.Builder()
                        .appendQueryParameter("", "");

                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                resCode = conn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    in = conn.getInputStream();
                }
                if (in == null) {
                    return null;
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String response = "", data = "";

                while ((data = reader.readLine()) != null) {
                    response += data + "\n";
                }

                /*
* "odisha_news": [
        {
            "id": "22412",
            "name": "BJD MP Ram Chandra Hansdah Granted Bail By Supreme Court",
            "odia_name": "BJD MP Ram Chandra Hansdah Granted Bail By Supreme Court",
            "slug": "bjd-mp-ram-chandra-hansdah-granted-bail-by-supreme-court",
            "cat_id": "4",
            "sub_cat_id": "0",
            "is_hot": "1",
            "is_top_video": null,
            "is_trash": "0",
            "meta_title": "BJD MP Ram Chandra Hansdah Granted Bail By Supreme Court",
            "featured_image": "130670557315306020941168967663.jpg",
            "journalist_name": "Anisha Khatun",
            "odia_journalist_name": "",
            "youtube_link": "",
            "news_location": "",
            "odia_news_location": "",
            "short_description": "The Supreme Court of India on Tuesday granted bail to BJD Lok Sabha member Ram Chandra Hansdah in the Nabadiganta chit fund scam.",
            "odia_short_description": "",
            "long_description": "**Bhubaneswar:**                                    The Supreme Court of India on Tuesday granted bail to BJD Lok Sabha member Ram Chandra Hansdah in the Nabadiganta chit fund scam.\r\n\r\nEarlier, CBI had arrested Hansdah along with two former Odisha MLAs Hitesh Kumar Bagartti and Subarna Naik in the multi-crore chit fund scam involving Nabadiganta Capital Service which allegedly duped hundreds of investors. He was in jail since November 4, 2014. \r\n\r\nWhile probing into the scam, the CBI had recovered Rs 28 lakh cash from Hansdah's house in Mayurbhanj district in July 2014  but he claimed it to be his personal and party fund.\r\n\r\n\r\n\r\n\r\nAlso Read: [Odisha: Under-Trial Prisoner Of Boudh Sub-Jail Die, Family Cries Foul](https://www.ommcomnews.com/public/odisha-under-trial-prisoner-of-boudh-sub-jail-die-family-cries-foul-)\r\n",
            "odia_long_description": "",
            "tags": "BJD MP Ram Chandra Hansdah Granted Bail By Supreme Court",
            "is_video": "0",
            "is_image": "0",
            "file_path": null,
            "video_title": "",
            "odia_video_title": "",
            "attachment_file": null,
            "is_draft": "1",
            "is_publish": "1",
            "user_id": "1",
            "is_archive": "0",
            "publish_date": "2018-07-03 12:30:16",
            "archive_date": null,
            "last_save_time": null,
            "is_approved": "1",
            "approved_by": "1",
            "approved_date": "2018-07-03 12:45:06",
            "is_enable": "1",
            "created_at": "2018-07-03 12:30:11",
            "updated_at": "2018-07-03 13:09:15",
            "allow_comment": "1",
            "position": "1",
            "is_featured": "1",
            "is_top_story": "0",
            "is_viral": "0",
            "meta_desc": "The Supreme Court of India on Tuesday granted bail to BJD Lok Sabha member Ram Chandra Hansdah in the Nabadiganta chit fund scam.",
            "meta_keywords": "BJD MP Ram Chandra Hansdah Granted Bail By Supreme Court,ommcom news,Odisha news, odisha latest news, latest odisha news,odisha, odisha latest news, latest odisha news, latest news of odisha, latest news odisha, odisha news today, odisha current news, odia news, latest odia news, odia news today, odisha news online",
            "news_count": "57"
        },
* */

                Log.i(TAG, "Response : " + response);
                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response);
                   // server_status=res.getInt("status");
                    JSONArray news_array=res.getJSONArray("odisha_news");
                    if(news_array.length()>=1){
                        server_status =1;
                        odisanewscount=news_array.length();
                        for(int i=0;i<news_array.length();i++){
                            JSONObject newsobj=news_array.getJSONObject(i);
                            String id = newsobj.getString("id");
                            String name=newsobj.getString("name");
                            String odia_name=newsobj.getString("odia_name");
                            String slug=newsobj.getString("slug");
                            String meta_title=newsobj.getString("meta_title");
                            String featured_image=newsobj.getString("featured_image");
                            String journalist_name=newsobj.getString("journalist_name");
                            String youtube_link=newsobj.getString("youtube_link");
                            String short_description=newsobj.getString("short_description");
                            String long_description=newsobj.getString("long_description");
                            String tags=newsobj.getString("tags");
                            String publish_date=newsobj.getString("publish_date");
                            String meta_desc=newsobj.getString("meta_desc");
                            String meta_keywords=newsobj.getString("meta_keywords");
                            String news_count=newsobj.getString("news_count");
                            String is_video=newsobj.getString("is_video");
                            String approved_date=newsobj.getString("approved_date");
                            OdishaNews odishaNews = new OdishaNews(id,name,odia_name,slug,meta_title,featured_image,journalist_name,youtube_link,
                                    short_description,long_description,tags,publish_date,meta_desc,meta_keywords,news_count,is_video,approved_date);
                            arrayList_odishanews.add(odishaNews);

                        }
                    }
                }

                return null;


            } catch (Exception exception) {
                Log.e(TAG, "LoginAsync : doInBackground", exception);
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            if(Integer.valueOf(odisanewscount)<1){
                odishanews=false;
            }
            else{
                odishanews=true;

            }

        }
    }


 /*
* GET ADD LIST ASYNTASK*/
        private class AllAds extends AsyncTask<String, Void, Void> {


            private static final String TAG = "ALL ADDS";

            @Override
            protected void onPreExecute() {


            }

            @Override
            protected Void doInBackground(String... params) {


                try {
                    InputStream in = null;
                    int resCode = -1;
                    String link = null;
                    if(lang==null || lang.contentEquals("English")){
                        //link="https://www.ommcomnews.com/public/api/v0.1/getAllAdvertisements"+Config.EN_CONENT;
                        link="https://www.ommcomnews.com/api/v0.1/getAllAdvertisements"+Config.EN_CONENT;
                    }
                    else{
                        //link="https://www.ommcomnews.com/public/api/v0.1/getAllAdvertisements"+Config.OD_CONENT;
                        link="https://www.ommcomnews.com/api/v0.1/getAllAdvertisements"+Config.OD_CONENT;

                    }
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setAllowUserInteraction(false);
                    conn.setInstanceFollowRedirects(true);
                    Uri.Builder builder = null;
                    builder = new Uri.Builder()
                                .appendQueryParameter("", "");

                    String query = builder.build().getEncodedQuery();

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();

                    conn.connect();
                    resCode = conn.getResponseCode();
                    if (resCode == HttpURLConnection.HTTP_OK) {
                        in = conn.getInputStream();
                    }
                    if (in == null) {
                        return null;
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    String response = "", data = "";

                    while ((data = reader.readLine()) != null) {
                        response += data + "\n";
                    }

                    Log.i(TAG, "Response : " + response);

                /*
                *
                * {
                    "POS1OBJECT1": {
            "name": "3 NEW HEALTH SCHEMES FOR THE PEOPLE OF ODISHA",
            "video_full_path": "https://ommcomnews.com/public/advertisement/uploads/",
            "start_date": "2018-06-13",
            "end_date": "2018-06-20",
            "image_full_path": "https://ommcomnews.com/public/advertisement/uploads/15289017483-NEW-HEALTH-SCHEMES-FOR-THE-PEOPLE-OF-ODISHA.jpg",
            "url": ""
        },
        "POS4OBJECT1": {
            "name": "INTERNATIONAL LABOUR DAY",
            "video_full_path": "https://ommcomnews.com/public/advertisement/uploads/",
            "start_date": "2018-04-30",
            "end_date": "2018-05-07",
            "image_full_path": "https://ommcomnews.com/public/advertisement/uploads/1525102414INTERNATIONAL-LABOUR-DAY.jpg",
            "url": ""
        },
        "POS5OBJECT1": {
            "name": "ROAD SAFETY WEEK",
            "video_full_path": "https://ommcomnews.com/public/advertisement/uploads/",
            "start_date": "2018-04-29",
            "end_date": "2018-05-06",
            "image_full_path": "https://ommcomnews.com/public/advertisement/uploads/1525000998ROAD-SAFETY-WEEK.jpg",
            "url": ""
        }
                    },*/


                    if (response != null && response.length() > 0) {
                        JSONObject res = new JSONObject(response);
                        //add_status = res.getInt("status");
                       // if (add_status == 1) {
                            if(res.isNull("POS1OBJECT1") == false){
                                pos1obj1=true;
                                pos1arr1=false;
                                JSONObject obj = res.getJSONObject("POS1OBJECT1");
                                pos1_obj1_name=obj.getString("name");
                                pos1_obj1_image=obj.getString("image_full_path");
                                pos1_obj1_url=obj.getString("url");
                            }
                            if(res.isNull("POS1OBJECT2") == false){
                                pos1obj2=true;
                                pos1arr2=false;
                                JSONObject obj = res.getJSONObject("POS1OBJECT2");
                                pos1_obj2_name=obj.getString("name");
                                pos1_obj2_image=obj.getString("image_full_path");
                                pos1_obj2_url=obj.getString("url");
                            }if(res.isNull("POS2OBJECT1") == false){
                                pos2obj1=true;
                                pos2arr1=false;
                                JSONObject obj = res.getJSONObject("POS2OBJECT1");
                                pos2_obj1_name=obj.getString("name");
                                pos2_obj1_image=obj.getString("image_full_path");
                                pos2_obj1_url=obj.getString("url");
                            }
                            if(res.isNull("POS2OBJECT2") == false){
                                pos2obj2=true;
                                pos2arr2=false;
                                JSONObject obj = res.getJSONObject("POS2OBJECT2");
                                pos2_obj2_name=obj.getString("name");
                                pos2_obj2_image=obj.getString("image_full_path");
                                pos2_obj2_url=obj.getString("url");
                            }
                            if(res.isNull("POS3OBJECT1") == false){
                                pos3obj1=true;
                                pos3arr1=false;
                                JSONObject obj = res.getJSONObject("POS3OBJECT1");
                                pos3_obj1_name=obj.getString("name");
                                pos3_obj1_image=obj.getString("image_full_path");
                                pos3_obj1_url=obj.getString("url");
                            }
                            if(res.isNull("POS3OBJECT2") == false){
                                pos3obj2=true;
                                pos3arr2=false;
                                JSONObject obj = res.getJSONObject("POS3OBJECT2");
                                pos3_obj2_name=obj.getString("name");
                                pos3_obj2_image=obj.getString("image_full_path");
                                pos3_obj2_url=obj.getString("url");
                            }
                            if(res.isNull("POS4OBJECT1") == false){
                                pos4obj1=true;
                                pos4arr1=false;
                                JSONObject obj = res.getJSONObject("POS4OBJECT1");
                                pos4_obj1_name=obj.getString("name");
                                pos4_obj1_image=obj.getString("image_full_path");
                                pos4_obj1_url=obj.getString("url");
                            }
                            if(res.isNull("POS4OBJECT2") == false){
                                pos4obj2=true;
                                pos4arr2=false;
                                JSONObject obj = res.getJSONObject("POS4OBJECT2");
                                pos4_obj2_name=obj.getString("name");
                                pos4_obj2_image=obj.getString("image_full_path");
                                pos4_obj2_url=obj.getString("url");
                            }
                            if(res.isNull("POS5OBJECT1") == false){
                                pos5obj1=true;
                                pos5arr1=false;
                                JSONObject obj = res.getJSONObject("POS5OBJECT1");
                                pos5_obj1_name=obj.getString("name");
                                pos5_obj1_image=obj.getString("image_full_path");
                                pos5_obj1_url=obj.getString("url");
                            }
                            if(res.isNull("POS5OBJECT2") == false){
                                pos5obj2=true;
                                pos5arr2=false;
                                JSONObject obj = res.getJSONObject("POS5OBJECT2");
                                pos5_obj2_name=obj.getString("name");
                                pos5_obj2_image=obj.getString("image_full_path");
                                pos5_obj2_url=obj.getString("url");
                            }
                            if(res.isNull("POS6OBJECT1") == false){
                                pos6obj1=true;
                                pos6arr1=false;
                                JSONObject obj = res.getJSONObject("POS6OBJECT1");
                                pos6_obj1_name=obj.getString("name");
                                pos6_obj1_image=obj.getString("image_full_path");
                                pos6_obj1_url=obj.getString("url");
                            }
                            if(res.isNull("POS6OBJECT2") == false){
                                pos6obj2=true;
                                pos6arr2=false;
                                JSONObject obj = res.getJSONObject("POS6OBJECT2");
                                pos6_obj2_name=obj.getString("name");
                                pos6_obj2_image=obj.getString("image_full_path");
                                pos6_obj2_url=obj.getString("url");
                            }
                            if(res.isNull("POS1ARRAY1") == false){
                                pos1arr1=true;
                                pos1obj1=false;
                                JSONArray arr=res.getJSONArray("POS1ARRAY1");
                                for(int i=0;i<=2;i++){
                                    JSONObject o_list_obj1 = arr.getJSONObject(0);
                                    pos1_arr1_name1= o_list_obj1.getString("name");
                                    pos1_arr1_image1= o_list_obj1.getString("image_full_path");
                                    pos1_arr1_url1=o_list_obj1.getString("url");

                                    JSONObject o_list_obj2 = arr.getJSONObject(1);
                                    pos1_arr1_name2= o_list_obj2.getString("name");
                                    pos1_arr1_image2= o_list_obj2.getString("image_full_path");
                                    pos1_arr1_url2=o_list_obj2.getString("url");

                                    JSONObject o_list_obj3 = arr.getJSONObject(2);
                                    pos1_arr1_name3= o_list_obj3.getString("name");
                                    pos1_arr1_image3= o_list_obj3.getString("image_full_path");
                                    pos1_arr1_url3=o_list_obj3.getString("url");
                                }
                            }
                        if(res.isNull("POS1ARRAY2") == false){
                            pos1arr2=true;
                            pos1obj2=false;
                            JSONArray arr=res.getJSONArray("POS1ARRAY2");
                            for(int i=0;i<=2;i++){
                                JSONObject o_list_obj1 = arr.getJSONObject(0);
                                pos1_arr2_name1= o_list_obj1.getString("name");
                                pos1_arr2_image1= o_list_obj1.getString("image_full_path");
                                pos1_arr2_url1=o_list_obj1.getString("url");

                                JSONObject o_list_obj2 = arr.getJSONObject(1);
                                pos1_arr2_name2= o_list_obj2.getString("name");
                                pos1_arr2_image2= o_list_obj2.getString("image_full_path");
                                pos1_arr2_url2=o_list_obj2.getString("url");

                                JSONObject o_list_obj3 = arr.getJSONObject(2);
                                pos1_arr2_name3= o_list_obj3.getString("name");
                                pos1_arr2_image3= o_list_obj3.getString("image_full_path");
                                pos1_arr2_url3=o_list_obj3.getString("url");
                            }
                        }


                        if(res.isNull("POS2ARRAY1") == false){
                            pos2arr1=true;
                            pos2obj1=false;
                            JSONArray arr=res.getJSONArray("POS2ARRAY1");
                            for(int i=0;i<=2;i++){
                                JSONObject o_list_obj1 = arr.getJSONObject(0);
                                pos2_arr1_name1= o_list_obj1.getString("name");
                                pos2_arr1_image1= o_list_obj1.getString("image_full_path");
                                pos2_arr1_url1=o_list_obj1.getString("url");

                                JSONObject o_list_obj2 = arr.getJSONObject(1);
                                pos2_arr1_name2= o_list_obj2.getString("name");
                                pos2_arr1_image2= o_list_obj2.getString("image_full_path");
                                pos2_arr1_url2=o_list_obj2.getString("url");

                                JSONObject o_list_obj3 = arr.getJSONObject(2);
                                pos2_arr1_name3= o_list_obj3.getString("name");
                                pos2_arr1_image3= o_list_obj3.getString("image_full_path");
                                pos2_arr1_url3=o_list_obj3.getString("url");
                            }
                        }
                        if(res.isNull("POS2ARRAY2") == false){
                            pos2arr2=true;
                            pos2obj2=false;
                            JSONArray arr=res.getJSONArray("POS2ARRAY2");
                            for(int i=0;i<=2;i++){
                                JSONObject o_list_obj1 = arr.getJSONObject(0);
                                pos2_arr2_name1= o_list_obj1.getString("name");
                                pos2_arr2_image1= o_list_obj1.getString("image_full_path");
                                pos2_arr2_url1=o_list_obj1.getString("url");

                                JSONObject o_list_obj2 = arr.getJSONObject(1);
                                pos2_arr2_name2= o_list_obj2.getString("name");
                                pos2_arr2_image2= o_list_obj2.getString("image_full_path");
                                pos2_arr2_url2=o_list_obj2.getString("url");

                                JSONObject o_list_obj3 = arr.getJSONObject(2);
                                pos2_arr2_name3= o_list_obj3.getString("name");
                                pos2_arr2_image3= o_list_obj3.getString("image_full_path");
                                pos2_arr2_url3=o_list_obj3.getString("url");
                            }
                        }

                        if(res.isNull("POS3ARRAY1") == false){
                            pos3arr1=true;
                            pos3obj1=false;
                            JSONArray arr=res.getJSONArray("POS3ARRAY1");
                            for(int i=0;i<=2;i++){
                                JSONObject o_list_obj1 = arr.getJSONObject(0);
                                pos3_arr1_name1= o_list_obj1.getString("name");
                                pos3_arr1_image1= o_list_obj1.getString("image_full_path");
                                pos3_arr1_url1=o_list_obj1.getString("url");

                                JSONObject o_list_obj2 = arr.getJSONObject(1);
                                pos3_arr1_name2= o_list_obj2.getString("name");
                                pos3_arr1_image2= o_list_obj2.getString("image_full_path");
                                pos3_arr1_url2=o_list_obj2.getString("url");

                                JSONObject o_list_obj3 = arr.getJSONObject(2);
                                pos3_arr1_name3= o_list_obj3.getString("name");
                                pos3_arr1_image3= o_list_obj3.getString("image_full_path");
                                pos3_arr1_url3=o_list_obj3.getString("url");
                            }
                        }
                        if(res.isNull("POS3ARRAY2") == false){
                            pos3arr2=true;
                            pos3obj2=false;
                            JSONArray arr=res.getJSONArray("POS3ARRAY2");
                            for(int i=0;i<=2;i++){
                                JSONObject o_list_obj1 = arr.getJSONObject(0);
                                pos3_arr2_name1= o_list_obj1.getString("name");
                                pos3_arr2_image1= o_list_obj1.getString("image_full_path");
                                pos3_arr2_url1=o_list_obj1.getString("url");

                                JSONObject o_list_obj2 = arr.getJSONObject(1);
                                pos3_arr2_name2= o_list_obj2.getString("name");
                                pos3_arr2_image2= o_list_obj2.getString("image_full_path");
                                pos3_arr2_url2=o_list_obj2.getString("url");

                                JSONObject o_list_obj3 = arr.getJSONObject(2);
                                pos3_arr2_name3= o_list_obj3.getString("name");
                                pos3_arr2_image3= o_list_obj3.getString("image_full_path");
                                pos3_arr2_url3=o_list_obj3.getString("url");
                            }
                        }

                        if(res.isNull("POS4ARRAY1") == false){
                            pos4arr1=true;
                            pos4obj1=false;
                            JSONArray arr=res.getJSONArray("POS4ARRAY1");
                            for(int i=0;i<=2;i++){
                                JSONObject o_list_obj1 = arr.getJSONObject(0);
                                pos4_arr1_name1= o_list_obj1.getString("name");
                                pos4_arr1_image1= o_list_obj1.getString("image_full_path");
                                pos4_arr1_url1=o_list_obj1.getString("url");

                                JSONObject o_list_obj2 = arr.getJSONObject(1);
                                pos4_arr1_name2= o_list_obj2.getString("name");
                                pos4_arr1_image2= o_list_obj2.getString("image_full_path");
                                pos4_arr1_url2=o_list_obj2.getString("url");

                                JSONObject o_list_obj3 = arr.getJSONObject(2);
                                pos4_arr1_name3= o_list_obj3.getString("name");
                                pos4_arr1_image3= o_list_obj3.getString("image_full_path");
                                pos4_arr1_url3=o_list_obj3.getString("url");
                            }
                        }
                        if(res.isNull("POS4ARRAY2") == false){
                            pos4arr2=true;
                            pos4obj2=false;
                            JSONArray arr=res.getJSONArray("POS4ARRAY2");
                            for(int i=0;i<=2;i++){
                                JSONObject o_list_obj1 = arr.getJSONObject(0);
                                pos4_arr2_name1= o_list_obj1.getString("name");
                                pos4_arr2_image1= o_list_obj1.getString("image_full_path");
                                pos4_arr2_url1=o_list_obj1.getString("url");

                                JSONObject o_list_obj2 = arr.getJSONObject(1);
                                pos4_arr2_name2= o_list_obj2.getString("name");
                                pos4_arr2_image2= o_list_obj2.getString("image_full_path");
                                pos4_arr2_url2=o_list_obj2.getString("url");

                                JSONObject o_list_obj3 = arr.getJSONObject(2);
                                pos4_arr2_name3= o_list_obj3.getString("name");
                                pos4_arr2_image3= o_list_obj3.getString("image_full_path");
                                pos4_arr2_url3=o_list_obj3.getString("url");
                            }
                        }
                        if(res.isNull("POS5ARRAY1") == false){
                            pos5arr1=true;
                            pos5obj1=false;
                            JSONArray arr=res.getJSONArray("POS5ARRAY1");
                            for(int i=0;i<=2;i++){
                                JSONObject o_list_obj1 = arr.getJSONObject(0);
                                pos5_arr1_name1= o_list_obj1.getString("name");
                                pos5_arr1_image1= o_list_obj1.getString("image_full_path");
                                pos5_arr1_url1=o_list_obj1.getString("url");

                                JSONObject o_list_obj2 = arr.getJSONObject(1);
                                pos5_arr1_name2= o_list_obj2.getString("name");
                                pos5_arr1_image2= o_list_obj2.getString("image_full_path");
                                pos5_arr1_url2=o_list_obj2.getString("url");

                                JSONObject o_list_obj3 = arr.getJSONObject(2);
                                pos5_arr1_name3= o_list_obj3.getString("name");
                                pos5_arr1_image3= o_list_obj3.getString("image_full_path");
                                pos5_arr1_url3=o_list_obj3.getString("url");
                            }
                        }
                        if(res.isNull("POS5ARRAY2") == false){
                            pos5arr2=true;
                            pos5obj2=false;
                            JSONArray arr=res.getJSONArray("POS5ARRAY2");
                            for(int i=0;i<=2;i++){
                                JSONObject o_list_obj1 = arr.getJSONObject(0);
                                pos5_arr2_name1= o_list_obj1.getString("name");
                                pos5_arr2_image1= o_list_obj1.getString("image_full_path");
                                pos5_arr2_url1=o_list_obj1.getString("url");

                                JSONObject o_list_obj2 = arr.getJSONObject(1);
                                pos5_arr2_name2= o_list_obj2.getString("name");
                                pos5_arr2_image2= o_list_obj2.getString("image_full_path");
                                pos5_arr2_url2=o_list_obj2.getString("url");

                                JSONObject o_list_obj3 = arr.getJSONObject(2);
                                pos5_arr2_name3= o_list_obj3.getString("name");
                                pos5_arr2_image3= o_list_obj3.getString("image_full_path");
                                pos5_arr2_url3=o_list_obj3.getString("url");
                            }
                        }
                        if(res.isNull("POS6ARRAY1") == false){
                            pos6arr1=true;
                            pos6obj1=false;
                            JSONArray arr=res.getJSONArray("POS6ARRAY1");
                            for(int i=0;i<=2;i++){
                                JSONObject o_list_obj1 = arr.getJSONObject(0);
                                pos6_arr1_name1= o_list_obj1.getString("name");
                                pos6_arr1_image1= o_list_obj1.getString("image_full_path");
                                pos6_arr1_url1=o_list_obj1.getString("url");

                                JSONObject o_list_obj2 = arr.getJSONObject(1);
                                pos6_arr1_name2= o_list_obj2.getString("name");
                                pos6_arr1_image2= o_list_obj2.getString("image_full_path");
                                pos6_arr1_url2=o_list_obj2.getString("url");

                                JSONObject o_list_obj3 = arr.getJSONObject(2);
                                pos6_arr1_name3= o_list_obj3.getString("name");
                                pos6_arr1_image3= o_list_obj3.getString("image_full_path");
                                pos6_arr1_url3=o_list_obj3.getString("url");
                            }
                        }
                        if(res.isNull("POS6ARRAY2") == false){
                            pos6arr2=true;
                            pos6obj2=false;
                            JSONArray arr=res.getJSONArray("POS6ARRAY2");
                            for(int i=0;i<=2;i++){
                                JSONObject o_list_obj1 = arr.getJSONObject(0);
                                pos6_arr2_name1= o_list_obj1.getString("name");
                                pos6_arr2_image1= o_list_obj1.getString("image_full_path");
                                pos6_arr2_url1=o_list_obj1.getString("url");

                                JSONObject o_list_obj2 = arr.getJSONObject(1);
                                pos6_arr2_name2= o_list_obj2.getString("name");
                                pos6_arr2_image2= o_list_obj2.getString("image_full_path");
                                pos6_arr2_url2=o_list_obj2.getString("url");

                                JSONObject o_list_obj3 = arr.getJSONObject(2);
                                pos6_arr2_name3= o_list_obj3.getString("name");
                                pos6_arr2_image3= o_list_obj3.getString("image_full_path");
                                pos6_arr2_url3=o_list_obj3.getString("url");
                            }
                        }
                       // }

                    }

                    return null;


                } catch (Exception exception) {
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                }

                return null;
            }


            @Override
            protected void onPostExecute(Void user) {
                super.onPostExecute(user);

            }
        }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void setupToolbar() {
        if(toolbar == null) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
        }
        if(mToolbarBottomBorder == null){
            mToolbarBottomBorder = (LinearLayout) findViewById(R.id.toolbar_bottom_border);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
    }

    private void setToolbarInvisible(){
        if(toolbar == null) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
        }
        if(mToolbarBottomBorder == null){
            mToolbarBottomBorder = (LinearLayout) findViewById(R.id.toolbar_bottom_border);
        }
        toolbar.setVisibility(View.GONE);
        mToolbarBottomBorder.setVisibility(View.GONE);
    }

    private void setToolbarVisible(){
        if(toolbar == null) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
        }
        if(mToolbarBottomBorder == null){
            mToolbarBottomBorder = (LinearLayout) findViewById(R.id.toolbar_bottom_border);
        }
        toolbar.setVisibility(View.VISIBLE);
        mToolbarBottomBorder.setVisibility(View.VISIBLE);
    }

    private void init(){
        setToolbarInvisible();
        //setToolbarVisible();
        //displayContent();


        layoutLogoImg = (RelativeLayout) findViewById(R.id.layoutLogoImg);
        int yPosition= -(Util.getScreenHeight() / 2);
        AnimatorPath path = new AnimatorPath();
        path.moveTo(0, 0);
        path.lineTo(0, yPosition);
        anim = ObjectAnimator.ofObject(this, "buttonLoc",
                new PathEvaluator(), path.getPoints().toArray());
        anim.setDuration(INIT_CENTER_LOG_MOTION_DURATION);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                AnimationUtil.zoomOut(layoutLogoImg, INIT_CENTER_LOG_MOTION_DURATION, 0);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setToolbarVisible();
                AnimationUtil.slideInOfDifferentToolbarElements(toolbar);
                AnimationUtil.slideDown(mToolbarBottomBorder);
                displayContent();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        RelativeLayout layoutContent = (RelativeLayout) findViewById(R.id.layoutContentFirst);
        layoutContent.setVisibility(View.GONE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void intro(){
        AnimationUtil.fadeIn(layoutLogoImg, 2000, 0);
       // playAudio();
    }

    private void withoutAudioPlay(){

    }

    private void loadNewsData(final boolean isRefresh){

        if(Util.getNetworkConnectivityStatus(HomeActivity.this) == false){
            Util.showDialogToShutdownApp(HomeActivity.this);
            return;
        }

        //final ProgressBar pBar = (ProgressBar) findViewById(R.id.pBar);
        new AsyncTask<Void, Void, Boolean>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //pBar.setVisibility(View.VISIBLE);
                isLatestData = false;
            }

            @Override
            protected void onPostExecute(Boolean aVoid) {
                super.onPostExecute(aVoid);
                //pBar.setVisibility(View.GONE);
               // anim.start();


                if(aVoid != null && aVoid.booleanValue() == false){
                    Util.showDialogToShutdownApp(HomeActivity.this);
                    return;
                }

                mIsContentLoaded = true;
                if(isRefresh){
                    if(mSwipeRefreshLayout != null){
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    displayContent();
                } else {
                    anim.start();
                /*if(mIsCategoriesLoaded
                        && mIsBreakingNewsLoaded
                       // && mIsContentLoaded
                        && mIsContentDisplayed == false) {
                    if(mSwipeRefreshLayout != null){
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    if(isAudioDone) {
                        anim.start();
                    }*/
                }
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                InputStream in = null;
                int resCode = -1;

                try {
                    String link = null;
                    if(lang==null || lang.contentEquals("English")) {
                       link=Config.API_BASE_URL + Config.HOME_SCREEN_DETAILS_API+Config.EN_CONENT;
                    }
                    else{
                        link=Config.API_BASE_URL + Config.HOME_SCREEN_DETAILS_API+Config.OD_CONENT;
                    }
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    //conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setAllowUserInteraction(false);
                    //conn.setInstanceFollowRedirects(true);
                    conn.setRequestMethod("GET");
                    conn.connect();

                    resCode = conn.getResponseCode();
                    if (resCode == HttpURLConnection.HTTP_OK) {
                        in = conn.getInputStream();
                    }
                    Log.i("HomeActivity : loadNewsData()", "Error :" + resCode);
                    if (in == null) {
                        return null;
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    String response = "", data = "";

                    while ((data = reader.readLine()) != null) {
                        response += data + "\n";
                    }

                    Log.i(TAG, "Response : " + response);

                    JSONObject res = new JSONObject(response);
                    if(res.isNull("FEATUREDNEWS") == false) {
                        JSONObject fnews = res.getJSONObject("FEATUREDNEWS");

                        if (fnews.isNull("id") == false) {
                            String id = fnews.getString("id");
                            featured_news.setId(id);
                        }

                        if (fnews.isNull("name") == false) {
                            String name = fnews.getString("name");
                            featured_news.setName(name);
                        }

                        if (fnews.isNull("slug") == false) {
                            String slug = fnews.getString("slug");
                            featured_news.setSlug(slug);
                        }

                        if (fnews.isNull("featured_image") == false) {
                            String featured_image = fnews.getString("featured_image");
                            featured_news.setImage(featured_image);
                        }

                        if (fnews.isNull("short_description") == false){
                            String short_description = fnews.getString("short_description");
                            featured_news.setShort_description(short_description);
                        }

                        if(fnews.isNull("file_path") == false) {
                            String file_path = fnews.getString("file_path");
                            featured_news.setFile_path(file_path);
                        }

                        if(fnews.isNull("approved_date") == false) {
                            String approved_date = fnews.getString("approved_date");
                            featured_news.setApproved_date(approved_date);
                        }

                        if(fnews.isNull("position") == false) {
                            String position = fnews.getString("position");
                            featured_news.setPosition(position);
                        }

                        if(fnews.isNull("user_id") == false) {
                            String user_id = fnews.getString("user_id");
                            featured_news.setUser_id(user_id);
                        }

                        if(fnews.isNull("username") ==false) {
                            String username = fnews.getString("username");
                            featured_news.setJounalist_name(username);
                        }
                        if(fnews.isNull("is_video") ==false) {
                            String is_video = fnews.getString("is_video");
                            featured_news.setIs_video(is_video);
                        }
                        if(fnews.isNull("is_image") ==false) {
                            String is_image = fnews.getString("is_image");
                            featured_news.setIs_image(is_image);
                        }
                    }
                    if(res.isNull("TOPNEWSNOW") == false){
                        JSONArray top_news_now = res.getJSONArray("TOPNEWSNOW");
                        for(int i = 0; i < top_news_now.length(); i++){
                            News news = new News(Parcel.obtain());

                            if(top_news_now.getJSONObject(i).isNull("id") == false) {
                                String id = top_news_now.getJSONObject(i).getString("id");
                                news.setId(id);
                            }
                            if(top_news_now.getJSONObject(i).isNull("name") == false) {
                                String id = top_news_now.getJSONObject(i).getString("name");
                                news.setName(id);
                            }
                            if(top_news_now.getJSONObject(i).isNull("featured_image") == false) {
                                String featured_image = top_news_now.getJSONObject(i).getString("featured_image");
                                news.setImage(featured_image);
                            }
                            if(top_news_now.getJSONObject(i).isNull("slug") == false) {
                                String slug = top_news_now.getJSONObject(i).getString("slug");
                                news.setSlug(slug);
                            }
                            if(top_news_now.getJSONObject(i).isNull("is_image") == false) {
                                String slug = top_news_now.getJSONObject(i).getString("is_image");
                                news.setIs_image(slug);
                            }
                            if(top_news_now.getJSONObject(i).isNull("file_path") == false) {
                                String slug = top_news_now.getJSONObject(i).getString("file_path");
                                news.setFile_path(slug);
                            }
                            if(top_news_now.getJSONObject(i).isNull("is_video") == false) {
                                String slug = top_news_now.getJSONObject(i).getString("is_video");
                                news.setIs_video(slug);
                            }
                            newsList.add(news);
                        }
                    }
                    if(res.isNull("CONFERENCE_NEWS")==true){
                        if(res.isNull("REPLACE_CONFERENCE_NEWS")==false){
                            JSONObject conference_replace=res.getJSONObject("REPLACE_CONFERENCE_NEWS");
                            if(conference_replace.isNull("feature_video")==false){
                                feature_video_replace=conference_replace.getString("feature_video");
                            }
                            if(conference_replace.isNull("feature_image")==false){
                                feature_image_replace=conference_replace.getString("feature_image");
                            }
                        }
                    }
                    if(res.isNull("CONFERENCE_NEWS") == false){
                        conferenceNews = new ConferenceNews(Parcel.obtain());
                        JSONObject conference_news = res.getJSONObject("CONFERENCE_NEWS");
                        if(conference_news.isNull("id") == false) {
                            String id = conference_news.getString("id");
                            conferenceNews.setId(id);
                        }
                        if(conference_news.isNull("featured_image") == false){
                            String featured_image = conference_news.getString("featured_image");
                            conferenceNews.setFeatured_image(featured_image);
                        }

                        if(conference_news.isNull("name") == false) {
                            String name = conference_news.getString("name");
                            conferenceNews.setName(name);
                        }
                        if(conference_news.isNull("short_desc") == false) {
                            String short_desc = conference_news.getString("short_desc");
                            conferenceNews.setShort_desc(short_desc);
                        }
                        if(conference_news.isNull("long_desc") == false) {
                            String long_desc = conference_news.getString("long_desc");
                            conferenceNews.setLong_desc(long_desc);
                        }
                        if(conference_news.isNull("conference_banner") == false) {
                            String conference_banner = conference_news.getString("conference_banner");
                            conferenceNews.setConference_banner(conference_banner);
                        }
                        if(conference_news.isNull("started_at") == false){
                            String started_at = conference_news.getString("started_at");
                            conferenceNews.setStarted_at(started_at);
                        }
                        if(conference_news.isNull("start_time") == false) {
                            String start_time = conference_news.getString("start_time");
                            conferenceNews.setStart_time(start_time);
                        }
                        if(conference_news.isNull("end_time") == false) {
                            String end_time = conference_news.getString("end_time");
                            conferenceNews.setEnd_time(end_time);
                        }
                    }
                    if(res.isNull("CITIZEN_CUSTOMIZE") == false){
                        JSONObject citizen_customize = res.getJSONObject("CITIZEN_CUSTOMIZE");
                        if(citizen_customize.isNull("name") == false) {
                            String name = citizen_customize.getString("name");
                            citizenJournalistVideos.setName(name);
                        }
                        if(citizen_customize.isNull("file_path") == false) {
                            String file_path = citizen_customize.getString("file_path");
                            citizenJournalistVideos.setFile_path(file_path);
                        }
                    }

                    /*
                    * {
                          "name": "Iphone",
                          "advertisement_section_id": "1",
                          "file_path": "150485319914622767751844484274-adv1.jpg",
                          "url_link": "https://www.google.co.in/"
                        }
                    * */
                    if(res.isNull("ADVERTISEMENT_HEADER") == false){
                        ADVERTISEMENT_HEADER = res.getInt("ADVERTISEMENT_HEADER");
                    }

                    if(res.isNull("ADVERTISEMENT_MIDDLE") == false){
                        ADVERTISEMENT_MIDDLE = res.getInt("ADVERTISEMENT_MIDDLE");
                    }

                    if(res.isNull("ADVERTISEMENT_FOOTER") == false){
                        ADVERTISEMENT_FOOTER = res.getInt("ADVERTISEMENT_FOOTER");
                    }

                    if(res.isNull("ADVERTISEMENT") == false){
                        JSONArray top_news_now = res.getJSONArray("ADVERTISEMENT");
                        for(int i = 0; i < top_news_now.length(); i++){
                            Advertisement advertisement = new Advertisement(Parcel.obtain());
                            if(top_news_now.getJSONObject(i).isNull("name") == false){
                                String name = top_news_now.getJSONObject(i).getString("name");
                                advertisement.setName(name);
                            }
                            if(top_news_now.getJSONObject(i).isNull("advertisement_section_id") == false){
                                String section = top_news_now.getJSONObject(i).getString("advertisement_section_id");
                                advertisement.setSection(section);
                            }
                            if(top_news_now.getJSONObject(i).isNull("file_path") == false){
                                String file_path = top_news_now.getJSONObject(i).getString("file_path");
                                advertisement.setFile_path(file_path);
                            }
                            if(top_news_now.getJSONObject(i).isNull("url_link") == false){
                                String url_link = top_news_now.getJSONObject(i).getString("url_link");
                                advertisement.setUrl_link(url_link);
                            }
                            advertisementList.add(advertisement);
                        }
                    }

                    if(res.isNull("CATEGORY_NEWS") == false){
                        JSONArray category_news_list = res.getJSONArray("CATEGORY_NEWS");
                        categoryNews.clear();
                        for(int i = 0; i < category_news_list.length(); i++){

                            News news = new News(Parcel.obtain());
                            if(category_news_list.getJSONObject(i).isNull("name") == false){
                                String name = category_news_list.getJSONObject(i).getString("name");
                                news.setName(name);
                            }
                            if(category_news_list.getJSONObject(i).isNull("slug") == false){
                                String slug = category_news_list.getJSONObject(i).getString("slug");
                                news.setSlug(slug);
                            }
                            if(category_news_list.getJSONObject(i).isNull("featured_image") == false){
                                String featured_image = category_news_list.getJSONObject(i).getString("featured_image");
                                news.setImage(featured_image);
                            }
                            if(category_news_list.getJSONObject(i).isNull("short_description") == false){
                                String short_description = category_news_list.getJSONObject(i).getString("short_description");
                                news.setShort_description(short_description);
                            }
                            if(category_news_list.getJSONObject(i).isNull("approved_date") == false){
                                String approved_date = category_news_list.getJSONObject(i).getString("approved_date");
                                news.setApproved_date(approved_date);
                            }
                            if(category_news_list.getJSONObject(i).isNull("username") == false){
                                String username = category_news_list.getJSONObject(i).getString("username");
                                news.setJounalist_name(username);
                            }
                            if(category_news_list.getJSONObject(i).isNull("categoryname") == false){
                                String categoryname = category_news_list.getJSONObject(i).getString("categoryname");
                                news.setCategory(categoryname);
                            }
                            if(category_news_list.getJSONObject(i).isNull("categoryslug") == false){
                                String categoryslug = category_news_list.getJSONObject(i).getString("categoryslug");
                                news.setCategoryslug(categoryslug);
                            }
                            categoryNews.add(news);
                        }
                    }

                    if(res.isNull("TOP_VIDEO") == false){
                        mTopVideo = new News(Parcel.obtain());
                        if(res.getJSONObject("TOP_VIDEO").isNull("file_path") == false){
                            String file_path = res.getJSONObject("TOP_VIDEO").getString("file_path");
                            mTopVideo.setFile_path(file_path);
                        }
                        if(res.getJSONObject("TOP_VIDEO").isNull("name") == false){
                            String name = res.getJSONObject("TOP_VIDEO").getString("name");
                            mTopVideo.setName(name);
                        }
                        if(res.getJSONObject("TOP_VIDEO").isNull("slug") == false){
                            String slug = res.getJSONObject("TOP_VIDEO").getString("slug");
                            mTopVideo.setSlug(slug);
                        }
                        if(res.getJSONObject("TOP_VIDEO").isNull("is_video") == false){
                            String is_video = res.getJSONObject("TOP_VIDEO").getString("is_video");
                            mTopVideo.setIs_video(is_video);
                        }
                    }
                    if(res.isNull("VIRAL_VIDEO") == false){
                        mViralVideo = new News(Parcel.obtain());
                        if(res.getJSONObject("VIRAL_VIDEO").isNull("file_path") == false){
                            String file_path = res.getJSONObject("VIRAL_VIDEO").getString("file_path");
                            mViralVideo.setFile_path(file_path);
                        }
                        if(res.getJSONObject("VIRAL_VIDEO").isNull("name") == false){
                            String name = res.getJSONObject("VIRAL_VIDEO").getString("name");
                            mViralVideo.setName(name);
                        }
                        if(res.getJSONObject("VIRAL_VIDEO").isNull("slug") == false){
                            String slug = res.getJSONObject("VIRAL_VIDEO").getString("slug");
                            mViralVideo.setSlug(slug);
                        }
                        if(res.getJSONObject("VIRAL_VIDEO").isNull("is_video") == false){
                            String is_video = res.getJSONObject("VIRAL_VIDEO").getString("is_video");
                            mViralVideo.setIs_video(is_video);
                        }
                    }
                    //ODISHA_PLUS_NEWS
                    if(res.isNull("ODISHA_PLUS_NEWS") == false){
                        JSONArray odisha_plus = res.getJSONArray("ODISHA_PLUS_NEWS");
                        for(int i = 0; i < odisha_plus.length(); i++){
                            if(odisha_plus_news == null){
                                odisha_plus_news = new News(Parcel.obtain());
                            }
                            News news = new News(Parcel.obtain());
                            if(odisha_plus.getJSONObject(i).isNull("id") == false){
                                String id = odisha_plus.getJSONObject(i).getString("id");
                                news.setId(id);
                            }
                            if(odisha_plus.getJSONObject(i).isNull("name") == false){
                                String name = odisha_plus.getJSONObject(i).getString("name");
                                news.setName(name);
                            }
                            if(odisha_plus.getJSONObject(i).isNull("slug") == false){
                                String slug = odisha_plus.getJSONObject(i).getString("slug");
                                news.setSlug(slug);
                            }
                            if(odisha_plus.getJSONObject(i).isNull("featured_image") == false){
                                String featured_image = odisha_plus.getJSONObject(i).getString("featured_image");
                                news.setImage(featured_image);
                            }
                            if(odisha_plus.getJSONObject(i).isNull("short_description") == false){
                                String short_description = odisha_plus.getJSONObject(i).getString("short_description");
                                news.setShort_description(short_description);
                            }
                            if(odisha_plus.getJSONObject(i).isNull("approved_date") == false){
                                String approved_date = odisha_plus.getJSONObject(i).getString("approved_date");
                                news.setApproved_date(approved_date);
                            }
                            if(odisha_plus.getJSONObject(i).isNull("journalist_name") == false){
                                String journalist_name = odisha_plus.getJSONObject(i).getString("journalist_name");
                                news.setJounalist_name(journalist_name);
                            }
                            if(odisha_plus.getJSONObject(i).isNull("categoryname") == false){
                                String categoryname = odisha_plus.getJSONObject(i).getString("categoryname");
                                news.setCategory(categoryname);
                            }
                            if(odisha_plus.getJSONObject(i).isNull("categoryslug") == false){
                                String categoryslug = odisha_plus.getJSONObject(i).getString("categoryslug");
                                news.setCategoryslug(categoryslug);
                            }
                            if(odisha_plus.getJSONObject(i).isNull("is_video") == false){
                                String is_video = odisha_plus.getJSONObject(i).getString("is_video");
                                news.setIs_video(is_video);
                            }
                            if(odisha_plus.getJSONObject(i).isNull("is_image") == false){
                                String is_image = odisha_plus.getJSONObject(i).getString("is_image");
                                news.setIs_image(is_image);
                            }
                            if(odisha_plus.getJSONObject(i).isNull("file_path") == false){
                                String file_path = odisha_plus.getJSONObject(i).getString("file_path");
                                news.setFile_path(file_path);
                            }
                            odisha_plus_news = news;
                        }
                    }
                    isLatestData = true;
                    return true;
                } catch(SocketTimeoutException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                    return false;
                } catch(ConnectException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                    return false;
                } catch(MalformedURLException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                    return false;
                } catch (IOException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                    return false;
                } catch(Exception exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                    return false;

                    //to be done
                }
            }
        }.execute();
    }

    private void loadBreakingNews(){
        if(Util.getNetworkConnectivityStatus(HomeActivity.this) == false){
            Util.showDialogToShutdownApp(HomeActivity.this);
            return;
        }
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //pBar.setVisibility(View.VISIBLE);
                if(breaking_news == null){
                    breaking_news = new BreakingNews(Parcel.obtain());
                }
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //pBar.setVisibility(View.GONE);
                mIsBreakingNewsLoaded = true;
                if(mIsCategoriesLoaded
                        && mIsBreakingNewsLoaded
                        && mIsContentLoaded
                        && mIsContentDisplayed == false) {
                    if(mSwipeRefreshLayout != null){
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    if(isAudioDone) {
                        anim.start();
                    }
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                InputStream in = null;
                int resCode = -1;

                try {
                    String link = null;
                    if(lang==null || lang.contentEquals("English")) {
                        link=Config.API_BASE_URL + Config.BREAKING_NEWS_API+Config.EN_CONENT;

                    }
                    else{
                        link=Config.API_BASE_URL + Config.BREAKING_NEWS_API+Config.EN_CONENT;

                    }
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    //conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setAllowUserInteraction(false);
                    //conn.setInstanceFollowRedirects(true);
                    conn.setRequestMethod("GET");
                    conn.connect();

                    resCode = conn.getResponseCode();
                    if (resCode == HttpURLConnection.HTTP_OK) {
                        in = conn.getInputStream();
                    }
                    if (in == null) {
                        return null;
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    String response = "", data = "";

                    while ((data = reader.readLine()) != null) {
                        response += data + "\n";
                    }

                    Log.i(TAG, "Response : " + response);

                    /*
                    * {
                        "BREAKINGNEWS": [
                            {
                            "id": "1",
                            "title": "A look at the funding crisis in Egypt's health service, from cost of medication to organ sales."
                            }
                        ],
                        "NEWSDETAILS": "Packed from end to end with stunning action and liberal doses of humour and emotion, the film presses. ** A look at the funding crisis in Egypt's health service, from cost of medication to organ sales and \"patients for hire\". ** UN estimate says at least 430 people killed in the past year after President Nkurinziza launched election campaign...\r\n\r\n\r\n\r\n ** Kadyrov has erased all traces of the war. Even as we were filming, he decided to go even further. ** Riot police fire tear gas at demonstrators protesting proposed labour reforms that they claim threaten workers' rights. ** Israeli Prime Minister Netanyahu says the best way to resolve the conflict is through direct, bilateral negotiations. ** NEW DELHI: Even if oil prices move a little higher, they will not be negative for India but \"exceedingly high\". ** North Korea has sentenced a Korean American man to 10 years' hard labour for subversion, China's Xinhua news agency ** Syria war: UN humanitarian chief laments situation in Syria, where people are facing \"appalling\" desolation, hunger and starvation. ** BERHAMPUR: For 500 families of Sirtiguda village under Kanjamendi-Nuagaon block of Kandhamal district."
                        }
                    * */
                    if(response != null && response.trim().length() > 0){
                        JSONObject obj = new JSONObject(response);
                        List<BNews> breakingNewsList = new ArrayList<BNews>();
                        if(obj.isNull("BREAKINGNEWS") == false){
                            JSONArray array = obj.getJSONArray("BREAKINGNEWS");
                            if(array != null && array.length() > 0){
                                for(int i = 0; i < array.length(); i++) {
                                    BNews bNews = new BNews(Parcel.obtain());
                                    if (array.getJSONObject(i).isNull("id") == false) {
                                        String id = array.getJSONObject(i).getString("id");
                                        bNews.setId(id);
                                    }
                                    if (array.getJSONObject(i).isNull("title") == false) {
                                        String name = array.getJSONObject(i).getString("title");
                                        bNews.setTitle(name);
                                    }
                                    breakingNewsList.add(bNews);
                                }
                            }
                        }
                        List<BNews> newsDetailsList = new ArrayList<BNews>();
                        if(obj.isNull("NEWSDETAILS") == false){
                            JSONArray array = obj.getJSONArray("NEWSDETAILS");
                            if(array != null && array.length() > 0){
                                for(int i = 0; i < array.length(); i++) {
                                    BNews bNews = new BNews(Parcel.obtain());
                                    if (array.getJSONObject(i).isNull("id") == false) {
                                        String id = array.getJSONObject(i).getString("id");
                                        bNews.setId(id);
                                    }
                                    if (array.getJSONObject(i).isNull("title") == false) {
                                        String name = array.getJSONObject(i).getString("title");
                                        bNews.setTitle(name);
                                    }
                                    newsDetailsList.add(bNews);
                                }
                            }
                        }
                        breaking_news.setbNewsList(breakingNewsList);
                        breaking_news.setNewsdetails(newsDetailsList);
                    }


                    return null;
                } catch(SocketTimeoutException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch(ConnectException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch(MalformedURLException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch (IOException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch(Exception exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                }
                return null;
            }
        }.execute();
    }

    private boolean isAudioDone = false;

    @SuppressLint("StaticFieldLeak")
    private void loadDrawerMenuItems(){
        if(Util.getNetworkConnectivityStatus(HomeActivity.this) == false){
            Util.showDialogToShutdownApp(HomeActivity.this);
            return;
        }
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //pBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //pBar.setVisibility(View.GONE);
                mIsCategoriesLoaded = true;
                if(mIsCategoriesLoaded
                        && mIsBreakingNewsLoaded
                        && mIsContentLoaded
                        && mIsContentDisplayed == false) {
                    if(mSwipeRefreshLayout != null){
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    if(isAudioDone) {
                        anim.start();
                    }
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                InputStream in = null;
                int resCode = -1;

                try {
                    String link = null;
                    if(lang==null || lang.contentEquals("English")) {
                        link=Config.API_BASE_URL + Config.NAVIGATION_DRAWER_CATEGORIES_API+Config.EN_CONENT;

                    }
                    else{
                        link=Config.API_BASE_URL + Config.NAVIGATION_DRAWER_CATEGORIES_API+Config.OD_CONENT;

                    }
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //conn.setReadTimeout(10000);
                    //conn.setConnectTimeout(15000);
                    //conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setAllowUserInteraction(false);
                    //conn.setInstanceFollowRedirects(true);
                    conn.setRequestMethod("GET");
                    conn.connect();

                    resCode = conn.getResponseCode();
                    if (resCode == HttpURLConnection.HTTP_OK) {
                        in = conn.getInputStream();
                    }
                    if (in == null) {
                        return null;
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    String response = "", data = "";

                    while ((data = reader.readLine()) != null) {
                        response += data + "\n";
                    }

                    Log.i(TAG, "Response : " + response);

                    /*
                    * [
                          {
                            "id": "1",
                            "name": "World",
                            "slug": "world"
                          },
                          {
                            "id": "2",
                            "name": "City",
                            "slug": "city"
                          }
                      ]
                    * */

                    if(response != null && response.length() > 0) {
                        JSONArray resObj = new JSONArray(response);
                        if(resObj != null && resObj.length() > 0){
                            categoryList.clear();
                            for(int i = 0; i < resObj.length(); i++){
                                Category category = new Category(Parcel.obtain());
                                String id = resObj.getJSONObject(i).getString("id");
                                String name = resObj.getJSONObject(i).getString("name");
                                String slug = resObj.getJSONObject(i).getString("slug");

                                category.setName(name);
                                category.setId(id);
                                category.setSlug(slug);
                                categoryList.add(category);
                            }
                        }
                    }

                    return null;
                } catch(SocketTimeoutException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch(ConnectException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch(MalformedURLException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch (IOException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    private MediaPlayer mediaPlayer= null;
    private void playAudio(){
        mediaPlayer = MediaPlayer.create(this,R.raw.winxp);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //loadAllData();
                isAudioDone = true;
                if(mIsCategoriesLoaded
                        && mIsBreakingNewsLoaded
                        && mIsContentLoaded
                        && mIsContentDisplayed == false) {
                    if(mSwipeRefreshLayout != null){
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    anim.start();
                }
            }
        });
    }

    private void stopVideo(){
        if(mediaPlayer != null
                && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }

    private void loadAllData(){
        mIsContentLoaded = false;
        mIsCategoriesLoaded = false;
        mIsContentDisplayed = false;
        mIsBreakingNewsLoaded = false;

//        int is_resume = getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 1).getInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 0);
//        if(is_resume == 1){
//           mSwipeRefreshLayout.setVisibility(View.VISIBLE);
//        }

        loadNewsData(false);
      //  loadBreakingNews();
        //loadDrawerMenuItems();
        //loadConferenceNews(false);
    }
    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(com.lipl.ommcom.Notification.Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(com.lipl.ommcom.Notification.Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_about_us:
                startActivity(new Intent());
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id < categoryList.size()){
            for(int i = 0; i < categoryList.size(); i++){
                if (id == i) {
                    getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                    Intent intent = new Intent(HomeActivity.this, CategoryNewsListActivity.class);
                    intent.putExtra("slug", categoryList.get(i).getSlug());
                    startActivity(intent);
                    return true;
                }
            }
        }

        /*
        *
        * menu.add(Menu.NONE, xx + 1, xx + 1, "Feedback");
        menu.add(Menu.NONE, xx + 2, xx + 2, "Odisha");
        menu.add(Menu.NONE, xx + 3, xx + 3, "Nation");
        menu.add(Menu.NONE, xx + 4, xx + 4, "World");
        menu.add(Menu.NONE, xx + 5, xx + 5, "Sports");
        menu.add(Menu.NONE, xx + 6, xx + 6, "Business");
        menu.add(Menu.NONE, xx + 7, xx + 7, "Entertainment");
        menu.add(Menu.NONE, xx + 8, xx + 8, "Science & Tech");
        menu.add(Menu.NONE, xx + 9, xx + 9, "Videos");
        menu.add(Menu.NONE, xx + 10, xx + 10, "Language");
        * */


        if(categoryList != null){
            if(id == categoryList.size()){
                //About Us
                startActivity(new Intent(HomeActivity.this, AboutUsActivity.class));
            }
            if(id == categoryList.size() + 1){
                //Feedback
                startActivity(new Intent(HomeActivity.this, FeedbackActivity.class));
            }
            if(id == categoryList.size() + 2){
                //odisha plus
                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                Intent intent = new Intent(HomeActivity.this, CategoryNewsListActivity.class);
                intent.putExtra("slug", "odisha-news");
                startActivity(intent);
            }

            if(id == categoryList.size() + 3){
                //nation
                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                Intent intent = new Intent(HomeActivity.this, CategoryNewsListActivity.class);
                intent.putExtra("slug", "india-news");
                startActivity(intent);
            }

            if(id == categoryList.size() + 4){
                //world
                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                Intent intent = new Intent(HomeActivity.this, CategoryNewsListActivity.class);
                intent.putExtra("slug", "world-news");
                startActivity(intent);
                 }

            if(id == categoryList.size() + 5){
                //sports
                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                Intent intent = new Intent(HomeActivity.this, CategoryNewsListActivity.class);
                intent.putExtra("slug", "sports-news");
                startActivity(intent);
            }if(id == categoryList.size() + 6){
                //business
                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                Intent intent = new Intent(HomeActivity.this, CategoryNewsListActivity.class);
                intent.putExtra("slug", "business-news");
                startActivity(intent);

            }if(id == categoryList.size() + 7){
                //entertainment
                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                Intent intent = new Intent(HomeActivity.this, CategoryNewsListActivity.class);
                intent.putExtra("slug", "entertainment");
                startActivity(intent);

            }if(id == categoryList.size() + 8){
                //science-tech
                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                Intent intent = new Intent(HomeActivity.this, CategoryNewsListActivity.class);
                intent.putExtra("slug", "science-tech");
                startActivity(intent);
            }if(id == categoryList.size() + 9){
                //videos
                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                Intent intent = new Intent(HomeActivity.this, CategoryNewsListActivity.class);
                intent.putExtra("news", news2);
                intent.putExtra("isTopVideo", false);
                intent.putExtra("isViralVideo", true);
                startActivity(intent);            }

                if(id == categoryList.size() + 10){
                //language
                startActivity(new Intent(HomeActivity.this, LanguageActivity.class));
            }
        } else{
            if(id == 111){
                //About Us
                startActivity(new Intent(HomeActivity.this, AboutUsActivity.class));
            }
            if(id == 112){
                //feedback
                startActivity(new Intent(HomeActivity.this, FeedbackActivity.class));

            }

            if(id == 113){
                //odisha plus
                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                Intent intent = new Intent(HomeActivity.this, CategoryNewsListActivity.class);
                intent.putExtra("slug", "odisha-news");
                startActivity(intent);
            }
            if(id == 114){
                //nation
                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                Intent intent = new Intent(HomeActivity.this, CategoryNewsListActivity.class);
                intent.putExtra("slug", "india-news");
                startActivity(intent);
            }
            if(id == 115){
                //world
                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                Intent intent = new Intent(HomeActivity.this, CategoryNewsListActivity.class);
                intent.putExtra("slug", "world-news");
                startActivity(intent);

            }if(id == 116){
                //sports
                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                Intent intent = new Intent(HomeActivity.this, CategoryNewsListActivity.class);
                intent.putExtra("slug", "sports-news");
                startActivity(intent);
            }if(id == 117){
                //business

                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                Intent intent = new Intent(HomeActivity.this, CategoryNewsListActivity.class);
                intent.putExtra("slug", "business-news");
                startActivity(intent);
            }if(id == 118){
                //entertainment
                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                Intent intent = new Intent(HomeActivity.this, CategoryNewsListActivity.class);
                intent.putExtra("slug", "entertainment");
                startActivity(intent);

            }if(id == 119){
                //science-tech
                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                Intent intent = new Intent(HomeActivity.this, CategoryNewsListActivity.class);
                intent.putExtra("slug", "science-tech");
                startActivity(intent);
            }if(id == 1110){
                //video
                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                Intent intent = new Intent(HomeActivity.this, CategoryNewsListActivity.class);
                intent.putExtra("news", news2);
                intent.putExtra("isTopVideo", false);
                intent.putExtra("isViralVideo", true);
                startActivity(intent);
            }
                if(id == 1111){
                //language
                startActivity(new Intent(HomeActivity.this, LanguageActivity.class));
            }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isLatestData = false;
    public void setButtonLoc(PathPoint newLoc) {
        layoutLogoImg.setTranslationX(newLoc.mX);
        layoutLogoImg.setTranslationY(newLoc.mY);
    }

    private void setTextAppearance(TextView textView, Context context, int resId) {

        if (Build.VERSION.SDK_INT < 23) {
            textView.setTextAppearance(context, resId);
        } else {
            textView.setTextAppearance(resId);
        }
    }

    /**
     * Body Content of Home Screen to be displayed
     * */
    private void displayContent() {
        mIsContentDisplayed = true;

        ImageView search = (ImageView) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        CustomTextView tvBreakingNewsNo = (CustomTextView) findViewById(R.id.tvBreakingNewsNo);
        tvBreakingNewsNo.setVisibility(View.GONE);

        Menu menu = navigationView.getMenu();
        if(menu != null){
            menu.clear();
        }
        try {
            for (int i = 0; i < categoryList.size(); i++) {
                menu.add(Menu.NONE, i, i, categoryList.get(i).getName());
            }
        } catch(Exception exception){
            Log.e(TAG, "displayContent()", exception);
        }

        int xx = 111;
        if(categoryNews != null){
            xx = categoryList.size();
        }
        if(lang==null || lang.contentEquals("English")) {
            menu.add(Menu.NONE, xx, xx, "About Us");
            menu.add(Menu.NONE, xx + 1, xx + 1, "Feedback");
            menu.add(Menu.NONE, xx + 2, xx + 2, "Odisha");
            menu.add(Menu.NONE, xx + 3, xx + 3, "Nation");
            menu.add(Menu.NONE, xx + 4, xx + 4, "World");
            menu.add(Menu.NONE, xx + 5, xx + 5, "Sports");
            menu.add(Menu.NONE, xx + 6, xx + 6, "Business");
            menu.add(Menu.NONE, xx + 7, xx + 7, "Entertainment");
            menu.add(Menu.NONE, xx + 8, xx + 8, "Science & Tech");
            menu.add(Menu.NONE, xx + 9, xx + 9, "Videos");
            menu.add(Menu.NONE, xx + 10, xx + 10, "Language");
        }
        else{
            menu.add(Menu.NONE, xx, xx, "ଆମ ବିଷୟରେ");
            menu.add(Menu.NONE, xx + 1, xx + 1, "ମତାମତ");
            menu.add(Menu.NONE, xx + 2, xx + 2, "ଓଡିଶା");
            menu.add(Menu.NONE, xx + 3, xx + 3, "ଦେଶ");
            menu.add(Menu.NONE, xx + 4, xx + 4, "ବିଦେଶ");
            menu.add(Menu.NONE, xx + 5, xx + 5, "କ୍ରୀଡା");
            menu.add(Menu.NONE, xx + 6, xx + 6, "ବାଣିଜ୍ୟ ");
            menu.add(Menu.NONE, xx + 7, xx + 7, "ମନୋରଞ୍ଜନ");
            menu.add(Menu.NONE, xx + 8, xx + 8, "ବିଜ୍ଞାନ ଏବଂ କୌଶଳ");
            menu.add(Menu.NONE, xx + 9, xx + 9, "ଭିଡିଓ");
            menu.add(Menu.NONE, xx + 10, xx + 10, "ଭାଷା");
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.app_logo_color_blue, R.color.app_logo_color_maroon);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
             @Override
             public void onRefresh() {
                 Boolean pos1obj1=false,pos1obj2=false,pos1arr1=false,pos1arr2=false;
                 Boolean pos2obj1=false,pos2obj2=false,pos2arr1=false,pos2arr2=false;
                 Boolean pos3obj1=false,pos3obj2=false,pos3arr1=false,pos3arr2=false;
                 Boolean pos4obj1=false,pos4obj2=false,pos4arr1=false,pos4arr2=false;
                 Boolean pos5obj1=false,pos5obj2=false,pos5arr1=false,pos5arr2=false;
                 Boolean pos6obj1=false,pos6obj2=false,pos6arr1=false,pos6arr2=false;
                 getOdishanews();
                 getAdvertisements();
                 loadNewsData(true);
             }
         });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                startActivity(new Intent(HomeActivity.this, PollingActivity.class));
            }
        });

        RelativeLayout layoutContent = (RelativeLayout) findViewById(R.id.layoutContentFirst);
        layoutContent.setVisibility(View.VISIBLE);

        LinearLayout layoutBody = (LinearLayout) findViewById(R.id.layoutBody);
        if(isLatestData){
            layoutBody.removeAllViews();
        }
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.stub)//ic_stub
                .showImageForEmptyUri(R.drawable.empty)//ic_empty
                .showImageOnFail(R.drawable.error)//ic_error
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .displayer(new SimpleBitmapDisplayer())
                .build();

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(HomeActivity.this));

        RelativeLayout layoutTopAdvertisement = null;
        RelativeLayout layoutBottomAdvertisement = null;
        RelativeLayout layoutMostBottomAdvertisement = null;
        if(advertisementList != null && advertisementList.size() > 0) {
            for(int i = 0; i < advertisementList.size(); i++) {
                Advertisement advertisement = advertisementList.get(i);
                if(advertisement != null && advertisement.getSection() != null && advertisement.getSection().length() > 0
                        && advertisement.getSection().equalsIgnoreCase(ADVERTISEMENT_HEADER+"")){
                    //Top Advertisement
                    layoutTopAdvertisement = new RelativeLayout(HomeActivity.this);
                    int height_layout_top_advertisement = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_HEADER_ADVERTISEMENT);
                    //RelativeLayout.LayoutParams layout_params_top_advertisement = new RelativeLayout.LayoutParams
                            //(RelativeLayout.LayoutParams.MATCH_PARENT, height_layout_top_advertisement);
                    RelativeLayout.LayoutParams layout_params_top_advertisement = new RelativeLayout.LayoutParams
                            (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutTopAdvertisement.setLayoutParams(layout_params_top_advertisement);
                    ImageView img_header_adv = new ImageView(HomeActivity.this);
                    LinearLayout.LayoutParams layout_params_header_adv_img = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                    img_header_adv.setLayoutParams(layout_params_header_adv_img);
                    img_header_adv.setAdjustViewBounds(true);
                    img_header_adv.setScaleType(ImageView.ScaleType.FIT_XY);
                    layoutTopAdvertisement.addView(img_header_adv);

                    String adv_header_image = "";
                    if(advertisement != null && advertisement.getFile_path() != null
                            && advertisement.getFile_path().trim().length() > 0){
                        adv_header_image = advertisement.getFile_path().trim();
                    }
                    imageLoader.displayImage(Config.IMAGE_DOWNLOAD_BASE_URL
                            + Config.FOLDER_ADVERTISEMENT + Config.FOLDER_VIDEO + "/" +
                                    adv_header_image , img_header_adv, options);

                    final String url = advertisement.getUrl_link();
                    if(url != null && url.trim().length() > 0){
                        layoutTopAdvertisement.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String _url = url;
                                if (!url.startsWith("http://") && !url.startsWith("https://"))
                                    _url = "http://" + url;
                                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(_url));
                                startActivity(intent);
                            }
                        });
                    }
                } else if(advertisement != null && advertisement.getSection() != null && advertisement.getSection().length() > 0
                            && advertisement.getSection().equalsIgnoreCase(ADVERTISEMENT_MIDDLE+"")){
                    layoutBottomAdvertisement = new RelativeLayout(HomeActivity.this);
                    int height_layout_footer_adv = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_FOOTER_ADVERTISEMENT);
                    //RelativeLayout.LayoutParams layout_params_footer_adv = new RelativeLayout.LayoutParams
                      //      (RelativeLayout.LayoutParams.MATCH_PARENT, height_layout_footer_adv);
                    RelativeLayout.LayoutParams layout_params_footer_adv = new RelativeLayout.LayoutParams
                            (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutBottomAdvertisement.setLayoutParams(layout_params_footer_adv);
                    ImageView imgFooterAdv = new ImageView(HomeActivity.this);
                    LinearLayout.LayoutParams layout_params_footer_adv_img = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                    imgFooterAdv.setLayoutParams(layout_params_footer_adv_img);
                    imgFooterAdv.setAdjustViewBounds(true);
                    imgFooterAdv.setScaleType(ImageView.ScaleType.FIT_XY);
                    layoutBottomAdvertisement.addView(imgFooterAdv);

                    String adv_footer_image = advertisement.getFile_path().trim();
                    imageLoader.displayImage(Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_ADVERTISEMENT
                            + Config.FOLDER_VIDEO + "/" +
                                    adv_footer_image//"http://timesofindia.indiatimes.com/" +
                            //"thumb/msid-51900429,width-400,resizemode-4/51900429.jpg"
                            , imgFooterAdv, options);
                    final String url = advertisement.getUrl_link();
                    if(url != null && url.trim().length() > 0){
                        layoutBottomAdvertisement.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String _url = url;
                                if (!url.startsWith("http://") && !url.startsWith("https://"))
                                    _url = "http://" + url;
                                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(_url));
                                startActivity(intent);
                            }
                        });
                    }
                } else if(advertisement != null && advertisement.getSection() != null && advertisement.getSection().length() > 0
                        && advertisement.getSection().equalsIgnoreCase(ADVERTISEMENT_FOOTER+"")){
                    layoutMostBottomAdvertisement = new RelativeLayout(HomeActivity.this);
                    int height_layout_footer_adv = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_FOOTER_ADVERTISEMENT);
                    //RelativeLayout.LayoutParams layout_params_footer_adv = new RelativeLayout.LayoutParams
                    //      (RelativeLayout.LayoutParams.MATCH_PARENT, height_layout_footer_adv);
                    RelativeLayout.LayoutParams layout_params_footer_adv = new RelativeLayout.LayoutParams
                            (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutMostBottomAdvertisement.setLayoutParams(layout_params_footer_adv);
                    ImageView imgFooterAdv = new ImageView(HomeActivity.this);
                    LinearLayout.LayoutParams layout_params_footer_adv_img = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                    imgFooterAdv.setLayoutParams(layout_params_footer_adv_img);
                    imgFooterAdv.setAdjustViewBounds(true);
                    imgFooterAdv.setScaleType(ImageView.ScaleType.FIT_XY);
                    layoutMostBottomAdvertisement.addView(imgFooterAdv);

                    String adv_footer_image = advertisement.getFile_path().trim();
                    imageLoader.displayImage(Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_ADVERTISEMENT
                                    + Config.FOLDER_VIDEO + "/" +
                                    adv_footer_image//"http://timesofindia.indiatimes.com/" +
                            //"thumb/msid-51900429,width-400,resizemode-4/51900429.jpg"
                            , imgFooterAdv, options);
                    final String url = advertisement.getUrl_link();
                    if(url != null && url.trim().length() > 0){
                        layoutMostBottomAdvertisement.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String _url = url;
                                if (!url.startsWith("http://") && !url.startsWith("https://"))
                                    _url = "http://" + url;
                                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(_url));
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
        }

        //Featured News
        RelativeLayout layoutFeaturedNews = new RelativeLayout(HomeActivity.this);
        int height_layout_featured_news_section = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_TOP_FEATURED_NEWS);
        RelativeLayout.LayoutParams layout_params_featured_news = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT, height_layout_featured_news_section);
        //RelativeLayout.LayoutParams layout_params_featured_news = new RelativeLayout.LayoutParams
          //      (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutFeaturedNews.setLayoutParams(layout_params_featured_news);
        View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.layout_featured_news, null);
        layoutFeaturedNews.addView(view);
        layoutBody.addView(layoutFeaturedNews);

        LinearLayout layoutTopNews = new LinearLayout(HomeActivity.this);
        int height_layout_news = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_TOP_NEWS);
        LinearLayout.LayoutParams layout_params_news = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, height_layout_news);
//        LinearLayout.LayoutParams layout_params_news = new LinearLayout.LayoutParams
//        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutTopNews.setLayoutParams(layout_params_news);
        View view_top_news = LayoutInflater.from(HomeActivity.this).inflate(R.layout.layout_top_news, null);
        layoutTopNews.addView(view_top_news);
        CustomTextView tvTopNewsTitle = (CustomTextView) view_top_news.findViewById(R.id.tvTopNewsTitle);
        if(lang==null || lang.contentEquals("English")){
            tvTopNewsTitle.setText("TOP 5 NEWS NOW");

        }
        else {
            tvTopNewsTitle.setText("୫ଟି ମୁଖ୍ୟ ଖବର");

        }
//        int text_size_in_pixel = getResources().getInteger(R.integer.news_title_size);
//        tvTopNewsTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, Config.get_text_size(HomeActivity.this, text_size_in_pixel));
        layoutBody.addView(layoutTopNews);

        // removed from here

 /*
        *
        * ADVERTISING SECTION (ampos0)
        * */

        // for single object advertisement (pos0)

        RelativeLayout pos0object = new RelativeLayout(HomeActivity.this);
        int height_pos0object = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_HEADER_ADVERTISEMENT);
        RelativeLayout.LayoutParams layoutParams_pos0object= new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT,height_pos0object);
        pos0object.setLayoutParams(layoutParams_pos0object);
        View view_p0ob = LayoutInflater.from(HomeActivity.this).inflate(R.layout.lay_posobj0, null);
        ImageView iv_pos1obj1=(ImageView)view_p0ob.findViewById(R.id.iv_pos1obj1);
        pos0object.addView(view_p0ob);
        // HERE IMAGE VIEW WILL BE SET
        layoutBody.addView(pos0object);
         AnimationUtil.slideInFromRight(pos0object, 500, 700);
         pos0object.setVisibility(View.GONE);
         if(pos1obj1==true){
             pos0object.setVisibility(View.VISIBLE);
             imageLoader.displayImage( pos1_obj1_image, iv_pos1obj1, options);
         }
         else{
             pos0object.setVisibility(View.GONE);

         }
        // for single array advertisement (pos0)
        LinearLayout pos0array = new LinearLayout(HomeActivity.this);
        int height_pos0array = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_TOP_NEWS);
        LinearLayout.LayoutParams layoutParams_pos0array = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, height_pos0array);
        pos0array.setLayoutParams(layoutParams_pos0array);
        View view_p0array = LayoutInflater.from(HomeActivity.this).inflate(R.layout.lay_posarray0, null);
        ImageView iv_addim01=(ImageView)view_p0array.findViewById(R.id.adim01);
        ImageView iv_addim02=(ImageView)view_p0array.findViewById(R.id.addim02);
        ImageView iv_addim03=(ImageView)view_p0array.findViewById(R.id.addim03);
        pos0array.addView(view_p0array);
        layoutBody.addView(pos0array);
        AnimationUtil.slideInFromLeft(pos0array, 500, 700);
        pos0array.setVisibility(View.GONE);
        if(pos1arr1==true){
            pos0array.setVisibility(View.VISIBLE);
            imageLoader.displayImage( pos1_arr1_image1, iv_addim01, options);
            imageLoader.displayImage( pos1_arr1_image2, iv_addim02, options);
            imageLoader.displayImage( pos1_arr1_image3, iv_addim03, options);

        }
        else {
            pos0array.setVisibility(View.GONE);
        }

        RelativeLayout pos0object2 = new RelativeLayout(HomeActivity.this);
        int height_pos0object2 = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_HEADER_ADVERTISEMENT);
        RelativeLayout.LayoutParams layoutParams_pos0object2= new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT,height_pos0object2);
        pos0object2.setLayoutParams(layoutParams_pos0object2);
        View view_p0ob2 = LayoutInflater.from(HomeActivity.this).inflate(R.layout.lay_posobj02, null);
        ImageView iv_pos1obj2=(ImageView)view_p0ob2.findViewById(R.id.iv_pos1obj2);
        pos0object2.addView(view_p0ob2);
        // HERE IMAGE VIEW WILL BE SET
        layoutBody.addView(pos0object2);
        AnimationUtil.slideInFromRight(pos0object2, 500, 700);
        pos0object2.setVisibility(View.GONE);
        if(pos1obj2==true){
            pos0object2.setVisibility(View.VISIBLE);
            imageLoader.displayImage( pos1_obj2_image, iv_pos1obj2, options);
        }
        else{
            pos0object2.setVisibility(View.GONE);

        }
        // for single array advertisement (pos0)
        LinearLayout pos0array2 = new LinearLayout(HomeActivity.this);
        int height_pos0array2 = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_TOP_NEWS);
        LinearLayout.LayoutParams layoutParams_pos0array2 = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, height_pos0array2);
        pos0array2.setLayoutParams(layoutParams_pos0array2);
        View view_p0array2 = LayoutInflater.from(HomeActivity.this).inflate(R.layout.lay_posarray02, null);
        ImageView iv_addim012=(ImageView)view_p0array.findViewById(R.id.adim012);
        ImageView iv_addim022=(ImageView)view_p0array.findViewById(R.id.addim022);
        ImageView iv_addim032=(ImageView)view_p0array.findViewById(R.id.addim032);
        pos0array2.addView(view_p0array2);
        layoutBody.addView(pos0array2);
        AnimationUtil.slideInFromLeft(pos0array2, 500, 700);
        pos0array2.setVisibility(View.GONE);
        if(pos1arr2==true){
            pos0array2.setVisibility(View.VISIBLE);
            imageLoader.displayImage( pos1_arr2_image1, iv_addim012, options);
            imageLoader.displayImage( pos1_arr2_image2, iv_addim022, options);
            imageLoader.displayImage( pos1_arr2_image3, iv_addim032, options);

        }
        else {
            pos0array2.setVisibility(View.GONE);
        }

 /*
        *
        * ADVERTISING SECTION END  (pos0)
        * */



 /* ODISHA NEWS SECTION*/

        RelativeLayout odnshead = new RelativeLayout(HomeActivity.this);
        int height_odnshd = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_HEADER_ADVERTISEMENT);
        RelativeLayout.LayoutParams layoutParams_odnshd= new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT,height_odnshd);
        odnshead.setLayoutParams(layoutParams_odnshd);
        View view_odnshd = LayoutInflater.from(HomeActivity.this).inflate(R.layout.lay_odnshd, null);
        TextView textView_start = (TextView) view_odnshd.findViewById(R.id.tv_odhd);
        if(lang==null || lang.contentEquals("English")) {
            textView_start.setText(R.string.odisha_news_en);
        }
        else{
            textView_start.setText(R.string.odisha_news_od);

        }

        odnshead.addView(view_odnshd);
        layoutBody.addView(odnshead);

        odnshead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,AllOdishaNewsList.class);
                startActivity(intent);
            }
        });
        //AnimationUtil.slideInFromRight(pos0object2, 500, 700);

        /*LinearLayout ln_odisha_news = new LinearLayout(HomeActivity.this);
        LinearLayout.LayoutParams layoutParams_odns = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ln_odisha_news.setLayoutParams(layoutParams_odns);
        View view_odns = LayoutInflater.from(HomeActivity.this).inflate(R.layout.lay_odns, null);
        odnshead.addView(view_odns);*/
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_TOP_FEATURED_NEWS));

        // layoutBody.addView(odnshead);


        relativeolder = new RelativeLayout[odisanewscount];

        for(int i = 0;i<odisanewscount;i++){
            RelativeLayout relativeLayout = new RelativeLayout(this);
            relativeLayout.setId(i);
            relativeLayout.setLayoutParams(params);
            //relativeLayout.setBackgroundColor(Color.parseColor("#AD1C39"));
            relativeolder[i] = relativeLayout;
            layoutBody.setOrientation(LinearLayout.VERTICAL);
            View view_im= LayoutInflater.from(HomeActivity.this).inflate(R.layout.gradient,null);
            layoutBody.addView(relativeLayout);

          //  final float scale = this.getResources().getDisplayMetrics().density;
           // int pixels = (int) (150 * scale + 0.5f);
            RelativeLayout.LayoutParams iv_params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT );
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(iv_params);
            imageView.setId(i);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageLoader.displayImage( arrayList_odishanews.get(i).getFeatured_image(), imageView, options);
            relativeLayout.addView(imageView);
            relativeLayout.addView(view_im);


            TextView textView = new TextView(this);
            textView.setId(i);
            textView.setLayoutParams(params);
            textView.setTextColor(Color.parseColor("#FFFFFF"));
            textView.setTypeface(null, Typeface.BOLD);
            textView.setTextSize(17);
            textView.setMaxLines(2);
            textView.setPadding(20,0,0,10);
            textView.setGravity(Gravity.BOTTOM);
            textView.setText(arrayList_odishanews.get(i).getName());
            relativeLayout.addView(textView);

            LinearLayout.LayoutParams tvtime_params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            tvtime_params.gravity=Gravity.BOTTOM;
            TextView tv_time = new TextView(this);
            tv_time.setId(i);
            tv_time.setTextColor(Color.parseColor("#FFFFFF"));
            tv_time.setMaxLines(1);
            tv_time.setLayoutParams(tvtime_params);
            tv_time.setText(Util.getTime(arrayList_odishanews.get(i).getApproved_date()));
            tv_time.setVisibility(View.GONE);

            relativeLayout.addView(tv_time);


            final int finalI = i;
            relativeolder[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    News featured_news_new = new News(Parcel.obtain());
                    featured_news_new.setSlug(arrayList_odishanews.get(finalI).getSlug());
                    getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                    Intent intent = new Intent(HomeActivity.this, NewsDetailsActivity.class);
                    intent.putExtra("news", featured_news_new);
                    startActivity(intent);
                }
            });

        }

        RelativeLayout odnsend = new RelativeLayout(HomeActivity.this);
        int height_odnsend = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_HEADER_ADVERTISEMENT);
        RelativeLayout.LayoutParams layoutParams_odnsend= new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT,height_odnsend);
        odnsend.setLayoutParams(layoutParams_odnsend);
        View view_odnsend = LayoutInflater.from(HomeActivity.this).inflate(R.layout.lay_odnsend, null);
        TextView textView = (TextView) view_odnsend.findViewById(R.id.tvendodishaTitle);
        if(lang==null || lang.contentEquals("English")) {
            textView.setText(R.string.more_odisha_en);
        }
        else{
            textView.setText(R.string.more_odisha_od);

        }

        odnsend.addView(view_odnsend);
        layoutBody.addView(odnsend);

        odnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,AllOdishaNewsList.class);
                startActivity(intent);
            }
        });


        /*
        * ODISHA NEWS SECTION ENDS
        * */

        if(odishanews==false){
            odnshead.setVisibility(View.GONE);
            odnsend.setVisibility(View.GONE);
        }
        else{
            odnshead.setVisibility(View.VISIBLE);
            odnsend.setVisibility(View.VISIBLE);
        }



        RelativeLayout layoutDebate = new RelativeLayout(HomeActivity.this);
        int height_layout_conference = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_TOP_FEATURED_NEWS1);
        //RelativeLayout.LayoutParams layout_params_conference = new RelativeLayout.LayoutParams
        //      (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams layout_params_conference = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT, height_layout_conference);
        layoutDebate.setLayoutParams(layout_params_conference);
        View view_debate = LayoutInflater.from(HomeActivity.this).inflate(R.layout.layout_conference, null);
        CustomTextView tvlivetitle = (CustomTextView) view_debate.findViewById(R.id.livetitle);
        ImageView imgPlay = (ImageView) view_debate.findViewById(R.id.imgPlay);
        if(conferenceNews != null
                && conferenceNews.getName() != null
                && conferenceNews.getName().trim().length() > 0){
            imgPlay.setVisibility(View.GONE);
            tvlivetitle.setVisibility(View.GONE);
        } else {
            imgPlay.setVisibility(View.VISIBLE);
            tvlivetitle.setVisibility(View.VISIBLE);
        }
        layoutDebate.addView(view_debate);
        if(lang==null || lang.contentEquals("English")){
            tvlivetitle.setText(R.string.live_stream_en);
        }
        else{
            tvlivetitle.setText(R.string.live_stream_od);

        }
        layoutBody.addView(layoutDebate);

        RelativeLayout layoutCitizenJournalist = new RelativeLayout(HomeActivity.this);
        int height_layout_citizen_journalist = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_TOP_FEATURED_NEWS);
        RelativeLayout.LayoutParams layout_params_citizen_journalist = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT, height_layout_citizen_journalist);
        //RelativeLayout.LayoutParams layout_params_citizen_journalist = new RelativeLayout.LayoutParams
        //      (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutCitizenJournalist.setLayoutParams(layout_params_citizen_journalist);
        View view_cj = LayoutInflater.from(HomeActivity.this).inflate(R.layout.layout_citizen_journalist, null);
        layoutCitizenJournalist.addView(view_cj);
        CustomTextView tvCJTitle = (CustomTextView) view_cj.findViewById(R.id.tvCJTitle);
        if(lang==null || lang.contentEquals("English")){
            tvCJTitle.setText(R.string.citizen_j_en);
        }
        else{
            tvCJTitle.setText(R.string.citizen_j_od);

        }
//        int text_size_in_pixel_cj = getResources().getInteger(R.integer.news_title_size);
//        tvCJTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, Config.get_text_size(HomeActivity.this, text_size_in_pixel_cj));
        layoutBody.addView(layoutCitizenJournalist);


        /*
        *
        * ADVERTISING SECTION (ampos1) but in response pos2
        * */

        // for single object advertisement (pos1)

        RelativeLayout pos1object = new RelativeLayout(HomeActivity.this);
        int height_pos1object = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_HEADER_ADVERTISEMENT);
        RelativeLayout.LayoutParams layoutParams_pos1object= new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT,height_pos1object);
        pos1object.setLayoutParams(layoutParams_pos1object);
        View view_p1ob = LayoutInflater.from(HomeActivity.this).inflate(R.layout.lay_posobj1, null);
        ImageView iv_pos11=(ImageView) view_p1ob.findViewById(R.id.iv_pos11);
        pos1object.addView(view_p1ob);
        layoutBody.addView(pos1object);
        pos1object.setVisibility(View.GONE);
        AnimationUtil.slideInFromRight(pos1object, 500, 700);
        if(pos2obj1==true){
            pos1object.setVisibility(View.VISIBLE);
            imageLoader.displayImage( pos2_obj1_image, iv_pos11, options);
        }
        else{
            pos1object.setVisibility(View.GONE);

        }
        // for single array advertisement (pos1)

        LinearLayout pos1array = new LinearLayout(HomeActivity.this);
        int height_pos1array = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_TOP_NEWS);
        LinearLayout.LayoutParams layoutParams_pos1array = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, height_pos1array);
        pos1array.setLayoutParams(layoutParams_pos1array);
        View view_p1array = LayoutInflater.from(HomeActivity.this).inflate(R.layout.lay_posarray1, null);
        ImageView adim11=(ImageView)view_p1array.findViewById(R.id.adim11);
        ImageView adim12=(ImageView)view_p1array.findViewById(R.id.addim12);
        ImageView adim13=(ImageView)view_p1array.findViewById(R.id.addim13);
        pos1array.addView(view_p1array);
        layoutBody.addView(pos1array);
        AnimationUtil.slideInFromRight(pos1array, 500, 700);
        pos1array.setVisibility(View.GONE);
        if(pos2arr1==true){
            pos1array.setVisibility(View.VISIBLE);
            imageLoader.displayImage( pos2_arr1_image1, adim11, options);
            imageLoader.displayImage( pos2_arr1_image2, adim12, options);
            imageLoader.displayImage( pos2_arr1_image3, adim13, options);
        }
        else{
            pos1array.setVisibility(View.GONE);

        }


 /*
        *
        * ADVERTISING SECTION END  (pos1)
        * */
        /*RelativeLayout layoutBreakingNews = new RelativeLayout(HomeActivity.this);
        int height_layout_breaking_news = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_BREAKING_NEWS);
        RelativeLayout.LayoutParams layout_params_breaking_news = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT, height_layout_breaking_news);
        layoutBreakingNews.setLayoutParams(layout_params_breaking_news);
        View view_breaking_news = LayoutInflater.from(HomeActivity.this).inflate(R.layout.layout_breaking_news, null);
        layoutBreakingNews.addView(view_breaking_news);
        layoutBody.addView(layoutBreakingNews);*/
        tvBreakingNews = (CustomTextView) findViewById(R.id.tvBreakingNews);

        if(layoutBottomAdvertisement != null) {
            layoutBody.addView(layoutBottomAdvertisement);
        }

        if(layoutTopAdvertisement != null){
            layoutBody.addView(layoutTopAdvertisement, 0);
        }

        if(layoutTopAdvertisement != null) {
            AnimationUtil.slideInFromLeft(layoutTopAdvertisement, 500, 500);
        }
        AnimationUtil.slideInFromRight(layoutFeaturedNews, 500, 700);
        AnimationUtil.slideInFromLeft(layoutTopNews, 500, 900);
        AnimationUtil.slideInFromRight(layoutDebate, 500, 1100);
        AnimationUtil.slideInFromLeft(layoutCitizenJournalist, 500, 1300);
        if(layoutBottomAdvertisement != null) {
            AnimationUtil.slideInFromRight(layoutBottomAdvertisement, 500, 1700);
        }

        String featured_image_file_name = Util.getImageFilePathForNews(featured_news, null);
        ImageView fn_display_image_view = (ImageView) findViewById(R.id.imgDisplayPicture);
        if(featured_news != null && featured_news.getIs_video() != null
                && featured_news.getIs_video().trim().equalsIgnoreCase("1")){
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.stub)//ic_stub
                    .showImageForEmptyUri(R.drawable.video_default)//ic_empty
                    .showImageOnFail(R.drawable.video_default)//ic_error
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.ARGB_8888)
                    .displayer(new SimpleBitmapDisplayer())
                    .build();
        }
        imageLoader.displayImage(featured_image_file_name//"http://cdn.ndtv.com/tech/new_chromecast_black.jpg"
                , fn_display_image_view, options);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.stub)//ic_stub
                .showImageForEmptyUri(R.drawable.empty)//ic_empty
                .showImageOnFail(R.drawable.error)//ic_error
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .displayer(new SimpleBitmapDisplayer())
                .build();

        String top_news_one_file_name = "";
        if(newsList != null && newsList.size() > 0
                && newsList.get(0) != null
                ){
            top_news_one_file_name = Util.getImageFilePathForTopNews(newsList.get(0));
        }
        ImageView img_top_news_one = (ImageView) findViewById(R.id.imgTopImg1);
        imageLoader.displayImage(top_news_one_file_name//"http://timesofindia.indiatimes.com/thumb/" +
                //"msid-51902387,width-400,resizemode-4/51902387.jpg"
                , img_top_news_one, options);

        String top_news_two_file_name = "";
        if(newsList != null && newsList.size() > 1
                && newsList.get(1) != null){
            top_news_two_file_name = Util.getImageFilePathForTopNews(newsList.get(1));
        }
        ImageView img_top_news_two = (ImageView) findViewById(R.id.imgTopImg2);
        imageLoader.displayImage(top_news_two_file_name//"http://timesofindia.indiatimes.com/" +
                //"thumb/msid-51900429,width-400,resizemode-4/51900429.jpg"
                , img_top_news_two, options);

        String top_news_three_file_name = "";
        if(newsList != null && newsList.size() > 2
                && newsList.get(2) != null){
            top_news_three_file_name = Util.getImageFilePathForTopNews(newsList.get(2));
        }
        ImageView img_top_news_three = (ImageView) findViewById(R.id.imgTopImg3);
        imageLoader.displayImage(top_news_three_file_name//"http://timesofindia.indiatimes.com/" +
                //"thumb/msid-51900429,width-400,resizemode-4/51900429.jpg"
                , img_top_news_three, options);

        String cj_image = "";
        if(citizenJournalistVideos != null
                && citizenJournalistVideos.getFile_path() != null
                && citizenJournalistVideos.getFile_path().length() > 0){
            cj_image = citizenJournalistVideos.getFile_path().trim();
        }
        ImageView citizenJournalistImage = (ImageView) findViewById(R.id.citizenJournalistImage);
        imageLoader.displayImage(Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_CITIZEN_JOURNALIST + Config.getFolderForCj() +
                        cj_image//"http://timesofindia.indiatimes.com/" +
                //"thumb/msid-51900429,width-400,resizemode-4/51900429.jpg"
                , citizenJournalistImage, options);

        String postedBy = featured_news.getJounalist_name();
        CustomTextView tvNewsPostedBy = (CustomTextView) findViewById(R.id.tvNewsPostedBy);

        tvNewsPostedBy.setText(" " + postedBy);
        String postedAt = " " + Util.getTime(featured_news.getApproved_date());
        CustomTextView tvNewsPostedAt = (CustomTextView) findViewById(R.id.tvNewsPostedAt);
        tvNewsPostedAt.setText(postedAt);

        String title = featured_news.getName();
        CustomTextView tvNewsTitle = (CustomTextView) findViewById(R.id.tvNewsTitle);
//        int text_size_in_pixel_fn = getResources().getInteger(R.integer.news_title_size);
//        tvNewsTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, Config.get_text_size(HomeActivity.this, text_size_in_pixel_fn));
        tvNewsTitle.setText(title);

        setBreakingNews();

        if(conferenceNews != null && conferenceNews.getStarted_at() != null) {

            CustomTextView tvStartedAt = (CustomTextView) findViewById(R.id.tvStartedAt);
            String startedAt = conferenceNews.getStarted_at(); //2016-04-23 08:04:26

            if(startedAt != null && startedAt.contains(" ")) {
                String[] ss = startedAt.split(" ");
                if (conferenceNews.getStart_time() != null && ss.length > 0) {
                    String startTime = conferenceNews.getStart_time();
                    String final_to_show = ss[0] + " " + startTime;
                    tvStartedAt.setText(" " + final_to_show);
                } else if(ss.length > 0){
                    String final_to_show = ss[0];
                    tvStartedAt.setText(" " + final_to_show);
                }
            }
            ImageView imgConTime = (ImageView) findViewById(R.id.imgConTime);
            imgConTime.setVisibility(View.VISIBLE);
            CustomTextView tvLive = (CustomTextView) findViewById(R.id.tvLive);
            tvLive.setVisibility(View.VISIBLE);
        } else{
            ImageView imgConTime = (ImageView) findViewById(R.id.imgConTime);
            imgConTime.setVisibility(View.GONE);
            CustomTextView tvLive = (CustomTextView) findViewById(R.id.tvLive);
            tvLive.setVisibility(View.GONE);
        }

        if(conferenceNews != null && conferenceNews.getName() != null) {
            CustomTextView tvDebateTitleName = (CustomTextView) findViewById(R.id.tvDebateTitleName);
            tvDebateTitleName.setText(conferenceNews.getName().trim());
            CustomTextView tvDebateTitle = (CustomTextView) findViewById(R.id.tvDebateTitle);
            tvDebateTitle.setText("DEBATE OF THE DAY");
        }

        CustomTextView tvDebateTitle = (CustomTextView) findViewById(R.id.tvDebateTitle);
// here is the checking the conference is null or not (amaresh)
        String c_image = "";
        if(conferenceNews != null
                && conferenceNews.getConference_banner() != null
                && conferenceNews.getConference_banner().length() > 0) {
            c_image = Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_CONFERENCE + Config.getFolderForDP()
                    + conferenceNews.getConference_banner().trim();
        } else{
            if(feature_image_replace==null) {
                c_image = Config.DOMAIN + "/images/imgpsh_fullsize.jpeg";///bannerimg.jpg";
            }
            else{
                c_image=Config.DOMAIN+"/file/conference/original/"+feature_image_replace;
            }
        }
        ImageView imgConference = (ImageView) findViewById(R.id.imgConference);
        imageLoader.displayImage(c_image, imgConference, options);
        layoutFeaturedNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                Intent intent = new Intent(HomeActivity.this, NewsDetailsActivity.class);
                intent.putExtra("news", featured_news);
                startActivity(intent);
            }
        });

        layoutTopNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                Intent intent = new Intent(HomeActivity.this, NewsListActivity.class);
                intent.putExtra("isVideo", true);
                intent.putExtra("is_from_top_news", true);
                startActivity(intent);
            }
        });
// here is onclick to the layout (amaresh)
        layoutDebate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(conferenceNews != null

                        && conferenceNews.getName() != null
                        && conferenceNews.getName().length() > 0) {
                    getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                    Intent intent = new Intent(HomeActivity.this, VideoConferenceActivity.class);
                    startActivity(intent);
                } else {


                    /*new AlertDialog.Builder(HomeActivity.this)
                            .setTitle("Fail")
                            .setMessage("Unsuccessfull connection to Conference")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    dialog.cancel();
                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();


*/
                    if(feature_video_replace==null) {
                        Intent intent = new Intent(HomeActivity.this, VideoPlayerActivity.class);
                        //intent.putExtra("video_url", "http://ommcomnews.com/public/images/WISH_MONTAZ_OCNlow_versio-3.mp4");
                        intent.putExtra("video_url", "http://ommcomnews.com/images/WISH_MONTAZ_OCNlow_versio-3.mp4");
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(HomeActivity.this, VideoPlayerActivity.class);
                        intent.putExtra("video_url",Config.DOMAIN+"/file/conference/original/"+feature_video_replace);
                        startActivity(intent);
                    }
                }
            }
        });

        layoutCitizenJournalist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                Intent intent = new Intent(HomeActivity.this, CitizenJournalistActivity.class);
                //Intent intent = new Intent(HomeActivity.this, PaginationActivity.class);
                startActivity(intent);
            }
        });

        /*if(mTopVideo != null && mViralVideo != null){
            addTopAndViralVideoToView(layoutBody);
        } else if(mTopVideo != null){
            addTopAndViralVideoToView(layoutBody, mTopVideo, true);
        } else */

        /**
         *
         * Odisha Plus News
         * */
        /*RelativeLayout layout_odisha_plus_news = new RelativeLayout(HomeActivity.this);
        int height_layout_odisha_lpus_news_section = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_CATEGORY_NEWS);
        RelativeLayout.LayoutParams layout_params_odisha_plus_news = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT, height_layout_odisha_lpus_news_section);
        layout_odisha_plus_news.setLayoutParams(layout_params_odisha_plus_news);

        View view_odisha_plus_one = LayoutInflater.from(HomeActivity.this).inflate(R.layout.layout_category_news_one, null);
        ImageView img_odisha_plus_one = (ImageView) view_odisha_plus_one.findViewById(R.id.imgNewsOne);
        String odisha_plus_news_img_one_file_path = "";
        if(odisha_plus_news != null){
            odisha_plus_news_img_one_file_path = Util.getImageFilePathForNews(odisha_plus_news, null);
        }
        imageLoader.displayImage(odisha_plus_news_img_one_file_path//"http://timesofindia.indiatimes.com/" +
                //"thumb/msid-51900429,width-400,resizemode-4/51900429.jpg"
                , img_odisha_plus_one, options);
        CustomTextView tvodisha_plusNewsTitle = (CustomTextView) view_odisha_plus_one.findViewById(R.id.tvNewsTitle);
       //amaresh
        //String nnn=odisha_plus_news.getName();
        tvodisha_plusNewsTitle.setText(odisha_plus_news.getName());

        CustomTextView tvodisha_newsNewsPostedAt = (CustomTextView) view_odisha_plus_one.findViewById(R.id.tvNewsPostedAt);
        tvodisha_newsNewsPostedAt.setText(" " + Util.getTime(odisha_plus_news.getApproved_date()));

        CustomTextView tvNews_odisha_plus_Name = (CustomTextView) view_odisha_plus_one.findViewById(R.id.tvNewsCategoryName);
        tvNews_odisha_plus_Name.setText("Odisha Plus");
        img_odisha_plus_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                Intent intent = new Intent(HomeActivity.this, CategoryNewsListActivity.class);
                intent.putExtra("slug", "odishaPlus");
                startActivity(intent);
            }
        });

        layout_odisha_plus_news.addView(view_odisha_plus_one);
        layoutBody.addView(layout_odisha_plus_news);

        // No need of odisha news, so remove odisha news

        layout_odisha_plus_news.setVisibility(View.GONE);*/


        // for single object advertisement (ampos2) // in response pos3

        RelativeLayout pos2object = new RelativeLayout(HomeActivity.this);
        int height_pos2object = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_HEADER_ADVERTISEMENT);
        RelativeLayout.LayoutParams layoutParams_pos2object= new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT,height_pos2object);
        pos2object.setLayoutParams(layoutParams_pos2object);
        View view_p2ob = LayoutInflater.from(HomeActivity.this).inflate(R.layout.lay_posobj2, null);
        ImageView iv_pos2obj1=(ImageView) view_p2ob.findViewById(R.id.iv_pos2obj1);
        pos2object.addView(view_p2ob);
        // HERE IMAGE VIEW WILL BE SET
        layoutBody.addView(pos2object);
        AnimationUtil.slideInFromRight(pos2object, 500, 700);
        pos2object.setVisibility(View.GONE);
        if(pos2obj1==true){
            pos2object.setVisibility(View.VISIBLE);
            imageLoader.displayImage( pos3_obj1_image, iv_pos2obj1, options);

        }
        else {
            pos2object.setVisibility(View.GONE);
        }

        // for single array advertisement (pos1)

        LinearLayout pos2array = new LinearLayout(HomeActivity.this);
        int height_pos2array = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_TOP_NEWS);
        LinearLayout.LayoutParams layoutParams_pos2array = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, height_pos2array);
        pos2array.setLayoutParams(layoutParams_pos2array);
        View view_p2array = LayoutInflater.from(HomeActivity.this).inflate(R.layout.lay_posarray2, null);
        ImageView addim31=(ImageView) view_p2array.findViewById(R.id.addim31);
        ImageView addim32=(ImageView) view_p2array.findViewById(R.id.addim32);
        ImageView addim33=(ImageView) view_p2array.findViewById(R.id.addim33);
        pos2array.addView(view_p2array);
        layoutBody.addView(pos2array);
        pos2array.setVisibility(View.GONE);
        if(pos3arr1==true){
            pos2array.setVisibility(View.VISIBLE);
            imageLoader.displayImage( pos3_arr1_image1, addim31, options);
            imageLoader.displayImage( pos3_arr1_image2, addim32, options);
            imageLoader.displayImage( pos3_arr1_image3, addim33, options);
        }
        else{
            pos2array.setVisibility(View.GONE);
        }

        // second one

        RelativeLayout pos2object2 = new RelativeLayout(HomeActivity.this);
        int height_pos2object2 = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_HEADER_ADVERTISEMENT);
        RelativeLayout.LayoutParams layoutParams_pos2object2= new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT,height_pos2object2);
        pos2object2.setLayoutParams(layoutParams_pos2object2);
        View view_p2ob2 = LayoutInflater.from(HomeActivity.this).inflate(R.layout.lay_posobj22, null);
        ImageView iv_pos2obj2=(ImageView) view_p2ob2.findViewById(R.id.iv_pos2obj2);
        pos2object2.addView(view_p2ob2);
        // HERE IMAGE VIEW WILL BE SET
        layoutBody.addView(pos2object2);
        AnimationUtil.slideInFromRight(pos2object2, 500, 700);
        pos2object2.setVisibility(View.GONE);
        if(pos2obj2==true){
            pos2object2.setVisibility(View.VISIBLE);
            imageLoader.displayImage( pos3_obj2_image, iv_pos2obj2, options);

        }
        else {
            pos2object2.setVisibility(View.GONE);
        }

        // for single array advertisement (pos1)

        LinearLayout pos2array2 = new LinearLayout(HomeActivity.this);
        int height_pos2array2 = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_TOP_NEWS);
        LinearLayout.LayoutParams layoutParams_pos2array2 = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, height_pos2array2);
        pos2array2.setLayoutParams(layoutParams_pos2array2);
        View view_p2array2 = LayoutInflater.from(HomeActivity.this).inflate(R.layout.lay_posarray22, null);
        ImageView addim231=(ImageView) view_p2array2.findViewById(R.id.addim231);
        ImageView addim232=(ImageView) view_p2array2.findViewById(R.id.addim232);
        ImageView addim233=(ImageView) view_p2array2.findViewById(R.id.addim233);
        pos2array.addView(view_p2array2);
        layoutBody.addView(pos2array2);
        pos2array2.setVisibility(View.GONE);
        if(pos3arr2==true){
            pos2array2.setVisibility(View.VISIBLE);
            imageLoader.displayImage( pos3_arr2_image1, addim231, options);
            imageLoader.displayImage( pos3_arr2_image2, addim232, options);
            imageLoader.displayImage( pos3_arr2_image3, addim233, options);
        }
        else{
            pos2array2.setVisibility(View.GONE);
        }

 /*
        *
        * ADVERTISING SECTION END  (pos2)
        * */

        if(categoryNews != null && categoryNews.size() > 0){
            int i = 0;
            int count = 2;
            while(i < categoryNews.size()){
                if(count % 2 == 0){

                    RelativeLayout layout_cat_news = new RelativeLayout(HomeActivity.this);
                    int height_layout_cat_news_section = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_CATEGORY_NEWS);
                    RelativeLayout.LayoutParams layout_params_cat_news = new RelativeLayout.LayoutParams
                            (RelativeLayout.LayoutParams.MATCH_PARENT, height_layout_cat_news_section);
                    layout_cat_news.setLayoutParams(layout_params_cat_news);

                    if(i >= categoryNews.size()){
                        break;
                    }

                    final News news = categoryNews.get(i);
                    View view_cat_two = LayoutInflater.from(HomeActivity.this).inflate(R.layout.layout_category_news_two, null);
                    ImageView img_cat_one = (ImageView) view_cat_two.findViewById(R.id.imgNewsOne);
                    String cat_news_img_one_file_path = "";
                    if(news != null){
                        cat_news_img_one_file_path = Util.getImageFilePathForNews(news, null);
                    }
                    imageLoader.displayImage(cat_news_img_one_file_path//"http://timesofindia.indiatimes.com/" +
                            //"thumb/msid-51900429,width-400,resizemode-4/51900429.jpg"
                            , img_cat_one, options);
                    CustomTextView tvcatNewsTitle = (CustomTextView) view_cat_two.findViewById(R.id.tvNewsTitle);
                    tvcatNewsTitle.setText(news.getName());

                    CustomTextView tvcatNewsPostedAt = (CustomTextView) view_cat_two.findViewById(R.id.tvNewsPostedAt);
                    tvcatNewsPostedAt.setText(" " + Util.getTime(news.getApproved_date()));

                    CustomTextView tvNewsCategoryName = (CustomTextView) view_cat_two.findViewById(R.id.tvNewsCategoryName);
                    tvNewsCategoryName.setText(news.getCategory());

                    img_cat_one.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                            Intent intent = new Intent(HomeActivity.this, CategoryNewsListActivity.class);
                            intent.putExtra("slug", news.getCategoryslug());
                            startActivity(intent);
                        }
                    });
                    if(i+1 >= categoryNews.size()){
                        break;
                    }
                    final News news2 = categoryNews.get(i + 1);
                    ImageView img_cat_two = (ImageView) view_cat_two.findViewById(R.id.imgNewsTwo);
                    String cat_news_img_two_file_path = "";
                    if(news2 != null){
                        cat_news_img_two_file_path = Util.getImageFilePathForNews(news2, null);
                    }
                    imageLoader.displayImage(cat_news_img_two_file_path//"http://timesofindia.indiatimes.com/" +
                            //"thumb/msid-51900429,width-400,resizemode-4/51900429.jpg"
                            , img_cat_two, options);
                    CustomTextView tvcatNewsTitleTwo = (CustomTextView) view_cat_two.findViewById(R.id.tvNewsTitleTwo);
                    tvcatNewsTitleTwo.setText(news2.getName());

                    CustomTextView tvNewsCategoryNameTwo = (CustomTextView) view_cat_two.findViewById(R.id.tvNewsCategoryNameTwo);
                    tvNewsCategoryNameTwo.setText(news2.getCategory());

                    CustomTextView tvcatNewsPostedAtTwo = (CustomTextView) view_cat_two.findViewById(R.id.tvNewsPostedAtTwo);
                    tvcatNewsPostedAtTwo.setText(" " + Util.getTime(news2.getApproved_date()));

                    img_cat_two.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                            //Intent intent = new Intent(HomeActivity.this, NewsDetailsActivity.class);
                            //intent.putExtra("news", news2);
                            Intent intent = new Intent(HomeActivity.this, CategoryNewsListActivity.class);
                            intent.putExtra("slug", news2.getCategoryslug());
                            startActivity(intent);
                        }
                    });

                    i = i + 2;
                    layout_cat_news.addView(view_cat_two);
                    layoutBody.addView(layout_cat_news);
                } else{

                    RelativeLayout layout_cat_news = new RelativeLayout(HomeActivity.this);
                    int height_layout_cat_news_section = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_CATEGORY_NEWS);
                    RelativeLayout.LayoutParams layout_params_cat_news = new RelativeLayout.LayoutParams
                            (RelativeLayout.LayoutParams.MATCH_PARENT, height_layout_cat_news_section);
                    layout_cat_news.setLayoutParams(layout_params_cat_news);

                    if(i >= categoryNews.size()){
                        break;
                    }
                    final News news = categoryNews.get(i);
                    View view_cat_one = LayoutInflater.from(HomeActivity.this).inflate(R.layout.layout_category_news_one, null);
                    ImageView img_cat_one = (ImageView) view_cat_one.findViewById(R.id.imgNewsOne);
                    String cat_news_img_one_file_path = "";
                    if(news != null){
                        cat_news_img_one_file_path = Util.getImageFilePathForNews(news, null);
                    }
                    imageLoader.displayImage(cat_news_img_one_file_path//"http://timesofindia.indiatimes.com/" +
                            //"thumb/msid-51900429,width-400,resizemode-4/51900429.jpg"
                            , img_cat_one, options);
                    CustomTextView tvcatNewsTitle = (CustomTextView) view_cat_one.findViewById(R.id.tvNewsTitle);
                    tvcatNewsTitle.setText(news.getName());

                    CustomTextView tvcatNewsPostedAt = (CustomTextView) view_cat_one.findViewById(R.id.tvNewsPostedAt);
                    tvcatNewsPostedAt.setText(" " + Util.getTime(news.getApproved_date()));

                    CustomTextView tvNewsCategoryName = (CustomTextView) view_cat_one.findViewById(R.id.tvNewsCategoryName);
                    tvNewsCategoryName.setText(news.getCategory());
                    img_cat_one.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                            //Intent intent = new Intent(HomeActivity.this, NewsDetailsActivity.class);
                            //intent.putExtra("news", news);
                            Intent intent = new Intent(HomeActivity.this, CategoryNewsListActivity.class);
                            intent.putExtra("slug", news.getCategoryslug());
                            startActivity(intent);
                        }
                    });

                    i = i + 1;
                    layout_cat_news.addView(view_cat_one);
                    layoutBody.addView(layout_cat_news);
                }
                count++;
            }
        }

        if(mViralVideo != null){
            addTopAndViralVideoToView(layoutBody);//, mViralVideo, false);
        }

        //layoutBottomAdvertisement.addView(getAdvertisementView("http://swisswatchwire.com/images/2013/01/movado.jpg"));
        //layoutTopAdvertisement.addView(getAdvertisementView("http://popsop.com/wp-content/uploads/181845-sprite-sprite.jpg"));
        if(layoutMostBottomAdvertisement != null) {
            layoutBody.addView(layoutMostBottomAdvertisement);
        }
    }

    private void setBreakingNews(){

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(5);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                CustomTextView tvBreakingNews = (CustomTextView) findViewById(R.id.tvBreakingNews);
                //AnimationUtil.slideInFromTop(tvBreakingNews, 1000, 0);
                CustomTextView tvBreakingNewsNo = (CustomTextView) findViewById(R.id.tvBreakingNewsNo);
                tvBreakingNewsNo.setVisibility(View.GONE);
                if(breaking_news != null && breaking_news.getbNewsList() != null
                        && breaking_news.getbNewsList().size() > 0){
                    //blink();
                    slideBreakingNewsContinuously(false);
                } else if(breaking_news != null && breaking_news.getNewsdetails() != null
                        && breaking_news.getNewsdetails().size() > 0) {
                    slideBreakingNewsContinuously(true);
                } else{
                    tvBreakingNews.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if(breaking_news != null && breaking_news.getbNewsList() != null
                && breaking_news.getbNewsList().size() > 0){
            //blink();
            CustomTextView tvBreakingNews = (CustomTextView) findViewById(R.id.tvBreakingNews);
            tvBreakingNews.setText("BREAKING NEWS");
            tvBreakingNews.startAnimation(anim);
        } else if(breaking_news != null && breaking_news.getNewsdetails() != null
                && breaking_news.getNewsdetails().size() > 0) {
            CustomTextView tvBreakingNews = (CustomTextView) findViewById(R.id.tvBreakingNews);
            tvBreakingNews.setText("NEWS UPDATE");
            tvBreakingNews.startAnimation(anim);
        } else{
            CustomTextView tvBreakingNews = (CustomTextView) findViewById(R.id.tvBreakingNews);
            tvBreakingNews.setVisibility(View.GONE);
        }
    }

    /**
     * Slide Breaking News
     * */
    private int indexxx = 0;
    private void slideBreakingNewsContinuously(final boolean isNewsUpdate){

        final List<String> allNews = new ArrayList<String>();

        if(!isNewsUpdate) {
            for (int i = 0; i < breaking_news.getbNewsList().size(); i++) {
                String s = breaking_news.getbNewsList().get(i).getTitle();
                Util.setToList(s, allNews);
                if(((i > 0 && i % 2 == 0) && breaking_news.getbNewsList().size() > 3)
                        || (breaking_news.getbNewsList().size() <= 3 && i == breaking_news.getbNewsList().size() - 1)){
                    allNews.add("BREAKING NEWS");
                }
            }
        } else{
            for (int i = 0; i < breaking_news.getNewsdetails().size(); i++) {
                String s = breaking_news.getNewsdetails().get(i).getTitle();
                Util.setToList(s, allNews);
                if(((i > 0 && i % 2 == 0) && breaking_news.getNewsdetails().size() > 3)
                    || (breaking_news.getNewsdetails().size() <= 3 && i == breaking_news.getNewsdetails().size() - 1)){
                    allNews.add("NEWS UPDATE");
                }
            }
        }

        showNews(allNews);
    }

    private void showNews(final List<String> allNews){

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            if(indexxx == allNews.size()){
                indexxx = 0;
            }
            CustomTextView tvBreakingNews = (CustomTextView) findViewById(R.id.tvBreakingNews);
            tvBreakingNews.setText(allNews.get(indexxx).toString());
            AnimationUtil.fadeIn(tvBreakingNews, 800, 0);
            indexxx++;
            showNews(allNews);
            }
        }, News_Change_Speed);
    }


    @Override
    protected void onDestroy() {
        getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 0).commit();
        //GCMManager.getInstance(this).unRegisterListener();
        if(timer != null){
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }

    private CounterClass timer = null;
    private void setCountDownForCopnference(long total_time_remain_in_milliseconds){
        timer = new CounterClass(total_time_remain_in_milliseconds,1000);
        timer.start();
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("NewApi")
    public class CounterClass extends CountDownTimer {
        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {
            CustomTextView tvCoundowntimer = (CustomTextView) findViewById(R.id.tvCoundowntimer);
            tvCoundowntimer.setVisibility(View.INVISIBLE);
        }
        @SuppressLint("NewApi")
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override public void onTick(long millisUntilFinished) {
            CustomTextView tvCoundowntimer = (CustomTextView) findViewById(R.id.tvCoundowntimer);
            tvCoundowntimer.setVisibility(View.VISIBLE);
            long millis = millisUntilFinished;
            String hms = String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            System.out.println(hms); tvCoundowntimer.setText(hms);
        }
    }

    private void addTopAndViralVideoToView(LinearLayout layoutBody){
        RelativeLayout layout_cat_news = new RelativeLayout(HomeActivity.this);
        int height_layout_cat_news_section = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_CATEGORY_NEWS);
        RelativeLayout.LayoutParams layout_params_cat_news = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT, height_layout_cat_news_section);
        layout_cat_news.setLayoutParams(layout_params_cat_news);


        // for single object advertisement (ampos3) respose pos4

        RelativeLayout pos3object = new RelativeLayout(HomeActivity.this);
        int height_pos3object = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_HEADER_ADVERTISEMENT);
        RelativeLayout.LayoutParams layoutParams_pos3object= new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT,height_pos3object);
        pos3object.setLayoutParams(layoutParams_pos3object);
        View view_p3ob = LayoutInflater.from(HomeActivity.this).inflate(R.layout.lay_posobj3, null);
        ImageView iv_pos3obj1=(ImageView)view_p3ob.findViewById(R.id.iv_pos3obj1);
        pos3object.addView(view_p3ob);
        // HERE IMAGE VIEW WILL BE SET
        layoutBody.addView(pos3object);
        AnimationUtil.slideInFromRight(pos3object, 500, 700);
        if(pos4obj1==false){
            pos3object.setVisibility(View.GONE);
        }
        else{
            pos3object.setVisibility(View.VISIBLE);
            imageLoader.displayImage( pos4_obj1_image, iv_pos3obj1, options);
        }

        // for single array advertisement (pos1)

        LinearLayout pos3array = new LinearLayout(HomeActivity.this);
        int height_pos3array = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_TOP_NEWS);
        LinearLayout.LayoutParams layoutParams_pos3array = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, height_pos3array);
        pos3array.setLayoutParams(layoutParams_pos3array);
        View view_p3array = LayoutInflater.from(HomeActivity.this).inflate(R.layout.lay_posarray3, null);
        ImageView addim311=(ImageView)view_p3array.findViewById(R.id.addim311);
        ImageView addim312=(ImageView)view_p3array.findViewById(R.id.addim312);
        ImageView addim313=(ImageView)view_p3array.findViewById(R.id.addim313);
        pos3array.addView(view_p3array);
        layoutBody.addView(pos3array);
        if(pos4arr1==false){
            pos3array.setVisibility(View.GONE);
        }
        else{
            pos3array.setVisibility(View.VISIBLE);
            imageLoader.displayImage( pos4_arr1_image1, addim311, options);
            imageLoader.displayImage( pos4_arr1_image2, addim312, options);
            imageLoader.displayImage( pos4_arr1_image3, addim313, options);


        }
        RelativeLayout pos3object2 = new RelativeLayout(HomeActivity.this);
        int height_pos3object2 = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_HEADER_ADVERTISEMENT);
        RelativeLayout.LayoutParams layoutParams_pos3object2= new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT,height_pos3object2);
        pos3object2.setLayoutParams(layoutParams_pos3object2);
        View view_p3ob2 = LayoutInflater.from(HomeActivity.this).inflate(R.layout.lay_posobj32, null);
        ImageView iv_pos3obj2=(ImageView)view_p3ob2.findViewById(R.id.iv_pos3obj2);
        pos3object2.addView(view_p3ob2);
        // HERE IMAGE VIEW WILL BE SET
        layoutBody.addView(pos3object2);
        AnimationUtil.slideInFromRight(pos3object2, 500, 700);
        if(pos4obj2==false){
            pos3object2.setVisibility(View.GONE);
        }
        else{
            pos3object2.setVisibility(View.VISIBLE);
            imageLoader.displayImage( pos4_obj2_image, iv_pos3obj2, options);
        }

        // for single array advertisement (pos1)

        LinearLayout pos3array2 = new LinearLayout(HomeActivity.this);
        int height_pos3array2 = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_TOP_NEWS);
        LinearLayout.LayoutParams layoutParams_pos3array2 = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, height_pos3array2);
        pos3array2.setLayoutParams(layoutParams_pos3array2);
        View view_p3array2 = LayoutInflater.from(HomeActivity.this).inflate(R.layout.lay_posarray32, null);
        ImageView addim321=(ImageView)view_p3array2.findViewById(R.id.addim321);
        ImageView addim322=(ImageView)view_p3array2.findViewById(R.id.addim322);
        ImageView addim323=(ImageView)view_p3array2.findViewById(R.id.addim323);
        pos3array2.addView(view_p3array2);
        layoutBody.addView(pos3array2);
        if(pos4arr2==false){
            pos3array2.setVisibility(View.GONE);
        }
        else{
            pos3array2.setVisibility(View.VISIBLE);
            imageLoader.displayImage( pos4_arr2_image1, addim321, options);
            imageLoader.displayImage( pos4_arr2_image2, addim322, options);
            imageLoader.displayImage( pos4_arr2_image3, addim323, options);


        }
 /*
        *
        * ADVERTISING SECTION END  (pos3)
        * */
        // for single object advertisement (ampos4) -- in response pos5

        RelativeLayout pos4object = new RelativeLayout(HomeActivity.this);
        int height_pos4object = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_HEADER_ADVERTISEMENT);
        RelativeLayout.LayoutParams layoutParams_pos4object= new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT,height_pos4object);
        pos4object.setLayoutParams(layoutParams_pos4object);
        View view_p4ob = LayoutInflater.from(HomeActivity.this).inflate(R.layout.lay_posobj4, null);
        ImageView iv_pos4obj1=(ImageView)view_p4ob.findViewById(R.id.iv_pos4obj1);
        pos4object.addView(view_p4ob);
        // HERE IMAGE VIEW WILL BE SET
        layoutBody.addView(pos4object);
        AnimationUtil.slideInFromRight(pos4object, 500, 700);
        if(pos5obj1==false){
            pos4object.setVisibility(View.GONE);
        }
        else{
            pos4object.setVisibility(View.VISIBLE);
            imageLoader.displayImage( pos5_obj1_image, iv_pos4obj1, options);
        }


        // for single array advertisement (pos4)

        LinearLayout pos4array = new LinearLayout(HomeActivity.this);
        int height_pos4array = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_TOP_NEWS);
        LinearLayout.LayoutParams layoutParams_pos4array = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, height_pos4array);
        pos4array.setLayoutParams(layoutParams_pos4array);
        View view_p4array = LayoutInflater.from(HomeActivity.this).inflate(R.layout.lay_posarray4, null);
        ImageView addim411=(ImageView)view_p4array.findViewById(R.id.addim411);
        ImageView addim412=(ImageView)view_p4array.findViewById(R.id.addim412);
        ImageView addim413=(ImageView)view_p4array.findViewById(R.id.addim413);
        pos4array.addView(view_p4array);
        layoutBody.addView(pos4array);
        if(pos4arr1==false){
            pos4array.setVisibility(View.GONE);
        }
        else{
            pos4array.setVisibility(View.VISIBLE);
            imageLoader.displayImage( pos5_arr1_image1, addim411, options);
            imageLoader.displayImage( pos5_arr1_image2, addim412, options);
            imageLoader.displayImage( pos5_arr1_image3, addim413, options);

        }

        RelativeLayout pos4object2 = new RelativeLayout(HomeActivity.this);
        int height_pos4object2 = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_HEADER_ADVERTISEMENT);
        RelativeLayout.LayoutParams layoutParams_pos4object2= new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT,height_pos4object2);
        pos4object2.setLayoutParams(layoutParams_pos4object2);
        View view_p4ob2 = LayoutInflater.from(HomeActivity.this).inflate(R.layout.lay_posobj42, null);
        ImageView iv_pos4obj2=(ImageView)view_p4ob2.findViewById(R.id.iv_pos4obj2);
        pos4object2.addView(view_p4ob2);
        // HERE IMAGE VIEW WILL BE SET
        layoutBody.addView(pos4object2);
        AnimationUtil.slideInFromRight(pos4object2, 500, 700);
        if(pos5obj2==false){
            pos4object2.setVisibility(View.GONE);
        }
        else{
            pos4object2.setVisibility(View.VISIBLE);
            imageLoader.displayImage( pos5_obj2_image, iv_pos4obj2, options);
        }


        // for single array advertisement (pos4)

        LinearLayout pos4array2 = new LinearLayout(HomeActivity.this);
        int height_pos4array2 = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_TOP_NEWS);
        LinearLayout.LayoutParams layoutParams_pos4array2 = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, height_pos4array2);
        pos4array2.setLayoutParams(layoutParams_pos4array2);
        View view_p4array2 = LayoutInflater.from(HomeActivity.this).inflate(R.layout.lay_posarray42, null);
        ImageView addim421=(ImageView)view_p4array2.findViewById(R.id.addim421);
        ImageView addim422=(ImageView)view_p4array2.findViewById(R.id.addim422);
        ImageView addim423=(ImageView)view_p4array2.findViewById(R.id.addim423);
        pos4array2.addView(view_p4array2);
        layoutBody.addView(pos4array2);
        if(pos4arr2==false){
            pos4array2.setVisibility(View.GONE);
        }
        else{
            pos4array2.setVisibility(View.VISIBLE);
            imageLoader.displayImage( pos5_arr2_image1, addim421, options);
            imageLoader.displayImage( pos5_arr2_image2, addim422, options);
            imageLoader.displayImage( pos5_arr2_image3, addim423, options);

        }
 /*
        *
        * ADVERTISING SECTION END  (pos4)
        * */


        View view_cat_two = LayoutInflater.from(HomeActivity.this).inflate(R.layout.layout_category_news_one, null);
        final News news2 = mViralVideo;
        ImageView img_cat_two = (ImageView) view_cat_two.findViewById(R.id.imgNewsOne);
        String cat_news_img_two_file_path = "";
        if(news2 != null){
            cat_news_img_two_file_path = Util.getImageFilePathForNews(news2, null);
        }
        imageLoader.displayImage(cat_news_img_two_file_path//"http://timesofindia.indiatimes.com/" +
                //"thumb/msid-51900429,width-400,resizemode-4/51900429.jpg"
                , img_cat_two, options);
        CustomTextView tvcatNewsTitleTwo = (CustomTextView) view_cat_two.findViewById(R.id.tvNewsTitle);
        tvcatNewsTitleTwo.setText(news2.getName());

        CustomTextView tvNewsCategoryNameTwo = (CustomTextView) view_cat_two.findViewById(R.id.tvNewsCategoryName);
        if(lang==null || lang.contentEquals("English")) {

            tvNewsCategoryNameTwo.setText(R.string.viral_vd_en);
        }
        else{
            tvNewsCategoryNameTwo.setText(R.string.viral_vd_od);

        }

        ImageView imgPostedAtTwo = (ImageView) view_cat_two.findViewById(R.id.imgPostedAt);
        imgPostedAtTwo.setVisibility(View.GONE);

        CustomTextView tvcatNewsPostedAtTwo = (CustomTextView) view_cat_two.findViewById(R.id.tvNewsPostedAt);
        tvcatNewsPostedAtTwo.setText(" " + Util.getTime(news2.getApproved_date()));

        img_cat_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                Intent intent = new Intent(HomeActivity.this, CategoryNewsListActivity.class);
                intent.putExtra("news", news2);
                intent.putExtra("isTopVideo", false);
                intent.putExtra("isViralVideo", true);
                startActivity(intent);
            }
        });

        layout_cat_news.addView(view_cat_two);
        layoutBody.addView(layout_cat_news);



        // for single object advertisement (ampos5)-- in response pos6

        RelativeLayout pos5object = new RelativeLayout(HomeActivity.this);
        int height_pos5object = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_HEADER_ADVERTISEMENT);
        RelativeLayout.LayoutParams layoutParams_pos5object= new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT,height_pos5object);
        pos5object.setLayoutParams(layoutParams_pos5object);
        View view_p5ob = LayoutInflater.from(HomeActivity.this).inflate(R.layout.lay_posobj5, null);
        ImageView iv_pos5obj1=(ImageView)view_p5ob.findViewById(R.id.iv_pos5obj1);
        pos5object.addView(view_p5ob);
        // HERE IMAGE VIEW WILL BE SET
        layoutBody.addView(pos5object);
        AnimationUtil.slideInFromRight(pos5object, 500, 700);
        if(pos6obj1==false){
            pos5object.setVisibility(View.GONE);
        }
        else{
            pos5object.setVisibility(View.VISIBLE);
            imageLoader.displayImage( pos6_obj1_image, iv_pos5obj1, options);

        }

        // for single array advertisement (pos4)

        LinearLayout pos5array = new LinearLayout(HomeActivity.this);
        int height_pos5array = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_TOP_NEWS);
        LinearLayout.LayoutParams layoutParams_pos5array = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, height_pos5array);
        pos5array.setLayoutParams(layoutParams_pos5array);
        View view_p5array = LayoutInflater.from(HomeActivity.this).inflate(R.layout.lay_posarray5, null);
        ImageView addim511=(ImageView)view_p5array.findViewById(R.id.addim511);
        ImageView addim512=(ImageView)view_p5array.findViewById(R.id.addim512);
        ImageView addim513=(ImageView)view_p5array.findViewById(R.id.addim513);
        pos5array.addView(view_p5array);
        layoutBody.addView(pos5array);
        if(pos6arr1==false){
            pos5array.setVisibility(View.GONE);
        }
        else {
            pos5array.setVisibility(View.VISIBLE);
            imageLoader.displayImage( pos6_arr1_image1, addim511, options);
            imageLoader.displayImage( pos6_arr1_image2, addim512, options);
            imageLoader.displayImage( pos6_arr1_image3, addim513, options);
        }

        RelativeLayout pos5object2 = new RelativeLayout(HomeActivity.this);
        int height_pos5object2 = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_HEADER_ADVERTISEMENT);
        RelativeLayout.LayoutParams layoutParams_pos5object2= new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT,height_pos5object2);
        pos5object2.setLayoutParams(layoutParams_pos5object2);
        View view_p5ob2 = LayoutInflater.from(HomeActivity.this).inflate(R.layout.lay_posobj52, null);
        ImageView iv_pos5obj2=(ImageView)view_p5ob2.findViewById(R.id.iv_pos5obj2);
        pos5object2.addView(view_p5ob2);
        // HERE IMAGE VIEW WILL BE SET
        layoutBody.addView(pos5object2);
        AnimationUtil.slideInFromRight(pos5object2, 500, 700);
        if(pos6obj2==false){
            pos5object2.setVisibility(View.GONE);
        }
        else{
            pos5object2.setVisibility(View.VISIBLE);
            imageLoader.displayImage( pos6_obj2_image, iv_pos5obj2, options);

        }

        // for single array advertisement (pos4)

        LinearLayout pos5array2 = new LinearLayout(HomeActivity.this);
        int height_pos5array2 = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_TOP_NEWS);
        LinearLayout.LayoutParams layoutParams_pos5array2 = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, height_pos5array2);
        pos5array2.setLayoutParams(layoutParams_pos5array2);
        View view_p5array2 = LayoutInflater.from(HomeActivity.this).inflate(R.layout.lay_posarray52, null);
        ImageView addim521=(ImageView)view_p5array2.findViewById(R.id.addim521);
        ImageView addim522=(ImageView)view_p5array2.findViewById(R.id.addim522);
        ImageView addim523=(ImageView)view_p5array2.findViewById(R.id.addim523);
        pos5array2.addView(view_p5array2);
        layoutBody.addView(pos5array2);
        if(pos6arr2==false){
            pos5array2.setVisibility(View.GONE);
        }
        else {
            pos5array2.setVisibility(View.VISIBLE);
            imageLoader.displayImage( pos6_arr2_image1, addim521, options);
            imageLoader.displayImage( pos6_arr2_image2, addim522, options);
            imageLoader.displayImage( pos6_arr2_image3, addim523, options);
        }
 /*
        *
        * ADVERTISING SECTION END  (pos5)
        * */


    }

    private void addTopAndViralVideoToView(LinearLayout layoutBody, final News news, final boolean isTopVideo){
        RelativeLayout layout_cat_news = new RelativeLayout(HomeActivity.this);
        int height_layout_cat_news_section = (int)(Util.getScreenHeight() * LAYOUT_WEIGHT_CATEGORY_NEWS);
        RelativeLayout.LayoutParams layout_params_cat_news = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT, height_layout_cat_news_section);
        layout_cat_news.setLayoutParams(layout_params_cat_news);

        View view_cat_two = LayoutInflater.from(HomeActivity.this).inflate(R.layout.layout_category_news_one, null);
        ImageView img_cat_one = (ImageView) view_cat_two.findViewById(R.id.imgNewsOne);
        String cat_news_img_one_file_path = "";
        if(news != null){
            cat_news_img_one_file_path = Util.getImageFilePathForNews(news, null);
        }
        imageLoader.displayImage(cat_news_img_one_file_path//"http://timesofindia.indiatimes.com/" +
                //"thumb/msid-51900429,width-400,resizemode-4/51900429.jpg"
                , img_cat_one, options);

        ImageView imgPostedAt = (ImageView) view_cat_two.findViewById(R.id.imgPostedAt);
        imgPostedAt.setVisibility(View.GONE);

        CustomTextView tvcatNewsTitle = (CustomTextView) view_cat_two.findViewById(R.id.tvNewsTitle);
        tvcatNewsTitle.setText(news.getName());

        CustomTextView tvcatNewsPostedAt = (CustomTextView) view_cat_two.findViewById(R.id.tvNewsPostedAt);
        tvcatNewsPostedAt.setText(" " + Util.getTime(news.getApproved_date()));

        CustomTextView tvNewsCategoryName = (CustomTextView) view_cat_two.findViewById(R.id.tvNewsCategoryName);
        if(isTopVideo) {
            tvNewsCategoryName.setText("Top Video");
        } else{
            tvNewsCategoryName.setText("Viral Video");
        }

        img_cat_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                Intent intent = new Intent(HomeActivity.this, CategoryNewsListActivity.class);
                intent.putExtra("news", news);
                if(isTopVideo) {
                    intent.putExtra("isTopVideo", true);
                } else{
                    intent.putExtra("isViralVideo", false);
                }
                startActivity(intent);
            }
        });

        ImageView imgPlay = (ImageView) view_cat_two.findViewById(R.id.imgPlay);
        imgPlay.setVisibility(View.VISIBLE);

        layout_cat_news.addView(view_cat_two);
        layoutBody.addView(layout_cat_news);
    }

    private void loadTopViralVideos(final boolean isRefresh){

        if(Util.getNetworkConnectivityStatus(HomeActivity.this) == false){
            Util.showDialogToShutdownApp(HomeActivity.this);
            return;
        }

        //final ProgressBar pBar = (ProgressBar) findViewById(R.id.pBar);
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //pBar.setVisibility(View.VISIBLE);
                isLatestData = false;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //pBar.setVisibility(View.GONE);
                mIsContentLoaded = true;
                if(isRefresh){
                    if(mSwipeRefreshLayout != null){
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    displayContent();
                } else if(mIsCategoriesLoaded
                        && mIsBreakingNewsLoaded
                        && mIsContentLoaded
                        && mIsContentDisplayed == false) {
                    if(mSwipeRefreshLayout != null){
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    anim.start();
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                InputStream in = null;
                int resCode = -1;

                try {
                    String link = null;
                    if(lang==null || lang.contentEquals("English")) {
                        link=Config.API_BASE_URL + Config.HOME_SCREEN_TOP_VIRAL_VIDEO+Config.EN_CONENT;

                    }
                    else{
                        link=Config.API_BASE_URL + Config.HOME_SCREEN_TOP_VIRAL_VIDEO+Config.OD_CONENT;

                    }
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //conn.setReadTimeout(10000);
                    //conn.setConnectTimeout(15000);
                    //conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setAllowUserInteraction(false);
                    //conn.setInstanceFollowRedirects(true);
                    conn.setRequestMethod("GET");
                    conn.connect();

                    resCode = conn.getResponseCode();
                    if (resCode == HttpURLConnection.HTTP_OK) {
                        in = conn.getInputStream();
                    }
                    Log.i("HomeActivity : loadNewsData()", "Error :" + resCode);
                    if (in == null) {
                        return null;
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    String response = "", data = "";

                    while ((data = reader.readLine()) != null) {
                        response += data + "\n";
                    }

                    Log.i(TAG, "Response : " + response);

                    /*{
                               "TOP_VIDEO": {
                                    "file_path": "185134078014649630961671479371.mp4",
                                    "name": "Demo",
                                    "slug": "demo",
                                    "is_video": "1"
                                },
                                "VIRAL_VIDEO": {
                                    "file_path": "185134078014649630961671479371.mp4",
                                    "name": "Demo",
                                    "slug": "demo",
                                    "is_video": "1"
                                }
                    *    }
                    * */

                    JSONObject res = new JSONObject(response);
                    if(res.isNull("TOP_VIDEO") == false){
                        mTopVideo = new News(Parcel.obtain());
                        if(res.getJSONObject("TOP_VIDEO").isNull("file_path") == false){
                            String file_path = res.getJSONObject("TOP_VIDEO").getString("file_path");
                            mTopVideo.setFile_path(file_path);
                        }
                        if(res.getJSONObject("TOP_VIDEO").isNull("name") == false){
                            String name = res.getJSONObject("TOP_VIDEO").getString("name");
                            mTopVideo.setName(name);
                        }
                        if(res.getJSONObject("TOP_VIDEO").isNull("slug") == false){
                            String slug = res.getJSONObject("TOP_VIDEO").getString("slug");
                            mTopVideo.setSlug(slug);
                        }
                        if(res.getJSONObject("TOP_VIDEO").isNull("is_video") == false){
                            String is_video = res.getJSONObject("TOP_VIDEO").getString("is_video");
                            mTopVideo.setIs_video(is_video);
                        }
                    }
                    if(res.isNull("VIRAL_VIDEO") == false){
                        mViralVideo = new News(Parcel.obtain());
                        if(res.getJSONObject("VIRAL_VIDEO").isNull("file_path") == false){
                            String file_path = res.getJSONObject("VIRAL_VIDEO").getString("file_path");
                            mViralVideo.setFile_path(file_path);
                        }
                        if(res.getJSONObject("VIRAL_VIDEO").isNull("name") == false){
                            String name = res.getJSONObject("VIRAL_VIDEO").getString("name");
                            mViralVideo.setName(name);
                        }
                        if(res.getJSONObject("VIRAL_VIDEO").isNull("slug") == false){
                            String slug = res.getJSONObject("VIRAL_VIDEO").getString("slug");
                            mViralVideo.setSlug(slug);
                        }
                        if(res.getJSONObject("VIRAL_VIDEO").isNull("is_video") == false){
                            String is_video = res.getJSONObject("VIRAL_VIDEO").getString("is_video");
                            mViralVideo.setIs_video(is_video);
                        }
                    }
                    isLatestData = true;
                    return null;
                } catch(SocketTimeoutException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch(ConnectException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch(MalformedURLException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch (IOException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch(Exception exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                }
                return null;
            }
        }.execute();
    }

    private void loadAdvertisementPost(final boolean isRefresh){

        if(Util.getNetworkConnectivityStatus(HomeActivity.this) == false){
            Util.showDialogToShutdownApp(HomeActivity.this);
            return;
        }

        //final ProgressBar pBar = (ProgressBar) findViewById(R.id.pBar);
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //pBar.setVisibility(View.VISIBLE);
                isLatestData = false;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //pBar.setVisibility(View.GONE);
                mIsContentLoaded = true;
                if(isRefresh){
                    if(mSwipeRefreshLayout != null){
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    displayContent();
                } else if(mIsCategoriesLoaded
                        && mIsBreakingNewsLoaded
                        && mIsContentLoaded
                        && mIsContentDisplayed == false) {
                    if(mSwipeRefreshLayout != null){
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    anim.start();
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                InputStream in = null;
                int resCode = -1;

                try {
                    String link = Config.API_BASE_URL + Config.HOME_SCREEN_ADVERTISEMENT_POST;
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //conn.setReadTimeout(10000);
                    //conn.setConnectTimeout(15000);
                    //conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setAllowUserInteraction(false);
                    //conn.setInstanceFollowRedirects(true);
                    conn.setRequestMethod("GET");
                    conn.connect();

                    resCode = conn.getResponseCode();
                    if (resCode == HttpURLConnection.HTTP_OK) {
                        in = conn.getInputStream();
                    }
                    Log.i("HomeActivity : loadNewsData()", "Error :" + resCode);
                    if (in == null) {
                        return null;
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    String response = "", data = "";

                    while ((data = reader.readLine()) != null) {
                        response += data + "\n";
                    }

                    Log.i(TAG, "Response : " + response);

                    JSONObject res = new JSONObject(response);
                    if(res.isNull("ADVERTISEMENT_HEADER") == false){
                        ADVERTISEMENT_HEADER = res.getInt("ADVERTISEMENT_HEADER");
                    }

                    if(res.isNull("ADVERTISEMENT_MIDDLE") == false){
                        ADVERTISEMENT_MIDDLE = res.getInt("ADVERTISEMENT_MIDDLE");
                    }

                    if(res.isNull("ADVERTISEMENT_FOOTER") == false){
                        ADVERTISEMENT_FOOTER = res.getInt("ADVERTISEMENT_FOOTER");
                    }

                    if(res.isNull("ADVERTISEMENT") == false){
                        JSONArray top_news_now = res.getJSONArray("ADVERTISEMENT");
                        for(int i = 0; i < top_news_now.length(); i++){
                            Advertisement advertisement = new Advertisement(Parcel.obtain());
                            if(top_news_now.getJSONObject(i).isNull("name") == false){
                                String name = top_news_now.getJSONObject(i).getString("name");
                                advertisement.setName(name);
                            }
                            if(top_news_now.getJSONObject(i).isNull("advertisement_section_id") == false){
                                String section = top_news_now.getJSONObject(i).getString("advertisement_section_id");
                                advertisement.setSection(section);
                            }
                            if(top_news_now.getJSONObject(i).isNull("file_path") == false){
                                String file_path = top_news_now.getJSONObject(i).getString("file_path");
                                advertisement.setFile_path(file_path);
                            }
                            if(top_news_now.getJSONObject(i).isNull("url_link") == false){
                                String url_link = top_news_now.getJSONObject(i).getString("url_link");
                                advertisement.setUrl_link(url_link);
                            }
                            advertisementList.add(advertisement);
                        }
                    }

                    isLatestData = true;
                    return null;
                } catch(SocketTimeoutException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch(ConnectException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch(MalformedURLException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch (IOException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch(Exception exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                }
                return null;
            }
        }.execute();
    }

    private void loadCategoryNews(final boolean isRefresh){

        if(Util.getNetworkConnectivityStatus(HomeActivity.this) == false){
            Util.showDialogToShutdownApp(HomeActivity.this);
            return;
        }

        //final ProgressBar pBar = (ProgressBar) findViewById(R.id.pBar);
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //pBar.setVisibility(View.VISIBLE);
                isLatestData = false;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //pBar.setVisibility(View.GONE);
                mIsContentLoaded = true;
                if(isRefresh){
                    if(mSwipeRefreshLayout != null){
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    displayContent();
                } else if(mIsCategoriesLoaded
                        && mIsBreakingNewsLoaded
                        && mIsContentLoaded
                        && mIsContentDisplayed == false) {
                    if(mSwipeRefreshLayout != null){
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    anim.start();
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                InputStream in = null;
                int resCode = -1;

                try {
                    String link = null;
                    if(lang==null || lang.contentEquals("English")) {
                        link=Config.API_BASE_URL + Config.HOME_SCREEN_CATEGORY_NEWS+Config.EN_CONENT;

                    }
                    else{
                        link=Config.API_BASE_URL + Config.HOME_SCREEN_CATEGORY_NEWS+Config.OD_CONENT;

                    }
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //conn.setReadTimeout(10000);
                    //conn.setConnectTimeout(15000);
                    //conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setAllowUserInteraction(false);
                    //conn.setInstanceFollowRedirects(true);
                    conn.setRequestMethod("GET");
                    conn.connect();

                    resCode = conn.getResponseCode();
                    if (resCode == HttpURLConnection.HTTP_OK) {
                        in = conn.getInputStream();
                    }
                    Log.i("HomeActivity : loadNewsData()", "Error :" + resCode);
                    if (in == null) {
                        return null;
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    String response = "", data = "";

                    while ((data = reader.readLine()) != null) {
                        response += data + "\n";
                    }

                    Log.i(TAG, "Response : " + response);

                    JSONArray category_news_list = new JSONArray(response);
                    categoryNews.clear();
                    for(int i = 0; i < category_news_list.length(); i++){
                        News news = new News(Parcel.obtain());
                        if(category_news_list.getJSONObject(i).isNull("name") == false){
                            String name = category_news_list.getJSONObject(i).getString("name");
                            news.setName(name);
                        }
                        if(category_news_list.getJSONObject(i).isNull("slug") == false){
                            String slug = category_news_list.getJSONObject(i).getString("slug");
                            news.setSlug(slug);
                        }
                        if(category_news_list.getJSONObject(i).isNull("featured_image") == false){
                            String featured_image = category_news_list.getJSONObject(i).getString("featured_image");
                            news.setImage(featured_image);
                        }
                        if(category_news_list.getJSONObject(i).isNull("short_description") == false){
                            String short_description = category_news_list.getJSONObject(i).getString("short_description");
                            news.setShort_description(short_description);
                        }
                        if(category_news_list.getJSONObject(i).isNull("approved_date") == false){
                            String approved_date = category_news_list.getJSONObject(i).getString("approved_date");
                            news.setApproved_date(approved_date);
                        }
                        if(category_news_list.getJSONObject(i).isNull("username") == false){
                            String username = category_news_list.getJSONObject(i).getString("username");
                            news.setJounalist_name(username);
                        }
                        if(category_news_list.getJSONObject(i).isNull("categoryname") == false){
                            String categoryname = category_news_list.getJSONObject(i).getString("categoryname");
                            news.setCategory(categoryname);
                        }
                        categoryNews.add(news);
                    }
                    isLatestData = true;
                    return null;
                } catch(SocketTimeoutException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch(ConnectException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch(MalformedURLException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch (IOException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch(Exception exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                }
                return null;
            }
        }.execute();
    }

    private void loadCitizenCustomize(final boolean isRefresh){
        {

            if(Util.getNetworkConnectivityStatus(HomeActivity.this) == false){
                Util.showDialogToShutdownApp(HomeActivity.this);
                return;
            }

            //final ProgressBar pBar = (ProgressBar) findViewById(R.id.pBar);
            new AsyncTask<Void, Void, Void>(){
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    //pBar.setVisibility(View.VISIBLE);
                    isLatestData = false;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    //pBar.setVisibility(View.GONE);
                    mIsContentLoaded = true;
                    if(isRefresh){
                        if(mSwipeRefreshLayout != null){
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        displayContent();
                    } else if(mIsCategoriesLoaded
                            && mIsBreakingNewsLoaded
                            && mIsContentLoaded
                            && mIsContentDisplayed == false) {
                        if(mSwipeRefreshLayout != null){
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        anim.start();
                    }
                }

                @Override
                protected Void doInBackground(Void... params) {
                    InputStream in = null;
                    int resCode = -1;

                    try {
                        String link = Config.API_BASE_URL + Config.HOME_SCREEN_CITIZEN_CUSTOMIZE;
                        URL url = new URL(link);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        //conn.setReadTimeout(10000);
                        //conn.setConnectTimeout(15000);
                        //conn.setDoInput(true);
                        conn.setDoOutput(true);
                        conn.setAllowUserInteraction(false);
                        //conn.setInstanceFollowRedirects(true);
                        conn.setRequestMethod("GET");
                        conn.connect();

                        resCode = conn.getResponseCode();
                        if (resCode == HttpURLConnection.HTTP_OK) {
                            in = conn.getInputStream();
                        }
                        Log.i("HomeActivity : loadNewsData()", "Error :" + resCode);
                        if (in == null) {
                            return null;
                        }
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                        String response = "", data = "";

                        while ((data = reader.readLine()) != null) {
                            response += data + "\n";
                        }

                        Log.i(TAG, "Response : " + response);

                        JSONObject res = new JSONObject(response);
                        if(res != null){
                            if(res.isNull("name") == false) {
                                String name = res.getString("name");
                                citizenJournalistVideos.setName(name);
                            }
                            if(res.isNull("file_path") == false) {
                                String file_path = res.getString("file_path");
                                citizenJournalistVideos.setFile_path(file_path);
                            }
                        }
                        isLatestData = true;
                        return null;
                    } catch(SocketTimeoutException exception){
                        Log.e(TAG, "LoginAsync : doInBackground", exception);
                    } catch(ConnectException exception){
                        Log.e(TAG, "LoginAsync : doInBackground", exception);
                    } catch(MalformedURLException exception){
                        Log.e(TAG, "LoginAsync : doInBackground", exception);
                    } catch (IOException exception){
                        Log.e(TAG, "LoginAsync : doInBackground", exception);
                    } catch(Exception exception){
                        Log.e(TAG, "LoginAsync : doInBackground", exception);
                    }
                    return null;
                }
            }.execute();
        }
    }



    private void loadConferenceNews(final boolean isRefresh){

        if(Util.getNetworkConnectivityStatus(HomeActivity.this) == false){
            Util.showDialogToShutdownApp(HomeActivity.this);
            return;
        }

        //final ProgressBar pBar = (ProgressBar) findViewById(R.id.pBar);
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //pBar.setVisibility(View.VISIBLE);
                isLatestData = false;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //pBar.setVisibility(View.GONE);
                mIsContentLoaded = true;
                if(isRefresh){
                    if(mSwipeRefreshLayout != null){
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    displayContent();
                } else if(mIsCategoriesLoaded
                        && mIsBreakingNewsLoaded
                        && mIsContentLoaded
                        && mIsContentDisplayed == false) {
                    if(mSwipeRefreshLayout != null){
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    anim.start();
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                InputStream in = null;
                int resCode = -1;

                try {
                    String link = Config.API_BASE_URL_VC + Config.HOME_SCREEN_CONFERENCE;
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //conn.setReadTimeout(10000);
                    //conn.setConnectTimeout(15000);
                    //conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setAllowUserInteraction(false);
                    //conn.setInstanceFollowRedirects(true);
                    conn.setRequestMethod("GET");
                    conn.connect();

                    resCode = conn.getResponseCode();
                    if (resCode == HttpURLConnection.HTTP_OK) {
                        in = conn.getInputStream();
                    }
                    Log.i("HomeActivity : loadNewsData()", "Error :" + resCode);
                    if (in == null) {
                        return null;
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    String response = "", data = "";

                    while ((data = reader.readLine()) != null) {
                        response += data + "\n";
                    }

                    Log.i(TAG, "Response : " + response);

                    JSONObject conference_news = new JSONObject(response);
                    if(conference_news != null){
                        conferenceNews = new ConferenceNews(Parcel.obtain());
                        if(conference_news.isNull("id") == false) {
                            String id = conference_news.getString("id");
                            conferenceNews.setId(id);
                        }
                        if(conference_news.isNull("featured_image") == false){
                            String featured_image = conference_news.getString("featured_image");
                            conferenceNews.setFeatured_image(featured_image);
                        }

                        if(conference_news.isNull("name") == false) {
                            String name = conference_news.getString("name");
                            conferenceNews.setName(name);
                        }
                        if(conference_news.isNull("short_desc") == false) {
                            String short_desc = conference_news.getString("short_desc");
                            conferenceNews.setShort_desc(short_desc);
                        }
                        if(conference_news.isNull("long_desc") == false) {
                            String long_desc = conference_news.getString("long_desc");
                            conferenceNews.setLong_desc(long_desc);
                        }
                        if(conference_news.isNull("conference_banner") == false) {
                            String conference_banner = conference_news.getString("conference_banner");
                            conferenceNews.setConference_banner(conference_banner);
                        }
                        if(conference_news.isNull("started_at") == false){
                            String started_at = conference_news.getString("started_at");
                            conferenceNews.setStarted_at(started_at);
                        }
                        if(conference_news.isNull("start_time") == false) {
                            String start_time = conference_news.getString("start_time");
                            conferenceNews.setStart_time(start_time);
                        }
                        if(conference_news.isNull("end_time") == false) {
                            String end_time = conference_news.getString("end_time");
                            conferenceNews.setEnd_time(end_time);
                        }
                    }
                    isLatestData = true;
                    return null;
                } catch(SocketTimeoutException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch(ConnectException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch(MalformedURLException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch (IOException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch(Exception exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                }
                return null;
            }
        }.execute();
    }

    private void loadFeaturedNews(final boolean isRefresh){

        if(Util.getNetworkConnectivityStatus(HomeActivity.this) == false){
            Util.showDialogToShutdownApp(HomeActivity.this);
            return;
        }

        //final ProgressBar pBar = (ProgressBar) findViewById(R.id.pBar);
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //pBar.setVisibility(View.VISIBLE);
                isLatestData = false;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //pBar.setVisibility(View.GONE);
                mIsContentLoaded = true;
                if(isRefresh){
                    if(mSwipeRefreshLayout != null){
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    displayContent();
                } else if(mIsCategoriesLoaded
                        && mIsBreakingNewsLoaded
                        && mIsContentLoaded
                        && mIsContentDisplayed == false) {
                    if(mSwipeRefreshLayout != null){
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    anim.start();
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                InputStream in = null;
                int resCode = -1;

                try {
                    String link = null;
                    if(lang==null || lang.contentEquals("English")) {
                        link=Config.API_BASE_URL + Config.HOME_SCREEN_FEATURED_NEWS+Config.EN_CONENT;

                    }
                    else{
                        link=Config.API_BASE_URL + Config.HOME_SCREEN_FEATURED_NEWS+Config.OD_CONENT;

                    }
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setAllowUserInteraction(false);
                    conn.setRequestMethod("GET");
                    conn.connect();

                    resCode = conn.getResponseCode();
                    if (resCode == HttpURLConnection.HTTP_OK) {
                        in = conn.getInputStream();
                    }
                    Log.i("HomeActivity : loadNewsData()", "Error :" + resCode);
                    if (in == null) {
                        return null;
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    String response = "", data = "";

                    while ((data = reader.readLine()) != null) {
                        response += data + "\n";
                    }

                    Log.i(TAG, "Response : " + response);

                    JSONObject fnews = new JSONObject(response);
                    if(fnews != null) {

                        if (fnews.isNull("id") == false) {
                            String id = fnews.getString("id");
                            featured_news.setId(id);
                        }

                        if (fnews.isNull("name") == false) {
                            String name = fnews.getString("name");
                            featured_news.setName(name);
                        }

                        if (fnews.isNull("slug") == false) {
                            String slug = fnews.getString("slug");
                            featured_news.setSlug(slug);
                        }

                        if (fnews.isNull("featured_image") == false) {
                            String featured_image = fnews.getString("featured_image");
                            featured_news.setImage(featured_image);
                        }

                        if (fnews.isNull("short_description") == false){
                            String short_description = fnews.getString("short_description");
                            featured_news.setShort_description(short_description);
                        }

                        if(fnews.isNull("file_path") == false) {
                            String file_path = fnews.getString("file_path");
                            featured_news.setFile_path(file_path);
                        }

                        if(fnews.isNull("approved_date") == false) {
                            String approved_date = fnews.getString("approved_date");
                            featured_news.setApproved_date(approved_date);
                        }

                        if(fnews.isNull("position") == false) {
                            String position = fnews.getString("position");
                            featured_news.setPosition(position);
                        }

                        if(fnews.isNull("user_id") == false) {
                            String user_id = fnews.getString("user_id");
                            featured_news.setUser_id(user_id);
                        }

                        if(fnews.isNull("username") ==false) {
                            String username = fnews.getString("username");
                            featured_news.setJounalist_name(username);
                        }
                    }

                    isLatestData = true;
                    return null;
                } catch(SocketTimeoutException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch(ConnectException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch(MalformedURLException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch (IOException exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                } catch(Exception exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                }
                return null;
            }
        }.execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopVideo();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

        super.onPause();
        stopVideo();
    }
}
