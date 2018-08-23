package com.lipl.ommcom.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.daimajia.easing.linear.Linear;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lipl.ommcom.R;
import com.lipl.ommcom.adapter.NewsListAdapter;
import com.lipl.ommcom.pojo.Advertisement;
import com.lipl.ommcom.pojo.News;
import com.lipl.ommcom.pojo.PopupAdvertisement;
import com.lipl.ommcom.util.Config;
import com.lipl.ommcom.util.CustomTextView;
import com.lipl.ommcom.util.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

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
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class CategoryNewsListActivity extends AppCompatActivity
        implements View.OnClickListener, NewsListAdapter.OnItemClickListener,
        SearchView.OnQueryTextListener  {

    private ArrayList<News> mNewsList;
    private ArrayList<Advertisement> mAdvertisements;
    private News mTopNews;
    private ProgressBar pBar = null;
    private static final String TAG = "CategoryNewsList";
    private String slug = "",lang;
    private boolean isTopVideo = false;
    private boolean isViralVideo = false;
    private XRecyclerView mRecyclerView;
    private NewsListAdapter mAdapter;

    private PopupAdvertisement popupAdvertisement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_category_news);
        lang = getSharedPreferences(Config.SHAREDPREFERENCE_LANGUAGE, 0).getString(Config.LANG, null);

        if(getIntent().getExtras() != null){
            slug = getIntent().getExtras().getString("slug");
            isTopVideo = getIntent().getExtras().getBoolean("isTopVideo");
            isViralVideo = getIntent().getExtras().getBoolean("isViralVideo");
        }

        pBar = (ProgressBar) findViewById(R.id.pBar);
        mNewsList = new ArrayList<News>();
        mTopNews = new News(Parcel.obtain());
        mRecyclerView = (XRecyclerView)this.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        getData();
    }

    private void updateView(boolean isFilterList, String query_text){

        TextView text_empty = (TextView) findViewById(R.id.text_empty);
        text_empty.setVisibility(View.GONE);
        if(mTopNews == null || mTopNews.getId() == null){
            //Show there is no news here
            text_empty.setText("No news as of now.");
            text_empty.setVisibility(View.VISIBLE);
            setToolBar();

            return;
        }

        RelativeLayout parentLayout =   (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.layout_featured_news,
                (ViewGroup)findViewById(android.R.id.content),false);
        int height_layout_top_advertisement = (int)(Util.getScreenHeight() * 0.27f);
        RelativeLayout.LayoutParams layout_params_top_advertisement = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT, height_layout_top_advertisement);
        parentLayout.setLayoutParams(layout_params_top_advertisement);
        ImageView displayImage =(ImageView) parentLayout.findViewById(R.id.imgDisplayPicture);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.stub)//ic_stub
                .showImageForEmptyUri(R.drawable.empty)//ic_empty
                .showImageOnFail(R.drawable.error)//ic_error
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .displayer(new SimpleBitmapDisplayer())
                .build();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(CategoryNewsListActivity.this));
        //String image_url = Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_NEWS +
          //      Config.getFolderForDP() + mTopNews.getImage();
        ImageView imgPlay = (ImageView) parentLayout.findViewById(R.id.imgPlay);
        String image_url = Util.getImageFilePathForNews(mTopNews, imgPlay);

        String isVideo = mTopNews.getIs_video();
        if(isVideo != null && isVideo.trim().length() > 0 && isVideo.trim().equalsIgnoreCase("1")){
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

            imgPlay.setVisibility(View.VISIBLE);
        } else {
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
            imgPlay.setVisibility(View.GONE);
        }

        imageLoader.displayImage(image_url
                , displayImage, options);
        String postedBy = mTopNews.getJounalist_name();
        CustomTextView tvNewsPostedBy = (CustomTextView) parentLayout.findViewById(R.id.tvNewsPostedBy);
        tvNewsPostedBy.setText(" " + postedBy);
        String postedAt = " " + Util.getTime(mTopNews.getApproved_date());
        CustomTextView tvNewsPostedAt = (CustomTextView) parentLayout.findViewById(R.id.tvNewsPostedAt);
        tvNewsPostedAt.setText(" " + postedAt);
        String title = mTopNews.getName();
        CustomTextView tvNewsTitle = (CustomTextView) parentLayout.findViewById(R.id.tvNewsTitle);
        tvNewsTitle.setText(title);
        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryNewsListActivity.this, NewsDetailsActivity.class);
                intent.putExtra("news", mTopNews);
                startActivity(intent);
            }
        });

        mRecyclerView.setPullRefreshEnabled(false);

        if(mNewsList == null || mNewsList.size() <= 1){
            return;
        }

        if(isFilterList == false) {
            mRecyclerView.addHeaderView(parentLayout);
        }

        if(isFilterList && query_text != null
                && query_text.trim().length() > 0){
            ArrayList<News> filteredNewsList = new ArrayList<News>();
            for(int i = 0; i < mNewsList.size(); i++){
                String news_title = mNewsList.get(i).getName();
                if(news_title != null && news_title.trim().length() > 0
                        && query_text != null && news_title.trim().contains(query_text.trim())){
                    filteredNewsList.add(mNewsList.get(i));
                }
            }
            mAdapter = new NewsListAdapter(filteredNewsList, CategoryNewsListActivity.this, CategoryNewsListActivity.this);
        } else {
            mAdapter = new NewsListAdapter(mNewsList, CategoryNewsListActivity.this, CategoryNewsListActivity.this);
        }
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setRefreshing(false);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                getData();
            }
        });

        if(mAdvertisements != null && mAdvertisements.size() > 0) {
            for (Advertisement advertisement : mAdvertisements) {

                if(advertisement != null
                        && advertisement.getFile_path() != null) {
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

                    View view = LayoutInflater.from(CategoryNewsListActivity.this).inflate(R.layout.layout_advertisement,
                            (ViewGroup) findViewById(android.R.id.content), false);
                    ImageView mImageView = (ImageView) view.findViewById(R.id.imgAdv);
                    String imageurl = Util.getImageFilePathForAdvertisement(advertisement);
                    //Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_ADVERTISEMENT +
                      //      Config.getFolderForADV() + advertisement.getFile_path();
                    imageLoader.displayImage(imageurl,
                            mImageView, options);

                    mRecyclerView.addFootView(view);
                }
            }
        }

        setToolBar();
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
        getMenuInflater().inflate(R.menu.news_list_menu, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        if(mAdapter != null && mNewsList != null && mNewsList.size() > 0){
                            mAdapter.setFilter(mNewsList, CategoryNewsListActivity.this, CategoryNewsListActivity.this);
                        }
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });


        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.imgComment:
                Intent intent = new Intent(CategoryNewsListActivity.this, CommentListActivity.class);
                startActivity(intent);
                break;
            case R.id.tvComment:
                Intent intent1 = new Intent(CategoryNewsListActivity.this, CommentListActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(CategoryNewsListActivity.this, NewsDetailsActivity.class);
        intent.putExtra("news", mNewsList.get(position));
        startActivity(intent);
    }

    private int pagination_count = 1;
    private void getData(){
        if(Util.getNetworkConnectivityStatus(CategoryNewsListActivity.this) == false){
            Util.showDialogToShutdownApp(CategoryNewsListActivity.this);
            return;
        }

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                pBar.setVisibility(View.GONE);


                RelativeLayout relativeTopParent = (RelativeLayout) findViewById(R.id.relativeTopParent);
                RelativeLayout layoutRParent = (RelativeLayout) findViewById(R.id.layoutRParent);

                if(pagination_count == 1) {
                    if(popupAdvertisement != null) {
                        Util.showPopUpAdvertisement(CategoryNewsListActivity.this, popupAdvertisement, relativeTopParent, layoutRParent);
                        layoutRParent.setVisibility(View.VISIBLE);
                    }
                    updateView(false, "");
                } else {
                    layoutRParent.setVisibility(View.GONE);
                    if(mRecyclerView != null &&  mRecyclerView.getAdapter() != null) {
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                }
                pagination_count++;

                if(mRecyclerView != null){
                    mRecyclerView.loadMoreComplete();
                    mRecyclerView.refreshComplete();
                }

//                if(popupAdvertisement != null) {
//                    RelativeLayout relativeTopParent = (RelativeLayout) findViewById(R.id.relativeTopParent);
//                    RelativeLayout layoutRParent = (RelativeLayout) findViewById(R.id.layoutRParent);
//                    layoutRParent.setVisibility(View.VISIBLE);
//                    Util.showPopUpAdvertisement(CategoryNewsListActivity.this, popupAdvertisement, relativeTopParent, layoutRParent);
//                }
//                updateView();
            }

            @Override
            protected Void doInBackground(Void... params) {
                InputStream in = null;
                int resCode = -1;

                try {
                    String link = "";


                    if(lang==null || lang.contentEquals("English")) {
                        if (isViralVideo) {
                            link = Config.API_BASE_URL + Config.API_VIRAL_VIDEO_LIST+Config.EN_CONENT;
                        } else if (isTopVideo) {
                            link = Config.API_BASE_URL + Config.API_TOP_VIDEO_LIST+Config.EN_CONENT;
                        } else {

                            if(pagination_count > 1){
                                if(slug != null && slug.trim().equalsIgnoreCase("odishaPlus")) {
                                    link = Config.API_BASE_URL + Config.NEWS_DETAILS_API + "/" + slug + "?page=" + pagination_count+Config.EN_NEWCONENT;
                                } else{
                                    link = Config.API_BASE_URL + "/posts/nextCategory" + "/" + slug + "?page=" + pagination_count+Config.EN_NEWCONENT;
                                }
                            } else{
                                link = Config.API_BASE_URL + Config.NEWS_DETAILS_API + "/" + slug+Config.EN_CONENT;
                            }
                        }
                    }
                    else{
                        if (isViralVideo) {
                            link = Config.API_BASE_URL + Config.API_VIRAL_VIDEO_LIST+Config.OD_CONENT;
                        } else if (isTopVideo) {
                            link = Config.API_BASE_URL + Config.API_TOP_VIDEO_LIST+Config.OD_CONENT;
                        } else {

                            if(pagination_count > 1){
                                if(slug != null && slug.trim().equalsIgnoreCase("odishaPlus")) {
                                    link = Config.API_BASE_URL + Config.NEWS_DETAILS_API + "/" + slug + "?page=" + pagination_count+Config.OD_NEWCONENT;
                                } else{
                                    link = Config.API_BASE_URL + "/posts/nextCategory" + "/" + slug + "?page=" + pagination_count+Config.OD_NEWCONENT;
                                }
                            } else{
                                link = Config.API_BASE_URL + Config.NEWS_DETAILS_API + "/" + slug+Config.OD_CONENT;
                            }
                        }
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

                    /**
                     *
                     * {"total":1,
                     * "per_page":16,
                     * "current_page":1,
                     * "last_page":1,
                     * "next_page_url":null,
                     * "prev_page_url":null,
                     * "from":1,
                     * "to":5,
                     * "data":[{
                     *      "id":"10",
                     *      "name":"ICC to investigate Burundi political violence",
                     *      "slug":"icc-to-investigate-burundi-political-violence",
                     *      "shortdescription":"UN estimate says at least 430 people killed in the past year after President Nkurinziza launched election campaign...\r\n\r\n\r\n\r\n",
                     *      "featured_image":"2474974581461663657896622811-121212.jpg",
                     *      "file_path":"71224387914620023271502338763-BanglaCricket.mp4",
                     *      "is_video":"1",
                     *      "is_image":"0",
                     *      "approved_date":"2016-04-30 13:04:27",
                     *      "username":"John Pradhan"
                     *      }
                     * ]}
                     * */

                    JSONObject res = new JSONObject(response);
                    if (res.isNull("data") == false) {
                        if (mNewsList == null) {
                            mNewsList = new ArrayList<News>();
                        }
                        JSONArray jsonData = res.getJSONArray("data");
                        if (jsonData != null && jsonData.length() > 0) {
                            for (int i = 0; i < jsonData.length(); i++) {
                                News news = new News(Parcel.obtain());
                                if (jsonData.getJSONObject(i).isNull("id") == false) {
                                    String id = jsonData.getJSONObject(i).getString("id");
                                    news.setId(id);
                                }

                                if (jsonData.getJSONObject(i).isNull("name") == false) {
                                    String name = jsonData.getJSONObject(i).getString("name");
                                    news.setName(name);
                                }

                                if (jsonData.getJSONObject(i).isNull("slug") == false) {
                                    String slug = jsonData.getJSONObject(i).getString("slug");
                                    news.setSlug(slug);
                                }

                                if (jsonData.getJSONObject(i).isNull("shortdescription") == false) {
                                    String shortdescription = jsonData.getJSONObject(i).getString("shortdescription");
                                    news.setShort_description(shortdescription);
                                }

                                if (jsonData.getJSONObject(i).isNull("featured_image") == false) {
                                    String featured_image = jsonData.getJSONObject(i).getString("featured_image");
                                    news.setImage(featured_image);
                                }

                                if (jsonData.getJSONObject(i).isNull("file_path") == false) {
                                    String file_path = jsonData.getJSONObject(i).getString("file_path");
                                    news.setFile_path(file_path);
                                }

                                if (jsonData.getJSONObject(i).isNull("is_video") == false) {
                                    String is_video = jsonData.getJSONObject(i).getString("is_video");
                                    news.setIs_video(is_video);
                                }

                                if (jsonData.getJSONObject(i).isNull("is_image") == false) {
                                    String is_image = jsonData.getJSONObject(i).getString("is_image");
                                    news.setIs_image(is_image);
                                }

                                if (jsonData.getJSONObject(i).isNull("approved_date") == false) {
                                    String approved_date = jsonData.getJSONObject(i).getString("approved_date");
                                    news.setApproved_date(approved_date);
                                }

                                if (jsonData.getJSONObject(i).isNull("username") == false) {
                                    String username = jsonData.getJSONObject(i).getString("username");
                                    news.setJounalist_name(username);
                                }

                                if (i == 0) {
                                    mTopNews = news;
                                } else {
                                    mNewsList.add(news);
                                }
                            }
                        }
                    }

                    // Advertisements
                    if (res.isNull("Advertisement") == false) {
                        if (mAdvertisements == null) {
                            mAdvertisements = new ArrayList<Advertisement>();
                        }
                        JSONArray jsonData = res.getJSONArray("Advertisement");
                        if (jsonData != null && jsonData.length() > 0) {
                            for (int i = 0; i < jsonData.length(); i++) {
                                Advertisement advertisement = new Advertisement(Parcel.obtain());
                                if (jsonData.getJSONObject(i).isNull("id") == false) {
                                    String id = jsonData.getJSONObject(i).getString("id");
                                    advertisement.setId(id);
                                }
                                if (jsonData.getJSONObject(i).isNull("file_path") == false) {
                                    String file_path = jsonData.getJSONObject(i).getString("file_path");
                                    advertisement.setFile_path(file_path);
                                }
                                if (jsonData.getJSONObject(i).isNull("name") == false) {
                                    String name = jsonData.getJSONObject(i).getString("name");
                                    advertisement.setName(name);
                                }
                                if (jsonData.getJSONObject(i).isNull("file_type") == false) {
                                    String file_type = jsonData.getJSONObject(i).getString("file_type");
                                    advertisement.setFile_type(file_type);
                                }
                                mAdvertisements.add(advertisement);
                            }
                        }
                    }

                    if (res.isNull("advertisementPopup") == false) {
                        popupAdvertisement = new PopupAdvertisement(Parcel.obtain());
                        if (res.getJSONObject("advertisementPopup").isNull("id") == false) {
                            String id = res.getJSONObject("advertisementPopup").getString("id");
                            popupAdvertisement.setId(id);
                        }
                        if (res.getJSONObject("advertisementPopup").isNull("name") == false) {
                            String name = res.getJSONObject("advertisementPopup").getString("name");
                            popupAdvertisement.setName(name);
                        }
                        if (res.getJSONObject("advertisementPopup").isNull("sponsor_id") == false) {
                            String sponsor_id = res.getJSONObject("advertisementPopup").getString("sponsor_id");
                            popupAdvertisement.setSponsor_id(sponsor_id);
                        }
                        if (res.getJSONObject("advertisementPopup").isNull("user_id") == false) {
                            String user_id = res.getJSONObject("advertisementPopup").getString("user_id");
                            popupAdvertisement.setUser_id(user_id);
                        }
                        if (res.getJSONObject("advertisementPopup").isNull("cat_id") == false) {
                            String cat_id = res.getJSONObject("advertisementPopup").getString("cat_id");
                            popupAdvertisement.setCat_id(cat_id);
                        }
                        if (res.getJSONObject("advertisementPopup").isNull("created_at") == false) {
                            String created_at = res.getJSONObject("advertisementPopup").getString("created_at");
                            popupAdvertisement.setCreated_at(created_at);
                        }
                        if (res.getJSONObject("advertisementPopup").isNull("updated_at") == false) {
                            String updated_at = res.getJSONObject("advertisementPopup").getString("updated_at");
                            popupAdvertisement.setUpdated_at(updated_at);
                        }
                        if (res.getJSONObject("advertisementPopup").isNull("is_publish") == false) {
                            String is_publish = res.getJSONObject("advertisementPopup").getString("is_publish");
                            popupAdvertisement.setIs_publish(is_publish);
                        }
                        if (res.getJSONObject("advertisementPopup").isNull("publish_date") == false) {
                            String publish_date = res.getJSONObject("advertisementPopup").getString("publish_date");
                            popupAdvertisement.setPublish_date(publish_date);
                        }
                        if (res.getJSONObject("advertisementPopup").isNull("start_date") == false) {
                            String start_date = res.getJSONObject("advertisementPopup").getString("start_date");
                            popupAdvertisement.setStart_date(start_date);
                        }
                        if (res.getJSONObject("advertisementPopup").isNull("end_date") == false) {
                            String end_date = res.getJSONObject("advertisementPopup").getString("end_date");
                            popupAdvertisement.setEnd_date(end_date);
                        }
                        if (res.getJSONObject("advertisementPopup").isNull("advertisement_type_id") == false) {
                            String advertisement_type_id = res.getJSONObject("advertisementPopup").getString("advertisement_type_id");
                            popupAdvertisement.setAdvertisement_type_id(advertisement_type_id);
                        }
                        if (res.getJSONObject("advertisementPopup").isNull("advertisement_section_id") == false) {
                            String advertisement_section_id = res.getJSONObject("advertisementPopup").getString("advertisement_section_id");
                            popupAdvertisement.setAdvertisement_section_id(advertisement_section_id);
                        }
                        if (res.getJSONObject("advertisementPopup").isNull("is_enable") == false) {
                            String is_enable = res.getJSONObject("advertisementPopup").getString("is_enable");
                            popupAdvertisement.setIs_enable(is_enable);
                        }
                        if (res.getJSONObject("advertisementPopup").isNull("file_path") == false) {
                            String file_path = res.getJSONObject("advertisementPopup").getString("file_path");
                            popupAdvertisement.setFile_path(file_path);
                        }
                        if (res.getJSONObject("advertisementPopup").isNull("file_type") == false) {
                            String file_type = res.getJSONObject("advertisementPopup").getString("file_type");
                            popupAdvertisement.setFile_type(file_type);
                        }
                        if (res.getJSONObject("advertisementPopup").isNull("is_url") == false) {
                            String is_url = res.getJSONObject("advertisementPopup").getString("is_url");
                            popupAdvertisement.setIs_url(is_url);
                        }
                        if (res.getJSONObject("advertisementPopup").isNull("url_link") == false) {
                            String url_link = res.getJSONObject("advertisementPopup").getString("url_link");
                            popupAdvertisement.setUrl_link(url_link);
                        }
                        if (res.getJSONObject("advertisementPopup").isNull("priority") == false) {
                            String priority = res.getJSONObject("advertisementPopup").getString("priority");
                            popupAdvertisement.setPriority(priority);
                        }
                        if (res.getJSONObject("advertisementPopup").isNull("is_trash") == false) {
                            String is_trash = res.getJSONObject("advertisementPopup").getString("is_trash");
                            popupAdvertisement.setIs_trash(is_trash);
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

    @Override
    public boolean onQueryTextChange(String newText) {
        final ArrayList<News> filteredModelList = filter(mNewsList, newText);
        mAdapter.setFilter(filteredModelList, CategoryNewsListActivity.this, CategoryNewsListActivity.this);
        Log.i("NewsList", "Query newText: "+newText);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.i("NewsList", "Query : "+query);
        return true;
    }

    private ArrayList<News> filter(List<News> models, String query) {
        query = query.toLowerCase();

        final ArrayList<News> filteredModelList = new ArrayList<>();
        for (News model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}
