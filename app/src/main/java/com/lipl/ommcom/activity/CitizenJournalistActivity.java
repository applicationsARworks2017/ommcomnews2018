package com.lipl.ommcom.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcel;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.cunoraz.gifview.library.GifView;
import com.lipl.ommcom.R;
import com.lipl.ommcom.adapter.VideoListAdapter;
import com.lipl.ommcom.fragment.LatestVideosFragment;
import com.lipl.ommcom.fragment.MostViewedVideosFragment;
import com.lipl.ommcom.pojo.CitizenJournalistVideos;
import com.lipl.ommcom.pojo.PopupAdvertisement;
import com.lipl.ommcom.util.Config;
import com.lipl.ommcom.util.Util;

import org.json.JSONArray;
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

import io.fabric.sdk.android.Fabric;

public class CitizenJournalistActivity extends AppCompatActivity
        implements MostViewedVideosFragment.OnFragmentInteractionListener,
        LatestVideosFragment.OnFragmentInteractionListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private static final String TAG = "CitizenJournalistVideos";
    private PopupAdvertisement popupAdvertisement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_citizen_journalist);

        setToolBar();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        GifView fabPostComment = (GifView) findViewById(R.id.fabPostVideo);
        fabPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CitizenJournalistActivity.this, PostCommentActivity.class);
                intent.putExtra("is_video_upload", true);
                startActivity(intent);
            }
        });

        getAdvertisement();
    }

    private void setToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_citizen_journalist, menu);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            MostViewedVideosFragment fragment1 = MostViewedVideosFragment.newInstance("", "");
            LatestVideosFragment fragment2 = LatestVideosFragment.newInstance("", "");
            if(position == 0){
                return fragment1;
            } else{
                return fragment2;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 1:
                    return "LATEST";
                case 0:
                    return "MOST VIEWED";
            }
            return null;
        }
    }

    private void getAdvertisement(){

        if(Util.getNetworkConnectivityStatus(CitizenJournalistActivity.this) == false){
            Util.showDialogToShutdownApp(CitizenJournalistActivity.this);
            return;
        }
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if(popupAdvertisement != null) {
                    RelativeLayout relativeTopParent = (RelativeLayout) findViewById(R.id.relativeTopParent);
                    RelativeLayout layoutRParent = (RelativeLayout) findViewById(R.id.layoutRParent);
                    layoutRParent.setVisibility(View.VISIBLE);
                    Util.showPopUpAdvertisement(CitizenJournalistActivity.this, popupAdvertisement, relativeTopParent, layoutRParent);
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                InputStream in = null;
                int resCode = -1;

                try {
                    String link = Config.API_BASE_URL + Config.CITIZEN_NEWS_ADV_POPUP;
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

                    /**
                     * {
                     "total": 8,
                     "per_page": 16,
                     "current_page": 1,
                     "last_page": 1,
                     "next_page_url": null,
                     "prev_page_url": null,
                     "from": 1,
                     "to": 8,
                     "data": [
                     {
                     "id": "6",
                     "name": "Sameera Patel",
                     "file_path": "13644191381461934301895035599-document_413_13842_1450953346(1).flv",
                     "file_type": "Video",
                     "description": "Why I will vote for Bernie Sanders",
                     "slug": "why-i-will-vote-for-bernie-sanders",
                     "is_anonymous": "0"
                     }
                     ]
                     }
                     * */
                    if(response != null) {
                        JSONObject res = new JSONObject(response);
                        if(res != null){
                            popupAdvertisement = new PopupAdvertisement(Parcel.obtain());
                            if(res.isNull("id") == false){
                                String id = res.getString("id");
                                popupAdvertisement.setId(id);
                            }
                            if(res.isNull("name") == false){
                                String name = res.getString("name");
                                popupAdvertisement.setName(name);
                            }
                            if(res.isNull("sponsor_id") == false){
                                String sponsor_id = res.getString("sponsor_id");
                                popupAdvertisement.setSponsor_id(sponsor_id);
                            }
                            if(res.isNull("user_id") == false){
                                String user_id = res.getString("user_id");
                                popupAdvertisement.setUser_id(user_id);
                            }
                            if(res.isNull("cat_id") == false){
                                String cat_id = res.getString("cat_id");
                                popupAdvertisement.setCat_id(cat_id);
                            }
                            if(res.isNull("created_at") == false){
                                String created_at = res.getString("created_at");
                                popupAdvertisement.setCreated_at(created_at);
                            }
                            if(res.isNull("updated_at") == false){
                                String updated_at = res.getString("updated_at");
                                popupAdvertisement.setUpdated_at(updated_at);
                            }
                            if(res.isNull("is_publish") == false){
                                String is_publish = res.getString("is_publish");
                                popupAdvertisement.setIs_publish(is_publish);
                            }
                            if(res.isNull("publish_date") == false){
                                String publish_date = res.getString("publish_date");
                                popupAdvertisement.setPublish_date(publish_date);
                            }
                            if(res.isNull("start_date") == false){
                                String start_date = res.getString("start_date");
                                popupAdvertisement.setStart_date(start_date);
                            }
                            if(res.isNull("end_date") == false){
                                String end_date = res.getString("end_date");
                                popupAdvertisement.setEnd_date(end_date);
                            }
                            if(res.isNull("advertisement_type_id") == false){
                                String advertisement_type_id = res.getString("advertisement_type_id");
                                popupAdvertisement.setAdvertisement_type_id(advertisement_type_id);
                            }
                            if(res.isNull("advertisement_section_id") == false){
                                String advertisement_section_id = res.getString("advertisement_section_id");
                                popupAdvertisement.setAdvertisement_section_id(advertisement_section_id);
                            }
                            if(res.isNull("is_enable") == false){
                                String is_enable = res.getString("is_enable");
                                popupAdvertisement.setIs_enable(is_enable);
                            }
                            if(res.isNull("file_path") == false){
                                String file_path = res.getString("file_path");
                                popupAdvertisement.setFile_path(file_path);
                            }
                            if(res.isNull("file_type") == false){
                                String file_type = res.getString("file_type");
                                popupAdvertisement.setFile_type(file_type);
                            }
                            if(res.isNull("is_url") == false){
                                String is_url = res.getString("is_url");
                                popupAdvertisement.setIs_url(is_url);
                            }
                            if(res.isNull("url_link") == false){
                                String url_link = res.getString("url_link");
                                popupAdvertisement.setUrl_link(url_link);
                            }
                            if(res.isNull("priority") == false){
                                String priority = res.getString("priority");
                                popupAdvertisement.setPriority(priority);
                            }
                            if(res.isNull("is_trash") == false){
                                String is_trash = res.getString("is_trash");
                                popupAdvertisement.setIs_trash(is_trash);
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
                } catch(Exception exception){
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                }
                return null;
            }
        }.execute();
    }
}
