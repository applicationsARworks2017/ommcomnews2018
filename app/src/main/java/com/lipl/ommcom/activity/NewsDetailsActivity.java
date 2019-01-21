package com.lipl.ommcom.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.plus.PlusShare;
import com.lipl.ommcom.R;
import com.lipl.ommcom.adapter.FlipAdapter;
import com.lipl.ommcom.fliputil.FlipViewController;
import com.lipl.ommcom.pojo.Comment;
import com.lipl.ommcom.pojo.FImageListItem;
import com.lipl.ommcom.pojo.Item;
import com.lipl.ommcom.pojo.News;
import com.lipl.ommcom.pojo.NewsDetailsForFlipModel;
import com.lipl.ommcom.pojo.NewsImage;
import com.lipl.ommcom.pojo.NewsVideo;
import com.lipl.ommcom.twitter.Utils;
import com.lipl.ommcom.util.Config;
import com.lipl.ommcom.util.Util;

import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;
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

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class NewsDetailsActivity extends AppCompatActivity implements FlipAdapter.OnShareClick,
        FlipAdapter.OnCommentClick {

    private FlipViewController flipView;
    private static final String NEWS_DETAILS_API_BASE_URL =
            Config.API_BASE_URL + Config.NEWS_DETAILS_API + Config.FOLDER_SLUG;
    private ProgressBar pBar = null;
    private static final String TAG = "NewsDetailActivity";
    private News news;
    private boolean isFromNotification = false;
    private String slug = "",lang;
    ImageView home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main_update);



        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lang = getSharedPreferences(Config.SHAREDPREFERENCE_LANGUAGE, 0).getString(Config.LANG, null);

        pBar = (ProgressBar) findViewById(R.id.pBar);

        if(getIntent().getExtras() != null){
            news = getIntent().getExtras().getParcelable("news");
            slug = getIntent().getExtras().getString("slug");
            isFromNotification = getIntent().getExtras().getBoolean("isFromNotification");
        }
        if(news != null
                && news.getSlug() != null
                && news.getSlug().trim().length() > 0) {
            getNewsDetails(news.getSlug());
        } else if(slug != null && slug.trim().length() > 0) {
            getNewsDetails(slug);
        } else {
            new android.support.v7.app.AlertDialog.Builder(NewsDetailsActivity.this,
                    android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar)
                    .setIcon(R.mipmap.ic_sentiment_dissatisfied)
                    .setTitle("News Details")
                    .setMessage("Some error occured.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }
                    )
                    .setCancelable(false);
        }
        setToolBar();



    }

    /*private void updateView() {

        if (news == null) {
            return;
        }

        final String title = news.getName() + "\n";
        final String posted_by = news.getJounalist_name() + "\n";
        final String posted_at = " " + Util.getTime(news.getApproved_date()) + "\n";
        //String short_description = news.getShort_description() + "\n\n\n\n\n";
        String short_description = " " + Html.
                fromHtml("<br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />");
        Spanned long_description = Html.fromHtml(news.getLong_description().replace("\n","<br />"));

        final TextView tvDemo = (TextView) findViewById(R.id.description_with_img_only);
        //final TextView tvDemo = (TextView) findViewById(R.id.description_text);

        Typeface typeface = Typeface.createFromAsset(getAssets(),
                getResources().getString(R.string.text_font_family_for_news_long_desc));

        SpannableString spanString = new SpannableString(title);
        spanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanString.setSpan(new RelativeSizeSpan(1.8f), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanString.setSpan(new StyleSpan(typeface.getStyle()), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString pbspanString = new SpannableString(posted_by);
        pbspanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, posted_by.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        pbspanString.setSpan(new RelativeSizeSpan(0.9f), 0, posted_by.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        pbspanString.setSpan(new StyleSpan(typeface.getStyle()), 0, posted_by.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString paspanString = new SpannableString(posted_at);
        paspanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, posted_at.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        paspanString.setSpan(new RelativeSizeSpan(0.9f), 0, posted_at.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        paspanString.setSpan(new StyleSpan(typeface.getStyle()), 0, posted_at.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spanStringSd = new SpannableString(short_description);
        spanStringSd.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
                short_description.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStringSd.setSpan(new RelativeSizeSpan(1.1f), 0,
                short_description.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStringSd.setSpan(new StyleSpan(typeface.getStyle()), 0,
                short_description.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spanStringld = new SpannableString(long_description);
        spanStringld.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
                long_description.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStringld.setSpan(new RelativeSizeSpan(1.2f), 0,
                long_description.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStringld.setSpan(new StyleSpan(typeface.getStyle()), 0,
                long_description.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        final CharSequence text = TextUtils.concat(spanString, pbspanString, paspanString, spanStringSd ,spanStringld);
        final List<NewsDetailsForFlipModel> newsDetailsForFlipModels =
                new ArrayList<NewsDetailsForFlipModel>();

        tvDemo.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Removing layout listener to avoid multiple calls
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    tvDemo.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    tvDemo.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                Pagination pagination = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    pagination = new Pagination(text,
                            tvDemo.getWidth(),
                            tvDemo.getHeight(),
                            tvDemo.getPaint(),
                            tvDemo.getLineSpacingMultiplier(),
                            tvDemo.getLineSpacingExtra(),
                            tvDemo.getIncludeFontPadding());
                }

                for(int i = 0; i < pagination.size(); i++){
                        NewsDetailsForFlipModel newsDetailsForFlipModel = new NewsDetailsForFlipModel(Parcel.obtain());
                        newsDetailsForFlipModel.setText_to_show(pagination.get(i).toString());
                        newsDetailsForFlipModel.setImg_path(news.getImage());
                        newsDetailsForFlipModels.add(newsDetailsForFlipModel);
                    }

                    String postedAt = " " + Util.getTime(news.getApproved_date());
                    String postedBy = news.getJounalist_name();

                    List<FImageListItem> fImageListItems = new ArrayList<FImageListItem>();
                    if(news.getVideoList() != null && news.getVideoList().size() > 0) {
                        for (int i = 0; i < news.getVideoList().size(); i++) {
                            NewsVideo newsVideo = news.getVideoList().get(i);
                            if(newsVideo.getFile_link() != null
                                    && newsVideo.getFile_link().trim().length() > 0) {
                                FImageListItem imageListItem = new FImageListItem(Parcel.obtain());
                                imageListItem.setName(newsVideo.getName());
                                imageListItem.setIs_video("1");
                                imageListItem.setFile_path(newsVideo.getFile_link());
                                fImageListItems.add(imageListItem);
                            }
                        }
                    }
                    if(news.getImgList() != null && news.getImgList().size() > 0) {
                        for (int i = 0; i < news.getImgList().size(); i++) {
                            NewsImage newsImage = news.getImgList().get(i);
                            if(newsImage.getImage_link() != null
                                    && newsImage.getImage_link().trim().length() > 0) {
                                FImageListItem imageListItem = new FImageListItem(Parcel.obtain());
                                imageListItem.setName(newsImage.getName());
                                imageListItem.setIs_image("1");
                                imageListItem.setFile_path(newsImage.getImage_link());
                                fImageListItems.add(imageListItem);
                            }
                        }
                    }

                    if(news.getIs_video() != null && news.getIs_video().equalsIgnoreCase("1")
                            && news.getFile_path() != null && news.getFile_path().trim().length() > 0){
                        FImageListItem imageListItem = new FImageListItem(Parcel.obtain());
                        imageListItem.setName(news.getName());
                        imageListItem.setIs_video("1");
                        imageListItem.setFile_path(news.getFile_path());
                        fImageListItems.add(imageListItem);
                    } else {
                        if(news.getImage() != null && news.getImage().trim().length() > 0){
                            FImageListItem imageListItem = new FImageListItem(Parcel.obtain());
                            imageListItem.setName(news.getName());
                            imageListItem.setFile_path(news.getImage());
                            imageListItem.setIs_video("0");
                            fImageListItems.add(imageListItem);
                        }

                        if(news.getFile_path() != null && news.getFile_path().trim().length() > 0){
                            FImageListItem imageListItem22 = new FImageListItem(Parcel.obtain());
                            imageListItem22.setName(news.getName()+".");
                            imageListItem22.setIs_image("1");
                            imageListItem22.setFile_path(news.getFile_path());
                            fImageListItems.add(imageListItem22);
                        }
                    }

                    RelativeLayout layoutParent = (RelativeLayout) findViewById(R.id.layoutParent);
                    flipView = new FlipViewController(NewsDetailsActivity.this);
                    //Use RGB_565 can reduce peak memory usage on large screen device,
                    // but it's up to you to choose the best bitmap format
                    flipView.setAnimationBitmapFormat(Bitmap.Config.RGB_565);
                    flipView.setOverFlipEnabled(false);
                    flipView.setAdapter(new FlipAdapter(NewsDetailsActivity.this, newsDetailsForFlipModels,
                            title, postedAt, postedBy, fImageListItems,
                            NewsDetailsActivity.this, NewsDetailsActivity.this, news, mFBCount, mTwitterCount, mGPlusCount));
                    layoutParent.addView(flipView);
            }
        });
    }*/

    private int mFBCount = 0;
    private int mTwitterCount = 0;
    private int mGPlusCount = 0;

    private void getNewsDetails(final String slug){
        if(Util.getNetworkConnectivityStatus(NewsDetailsActivity.this) == false){
            Util.showDialogToShutdownApp(NewsDetailsActivity.this);
            return;
        }
        new AsyncTask<Void, Void, Boolean>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(pBar != null) {
                    pBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void onPostExecute(Boolean aVoid) {
                super.onPostExecute(aVoid);
                if(pBar != null) {
                    pBar.setVisibility(View.GONE);
                }
                if(aVoid != null && aVoid.booleanValue() == true) {
                   // updateView();
                }
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                InputStream in = null;
                int resCode = -1;

                try {
                    String link = null;
                    if(lang==null || lang.contentEquals("English")) {
                      link =  NEWS_DETAILS_API_BASE_URL + slug+ Config.EN_CONENT;

                    }
                    else{
                        link =  NEWS_DETAILS_API_BASE_URL + slug+ Config.OD_CONENT;

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
                    * "News":
                    * {"id":"10",
                        * "name":"ICC to investigate Burundi political violence",
                        * "slug":"icc-to-investigate-burundi-political-violence",
                        * "cat_id":"1",
                        * "sub_cat_id":"0",
                        * "is_hot":"1",
                        * "featured_image":"2474974581461663657896622811-121212.jpg",
                        * "short_description":"UN estimate says at least 430 people killed in the past year after President Nkurinziza launched election campaign...\r\n\r\n\r\n\r\n",
                        * "long_description":"**Gambela**, Ethiopia - Three-year-old Nyakuar sleeps, but restlessly, at a hospital in South Sudan.\r\n\r\nShe is from Ethiopia and last week she witnessed things nobody her age, nobody at all, should have to.\r\n\r\nHer village, and several others in the Gambela region of western Ethiopia, which straddles the border between those two countries, was attacked at dawn by heavily-armed men who had crossed the porous border from South Sudan.\r\n\r\nThey were merciless, shooting indiscriminately and burning down people's homes. When they were finished, 208 people were dead and 2,000 head of cattle had been stolen.\r\n\r\nBut, what has shocked this country, is that they also kidnapped more than 100 children.\r\n\r\nA few beds away lies her mother, Nyaruach Wang, asleep and still very weak. Doctors told us they had to sedate her because she was in too much pain. She was shot four times and two of her other children were among those spirited over the border.\r\n\r\n**Ethiopian hope**\r\n\r\nThe raid has set off a wave of revulsion across Ethiopian social media and sparked fears, already held by many, that a civil war raging in South Sudan could spill over the near invisible border.\r\n\r\nThose attacked in Ethiopia were from the Nuer tribe, which is also the second largest tribe in South Sudan.\r\n\r\nEthiopian officials, and witnesses spoken to by Al Jazeera, have blamed the attack on members of the rival Murle community. Gathuak Reath and others told us they recognised their tribal markings.\r\n\r\nThe Ethiopian army has now moved in and the border area has become a militarised zone. The government says that its troops - with the permission of South Sudan's government - have crossed the frontier and some officials say they are closing in on the area where they believe the children are being held.",
                        * "tags":"War & Conflict, Africa, Ethiopia",
                        * "is_video":"1",
                        * "is_image":"0",
                        * "file_path":"71224387914620023271502338763-BanglaCricket.mp4",
                        * "is_draft":"1",
                        * "is_publish":"1",
                        * "user_id":"2",
                        * "is_archive":"0",
                        * "publish_date":"2016-04-20 00:00:00",
                        * "archive_date":null,
                        * "last_save_time":null,
                        * "is_approved":"1",
                        * "approved_by":"1",
                        * "approved_date":"2016-04-30 13:04:27",
                        * "is_enable":"1",
                        * "created_at":"2016-04-20 11:08:46",
                        * "updated_at":"2016-05-02 13:20:36",
                        * "allow_comment":"1",
                        * "position":"1",
                        * "is_featured":"1",
                        * "is_top_story":"0",
                        * "is_viral":"0",
                        * "meta_desc":"rwerwe",
                        * "meta_keywords":"rwer",
                        * "news_count":253
                        * "news_image": [
                        *   {
                                "id": "1",
                                "name": "ICC to Investigate",
                                "image_link": "46348995614619958411922557231-newImage.jpg",
                                "news_id": "10",
                                "position": "0",
                                "is_enable": "1",
                                "is_trash": "0",
                                "updated_at": "2016-04-30 11:27:26",
                                "created_at": "2016-04-30 11:27:26"
                              }
                        * ]
                    * },
                    * "NewsComment":[
                    *
                    * ]
                    * }
                    *
                    * */

                    if(news == null){
                        news = new News(Parcel.obtain());
                    }

                    JSONObject res = new JSONObject(response);
                    if(res.isNull("News") == false){
                        JSONObject jsonObj = res.getJSONObject("News");

                        if(jsonObj.isNull("id") == false){
                            String id = jsonObj.getString("id");
                            news.setId(id);
                        }

                        if(jsonObj.isNull("name") == false){
                            String name = jsonObj.getString("name");
                            news.setName(name);
                        }

                        if(jsonObj.isNull("slug") == false){
                            String slug = jsonObj.getString("slug");
                            news.setSlug(slug);
                        }

                        if(jsonObj.isNull("cat_id") == false){
                            String cat_id = jsonObj.getString("cat_id");
                            news.setCat_id(cat_id);
                        }

                        if(jsonObj.isNull("sub_cat_id") == false){
                            String sub_cat_id = jsonObj.getString("sub_cat_id");
                            news.setSub_cat_id(sub_cat_id);
                        }

                        if(jsonObj.isNull("is_hot") == false){
                            String is_hot = jsonObj.getString("is_hot");
                            news.setIs_hot(is_hot);
                        }

                        if(jsonObj.isNull("featured_image") == false){
                            String featured_image = jsonObj.getString("featured_image");
                            news.setImage(featured_image);
                        }

                        if(jsonObj.isNull("short_description") == false){
                            String short_description = jsonObj.getString("short_description");
                            news.setShort_description(short_description);
                        }

                        if(jsonObj.isNull("long_description") == false){
                            String long_description = jsonObj.getString("long_description").trim();

                            if(long_description != null && long_description.length() > 0
                                    && long_description.contains("**")){
                                String[] arr = long_description.split("\\*\\*");
                                if(arr != null && arr.length >= 3){
                                    String location = arr[1];
                                    String rest = "  "+arr[2];
                                    long_description = "**" + location.trim() + "**" + rest;
                                }
                            }

                            news.setLong_description(long_description);
                        }

                        if(jsonObj.isNull("tags") == false){
                            String tags = jsonObj.getString("tags");
                            news.setTags(tags);
                        }

                        if(jsonObj.isNull("is_video") == false){
                            String is_video = jsonObj.getString("is_video");
                            news.setIs_video(is_video);
                        }

                        if(jsonObj.isNull("is_image") == false){
                            String is_image = jsonObj.getString("is_image");
                            news.setIs_image(is_image);
                        }

                        if(jsonObj.isNull("file_path") == false){
                            String file_path = jsonObj.getString("file_path");
                            news.setFile_path(file_path);
                        }

                        if(jsonObj.isNull("is_draft") == false){
                            String is_draft = jsonObj.getString("is_draft");
                            news.setIs_draft(is_draft);
                        }

                        if(jsonObj.isNull("is_publish") == false){
                            String is_publish = jsonObj.getString("is_publish");
                            news.setIs_publish(is_publish);
                        }

                        if(jsonObj.isNull("user_id") == false){
                            String user_id = jsonObj.getString("user_id");
                            news.setUser_id(user_id);
                        }

                        if(jsonObj.isNull("is_archive") == false){
                            String is_archive = jsonObj.getString("is_archive");
                            news.setIs_archive(is_archive);
                        }

                        if(jsonObj.isNull("publish_date") == false){
                            String publish_date = jsonObj.getString("publish_date");
                            news.setPublish_date(publish_date);
                        }

                        if(jsonObj.isNull("archive_date") == false){
                            String archive_date = jsonObj.getString("archive_date");
                            news.setArchive_date(archive_date);
                        }

                        if(jsonObj.isNull("last_save_time") == false){
                            String last_save_time = jsonObj.getString("last_save_time");
                            news.setLast_save_time(last_save_time);
                        }

                        if(jsonObj.isNull("is_approved") == false){
                            String is_approved = jsonObj.getString("is_approved");
                            news.setIs_approved(is_approved);
                        }

                        if(jsonObj.isNull("approved_by") == false){
                            String approved_by = jsonObj.getString("approved_by");
                            news.setApproved_by(approved_by);
                        }

                        if(jsonObj.isNull("approved_date") == false){
                            String approved_date = jsonObj.getString("approved_date");
                            news.setApproved_date(approved_date);
                        }

                        if(jsonObj.isNull("is_enable") == false){
                            String is_enable = jsonObj.getString("is_enable");
                            news.setIs_enable(is_enable);
                        }

                        if(jsonObj.isNull("created_at") == false){
                            String created_at = jsonObj.getString("created_at");
                            news.setCreated_at(created_at);
                        }

                        if(jsonObj.isNull("updated_at") == false){
                            String updated_at = jsonObj.getString("updated_at");
                            news.setUpdated_at(updated_at);
                        }

                        if(jsonObj.isNull("allow_comment") == false){
                            String allow_comment = jsonObj.getString("allow_comment");
                            news.setAllow_comment(allow_comment);
                        }

                        if(jsonObj.isNull("position") == false){
                            String position = jsonObj.getString("position");
                            news.setPosition(position);
                        }

                        if(jsonObj.isNull("is_featured") == false){
                            String is_featured = jsonObj.getString("is_featured");
                            news.setIs_featured(is_featured);
                        }

                        if(jsonObj.isNull("is_top_story") == false){
                            String is_top_story = jsonObj.getString("is_top_story");
                            news.setIs_top_story(is_top_story);
                        }

                        if(jsonObj.isNull("is_viral") == false){
                            String is_viral = jsonObj.getString("is_viral");
                            news.setIs_viral(is_viral);
                        }

                        if(jsonObj.isNull("meta_desc") == false){
                            String meta_desc = jsonObj.getString("meta_desc");
                            news.setMeta_desc(meta_desc);
                        }

                        if(jsonObj.isNull("meta_keywords") == false){
                            String meta_keywords = jsonObj.getString("meta_keywords");
                            news.setMeta_keywords(meta_keywords);
                        }
                        if(jsonObj.isNull("categoryslug") == false){
                            String categoryslug = jsonObj.getString("categoryslug");
                            news.setCategoryslug(categoryslug);
                        }

                        if(jsonObj.isNull("news_count") == false){
                            int news_count = jsonObj.getInt("news_count");
                            news.setNews_count(news_count);
                        }

                        if(jsonObj.isNull("journalist_name") == false){
                            String journalist_name = jsonObj.getString("journalist_name");
                            news.setJounalist_name(journalist_name);
                        }

                        if(jsonObj.isNull("news_image") == false) {
                            JSONArray array = jsonObj.getJSONArray("news_image");
                            if (array != null && array.length() > 0) {
                                List<NewsImage> newsImageList = new ArrayList<NewsImage>();
                                for (int i = 0; i < array.length(); i++) {
                                    NewsImage image = new NewsImage(Parcel.obtain());
                                    if (array.getJSONObject(i).isNull("id") == false) {
                                        String id = array.getJSONObject(i).getString("id");
                                        image.setId(id);
                                    }

                                    if (array.getJSONObject(i).isNull("name") == false) {
                                        String name = array.getJSONObject(i).getString("name");
                                        image.setName(name);
                                    }

                                    if (array.getJSONObject(i).isNull("image_link") == false) {
                                        String image_link = array.getJSONObject(i).getString("image_link");
                                        image.setImage_link(image_link);
                                    }

                                    if (array.getJSONObject(i).isNull("news_id") == false) {
                                        String news_id = array.getJSONObject(i).getString("news_id");
                                        image.setNews_id(news_id);
                                    }

                                    if (array.getJSONObject(i).isNull("position") == false) {
                                        String position = array.getJSONObject(i).getString("position");
                                        image.setPosition(position);
                                    }

                                    if (array.getJSONObject(i).isNull("is_enable") == false) {
                                        String is_enable = array.getJSONObject(i).getString("is_enable");
                                        image.setIs_enable(is_enable);
                                    }

                                    if (array.getJSONObject(i).isNull("is_trash") == false) {
                                        String is_trash = array.getJSONObject(i).getString("is_trash");
                                        image.setIs_trash(is_trash);
                                    }

                                    if (array.getJSONObject(i).isNull("updated_at") == false) {
                                        String updated_at = array.getJSONObject(i).getString("updated_at");
                                        image.setUpdated_at(updated_at);
                                    }

                                    if (array.getJSONObject(i).isNull("created_at") == false) {
                                        String created_at = array.getJSONObject(i).getString("created_at");
                                        image.setCreated_at(created_at);
                                    }
                                    if(image != null && image.getIs_enable() != null
                                            && image.getIs_enable().trim().length() > 0
                                            && image.getIs_enable().equalsIgnoreCase("1")
                                            && image.getIs_trash() != null
                                            && image.getIs_trash().trim().length() > 0
                                            && image.getIs_trash().equalsIgnoreCase("0")) {
                                        newsImageList.add(image);
                                    }
                                }
                                news.setImgList(newsImageList);
                            }
                        }

                        if(jsonObj.isNull("news_video") == false) {
                            JSONArray array = jsonObj.getJSONArray("news_video");
                            if (array != null && array.length() > 0) {
                                List<NewsVideo> newsVideoList = new ArrayList<NewsVideo>();
                                for (int i = 0; i < array.length(); i++) {
                                    NewsVideo video = new NewsVideo(Parcel.obtain());
                                    if (array.getJSONObject(i).isNull("id") == false) {
                                        String id = array.getJSONObject(i).getString("id");
                                        video.setId(id);
                                    }

                                    if (array.getJSONObject(i).isNull("name") == false) {
                                        String name = array.getJSONObject(i).getString("name");
                                        video.setName(name);
                                    }

                                    if (array.getJSONObject(i).isNull("file_link") == false) {
                                        String image_link = array.getJSONObject(i).getString("file_link");
                                        video.setFile_link(image_link);
                                    }

                                    if (array.getJSONObject(i).isNull("news_id") == false) {
                                        String news_id = array.getJSONObject(i).getString("news_id");
                                        video.setNews_id(news_id);
                                    }

                                    if (array.getJSONObject(i).isNull("position") == false) {
                                        String position = array.getJSONObject(i).getString("position");
                                        video.setPosition(position);
                                    }

                                    if (array.getJSONObject(i).isNull("is_enable") == false) {
                                        String is_enable = array.getJSONObject(i).getString("is_enable");
                                        video.setIs_enable(is_enable);
                                    }

                                    if (array.getJSONObject(i).isNull("is_trash") == false) {
                                        String is_trash = array.getJSONObject(i).getString("is_trash");
                                        video.setIs_trash(is_trash);
                                    }

                                    if (array.getJSONObject(i).isNull("updated_at") == false) {
                                        String updated_at = array.getJSONObject(i).getString("updated_at");
                                        video.setUpdated_at(updated_at);
                                    }

                                    if (array.getJSONObject(i).isNull("created_at") == false) {
                                        String created_at = array.getJSONObject(i).getString("created_at");
                                        video.setCreated_at(created_at);
                                    }
                                    if(video != null && video.getIs_enable() != null
                                            && video.getIs_enable().trim().length() > 0
                                            && video.getIs_enable().equalsIgnoreCase("1")
                                            && video.getIs_trash() != null
                                            && video.getIs_trash().trim().length() > 0
                                            && video.getIs_trash().equalsIgnoreCase("0")) {
                                        newsVideoList.add(video);
                                    }
                                }
                                news.setVideoList(newsVideoList);
                            }
                        }
                    }
                    if(res.isNull("NewsComment") == false){
                        JSONArray array = res.getJSONArray("NewsComment");
                        if(array != null && array.length() > 0){
                            List<Comment> comments = new ArrayList<Comment>();
                            for(int i = 0 ; i < array.length(); i ++){
                                Comment comment = new Comment(Parcel.obtain());
                                if(array.getJSONObject(i).isNull("name") == false){
                                    String name = array.getJSONObject(i).getString("name");
                                    comment.setName(name);
                                }
                                if(array.getJSONObject(i).isNull("comment") == false){
                                    String _comment = array.getJSONObject(i).getString("comment");
                                    comment.setComment(_comment);
                                }
                                if(array.getJSONObject(i).isNull("user_id") == false){
                                    String user_id = array.getJSONObject(i).getString("user_id");
                                    comment.setUser_id(user_id);
                                }
                                if(array.getJSONObject(i).isNull("verified_date") == false){
                                    String verified_date = array.getJSONObject(i).getString("verified_date");
                                    comment.setVerified_date(verified_date);
                                }
                                if(array.getJSONObject(i).isNull("file_path") == false){
                                    String file_path = array.getJSONObject(i).getString("file_path");
                                    comment.setFile_path(file_path);
                                }
                                if(array.getJSONObject(i).isNull("is_image") == false){
                                    String is_image = array.getJSONObject(i).getString("is_image");
                                    comment.setIs_image(is_image);
                                }
                                if(array.getJSONObject(i).isNull("is_video") == false){
                                    String is_video = array.getJSONObject(i).getString("is_video");
                                    comment.setIs_video(is_video);
                                }
                                if(array.getJSONObject(i).isNull("is_audio") == false){
                                    String is_audio = array.getJSONObject(i).getString("is_audio");
                                    comment.setIs_audio(is_audio);
                                }
                                comments.add(comment);
                            }
                            news.setCommentList(comments);
                        }
                    }

                    if(res.isNull("ReleatedNews") == false) {
                        JSONArray array = res.getJSONArray("ReleatedNews");
                        if (array != null && array.length() > 0) {
                            List<News> relatedNews = new ArrayList<News>();
                            for (int i = 0; i < array.length(); i++) {
                                News related_news = new News(Parcel.obtain());
                                if (array.getJSONObject(i).isNull("name") == false) {
                                    String name = array.getJSONObject(i).getString("name");
                                    related_news.setName(name);
                                }
                                if (array.getJSONObject(i).isNull("id") == false) {
                                    String id = array.getJSONObject(i).getString("id");
                                    related_news.setId(id);
                                }
                                if (array.getJSONObject(i).isNull("slug") == false) {
                                    String slug = array.getJSONObject(i).getString("slug");
                                    related_news.setSlug(slug);
                                }
                                if (array.getJSONObject(i).isNull("tags") == false) {
                                    String tags = array.getJSONObject(i).getString("tags");
                                    related_news.setTags(tags);
                                }
                                if (array.getJSONObject(i).isNull("short_description") == false) {
                                    String short_description = array.getJSONObject(i).getString("short_description");
                                    related_news.setShort_description(short_description);
                                }
                                if (array.getJSONObject(i).isNull("is_trash") == false) {
                                    String is_trash = array.getJSONObject(i).getString("is_trash");
                                    related_news.setIs_trash(is_trash);
                                }
                                if (array.getJSONObject(i).isNull("featured_image") == false) {
                                    String featured_image = array.getJSONObject(i).getString("featured_image");
                                    related_news.setFile_path(featured_image);
                                }
                                if(related_news != null && related_news.getIs_enable() != null
                                        && related_news.getIs_enable().trim().length() > 0
                                        && related_news.getIs_enable().equalsIgnoreCase("1")
                                        && related_news.getIs_trash() != null
                                        && related_news.getIs_trash().trim().length() > 0
                                        && related_news.getIs_trash().equalsIgnoreCase("0")) {
                                    relatedNews.add(related_news);
                                }
                            }
                            news.setRelatedNewsList(relatedNews);
                        }
                    }
                    return true;
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
                return false;
            }
        }.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(flipView != null) {
            flipView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(flipView != null) {
            flipView.onPause();
        }
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
        home=(ImageView)findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsDetailsActivity.this,HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


                /*
                *
                * FLAG_ACTIVITY_CLEAR_TOP
FLAG_ACTIVITY_SINGLE_TOP
FLAG_ACTIVITY_CLEAR_TASK
FLAG_ACTIVITY_NEW_TASK
                * */
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void shareInTwitter(String link){
        //initSocialAdapter(link);
       // Config.getTwitterIntent(NewsDetailsActivity.this,link);
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ommcom News");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, link);

        final PackageManager pm = NewsDetailsActivity.this.getPackageManager();
        final List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
        for (final ResolveInfo app : activityList) {
            if ("com.twitter.composer.ComposerActivity".equals(app.activityInfo.name)) {
                final ActivityInfo activity = app.activityInfo;
                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                shareIntent.setComponent(name);
                NewsDetailsActivity.this.startActivity(shareIntent);
                break;
            }
        }
    }

    private SocialAuthAdapter socialAuthAdapter;
    private ProgressDialog pd;

    private void initSocialAdapter(String link) {
        callbackManager = null;
        // Utils.isOnline method check the internet connection
        if (Utils.isOnline(getApplicationContext())) {
            // Initialize the socialAuthAdapter with ResponseListener
            pd = ProgressDialog.show(NewsDetailsActivity.this, null, null);
            socialAuthAdapter = new SocialAuthAdapter(new ResponseListener(
                    null,
                    link));
            // Add Twitter to set as provider to post on twitter
            socialAuthAdapter.addProvider(SocialAuthAdapter.Provider.TWITTER, R.drawable.twitter);
            // this line is for Authorize start
            socialAuthAdapter.authorize(NewsDetailsActivity.this, SocialAuthAdapter.Provider.TWITTER);
        } else {
            // showing message when internet connection is not available
            Toast.makeText(getApplicationContext(),
                    "Check your internet connection..", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void initSocialAdapterForGooglePlus(String title, String link) {

        callbackManager = null;
        Toast.makeText(this, "start share process", Toast.LENGTH_SHORT).show();
        Intent shareIntent = new PlusShare.Builder(NewsDetailsActivity.this)
                .setType("text/plain")
                .setText(title)
                .setContentUrl(Uri.parse(link))
                .getIntent();
        startActivityForResult(shareIntent, 0);

//        if (Utils.isOnline(getApplicationContext())) {
//            pd = ProgressDialog.show(NewsDetailsActivity.this, null, null);
//            socialAuthAdapter = new SocialAuthAdapter(new ResponseListener(
//                    null,
//                    link));
//            socialAuthAdapter.addProvider(SocialAuthAdapter.Provider.GOOGLEPLUS, R.drawable.googleplus);
//            socialAuthAdapter.authorize(NewsDetailsActivity.this, SocialAuthAdapter.Provider.GOOGLEPLUS);
//        } else {
//            Toast.makeText(getApplicationContext(),
//                    "Check your internet connection..", Toast.LENGTH_LONG)
//                    .show();
//        }
    }

    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private void initSocialAdapterForFacebook(String link, String description, String title) {
        // Utils.isOnline method check the internet connection
        /*if (Utils.isOnline(getApplicationContext())) {
            // Initialize the socialAuthAdapter with ResponseListener
            pd = ProgressDialog.show(NewsDetailsActivity.this, null, null);
            socialAuthAdapter = new SocialAuthAdapter(new ResponseListener(
                    null,
                    link));
            // Add Twitter to set as provider to post on twitter
            socialAuthAdapter.addProvider(SocialAuthAdapter.Provider.FACEBOOK, R.drawable.facebook);
            // this line is for Authorize start
            socialAuthAdapter.authorize(NewsDetailsActivity.this, SocialAuthAdapter.Provider.FACEBOOK);
        } else {
            // showing message when internet connection is not available
            Toast.makeText(getApplicationContext(),
                    "Check your internet connection..", Toast.LENGTH_LONG)
                    .show();
        }*/
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(title)
                    .setContentDescription(description)
                    .setContentUrl(Uri.parse(link))
                    .build();

            shareDialog.show(linkContent);
        }
    }

    // this ResponseListener class is for getting responce of post uploading
    private class ResponseListener implements DialogListener {
        Bitmap bitmap;
        String message;

        public ResponseListener(Bitmap bitmap, String message) {
            this.bitmap = bitmap;
            this.message = message;
        }

        @Override
        public void onComplete(final Bundle values) {
            // this method is call when successfull authorization is done
            try {
                //socialAuthAdapter.uploadImageAsync(message, "The AppGuruz.png",
                //		bitmap, 100, new UploadImageListener());
                socialAuthAdapter.updateStatus(message, new UploadImageListener(),true);
                //socialAuthAdapter.getUserProfileAsync(new ProfileDataListener());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(SocialAuthError error) {
            // this method is call when error is occured in authorization
            if (pd != null && pd.isShowing())
                pd.dismiss();
            Log.d("ShareTwitter", "Authentication Error: " + error.getMessage());
        }

        @Override
        public void onCancel() {
            // this method is call when user cancel Authentication
            if (pd != null && pd.isShowing())
                pd.dismiss();
            Log.d("ShareTwitter", "Authentication Cancelled");
        }

        @Override
        public void onBack() {
            // this method is call when user backpressed from dialog
            if (pd != null && pd.isShowing())
                pd.dismiss();
            Log.d("ShareTwitter", "Dialog Closed by pressing Back Key");
        }
    }

    private final class UploadImageListener implements
            SocialAuthListener<Integer> {

        @Override
        public void onError(SocialAuthError e) {
        }

        @Override
        public void onExecute(String arg0, Integer arg1) {
            Integer status = arg1;
            try {
                if (status.intValue() == 200 || status.intValue() == 201
                        || status.intValue() == 204) {
                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                    Toast.makeText(NewsDetailsActivity.this, "Image Uploaded",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                    Toast.makeText(NewsDetailsActivity.this, "Image not Uploaded",
                            Toast.LENGTH_SHORT).show();
                }

            } catch (NullPointerException e) {
                if (pd != null && pd.isShowing())
                    pd.dismiss();
                Toast.makeText(NewsDetailsActivity.this, "Image not Uploaded",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onClickWhatsApp(String link_share) {

        PackageManager pm=getPackageManager();
        try {
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");

            PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            waIntent.putExtra(Intent.EXTRA_SUBJECT, "OMMCOM");
            waIntent.putExtra(Intent.EXTRA_TEXT, link_share);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void showAlertForShare(final String link, final String description, final String title) {
       /* CharSequence colors[] = new CharSequence[]{"Facebook", "Twitter", "Google Plus", "Whats App", "Others"};

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder
                (this);
        //(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
        builder.setTitle("Choose one");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                switch (which) {
                    case 0:
                        initSocialAdapterForFacebook(link, description, title);
                        break;
                    case 1:
                        shareInTwitter(link);
                        break;
                    case 2:
                        initSocialAdapterForGooglePlus(title, link);
                        break;
                    case 3:
                        onClickWhatsApp(link);
                        break;
                    case 4:
                        if (news == null || news.getSlug() == null || news.getSlug().trim().length() <= 0) {
                            return;
                        }
                        if (link == null || link.trim().length() <= 0) {
                            return;
                        }
                        String link_share = news.getShort_description() + "\n" + link;
                        if (link_share == null || link_share.trim().length() <= 0) {
                            return;
                        }
                        String img_url = Util.getImageFilePathForNews(news, null);
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        share.putExtra(Intent.EXTRA_SUBJECT, "OMMCOM");
                        share.putExtra(Intent.EXTRA_TEXT, link_share);
                        startActivity(Intent.createChooser(share, "Share link!"));
                        break;
                    default:
                        break;
                }
            }
        });
        builder.show();*/


        final Item[] items = {
                new Item("Facebook", R.mipmap.facebook),
                new Item("Twitter", R.mipmap.twitter),
                new Item("Google Plus", R.mipmap.google_plus),
                new Item("Whats App", R.mipmap.whatsup),
                new Item("Others", R.mipmap.other)
        };

        ListAdapter adapter = new ArrayAdapter<Item>(
                this,
                android.R.layout.select_dialog_item,
                android.R.id.text1,
                items){
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView)v.findViewById(android.R.id.text1);

                tv.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0);

                int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                tv.setCompoundDrawablePadding(dp5);

                return v;
            }
        };


        new AlertDialog.Builder(this)
                .setTitle("Share Using")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                initSocialAdapterForFacebook(link, description, title);
                                break;
                            case 1:
                                shareInTwitter(link);
                                break;
                            case 2:
                                initSocialAdapterForGooglePlus(title, link);
                                break;
                            case 3:
                                onClickWhatsApp(link);
                                break;
                            case 4:
                                if (news == null || news.getSlug() == null || news.getSlug().trim().length() <= 0) {
                                    return;
                                }
                                if (link == null || link.trim().length() <= 0) {
                                    return;
                                }
                                String link_share = news.getName()+ "\n" + news.getShort_description() + "\n" + link;
                                if (link_share == null || link_share.trim().length() <= 0) {
                                    return;
                                }
                                String img_url = Util.getImageFilePathForNews(news, null);
                                Intent share = new Intent(Intent.ACTION_SEND);
                                share.setType("text/plain");
                                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                                share.putExtra(Intent.EXTRA_SUBJECT, "OMMCOM");
                                share.putExtra(Intent.EXTRA_TEXT, link_share);
                                startActivity(Intent.createChooser(share, "Share link!"));
                                break;
                            default:
                                break;
                        }
                    }
                }).show();
    }

    @Override
    public void onShareClick(News news) {
        if(news != null) {
            if(lang==null || lang.contentEquals("English")) {

                String link = Config.DOMAIN + "/" + news.getCategoryslug() + "/" + news.getSlug();
                String description = news.getShort_description();
                String title = news.getName();
              // we will use later
              //  showAlertForShare(link, description, title);

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                share.putExtra(Intent.EXTRA_SUBJECT, "OMMCOM");
                share.putExtra(Intent.EXTRA_TEXT, link);
                startActivity(Intent.createChooser(share, "Share link!"));

            }
            else {
                String link = Config.DOMAIN + "/" + news.getCategoryslug() + "/" + news.getSlug()+ "/odia";
                String description = news.getShort_description();
                String title = news.getName();


                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                share.putExtra(Intent.EXTRA_SUBJECT, "OMMCOM");
                share.putExtra(Intent.EXTRA_TEXT, link);
                startActivity(Intent.createChooser(share, "Share link!"));
                //showAlertForShare(link, description, title);
            }
        }
    }

    @Override
    public void onCommentClick() {
            Intent intent = new Intent(NewsDetailsActivity.this, CommentListActivity.class);
            intent.putExtra("news", news);
            startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isFromNotification){
            Intent intent = new Intent(NewsDetailsActivity.this, HomeActivity.class);
            intent.putExtra("isFromNotification", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}
