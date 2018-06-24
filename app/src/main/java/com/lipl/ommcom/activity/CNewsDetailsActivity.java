package com.lipl.ommcom.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.plus.PlusShare;
import com.lipl.ommcom.R;
import com.lipl.ommcom.adapter.CJFlipAdapter;
import com.lipl.ommcom.adapter.FlipAdapter;
import com.lipl.ommcom.fliputil.FlipViewController;
import com.lipl.ommcom.pojo.CitizenJournalistVideos;
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

public class CNewsDetailsActivity extends AppCompatActivity implements CJFlipAdapter.OnShareClick,
        CJFlipAdapter.OnCommentClick {

    private FlipViewController flipView;
    private static final String NEWS_DETAILS_API_BASE_URL =
            Config.API_BASE_URL + Config.NEWS_DETAILS_API + Config.FOLDER_SLUG;
    private ProgressBar pBar = null;
    private static final String TAG = "NewsDetailActivity";
    private CitizenJournalistVideos news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pBar = (ProgressBar) findViewById(R.id.pBar);

        if(getIntent().getExtras() != null){
            news = getIntent().getExtras().getParcelable("news");
        }
        if(news != null
                && news.getSlug() != null
                && news.getSlug().trim().length() > 0) {
            getNewsDetails(news.getSlug());
        }
        setToolBar();
    }

    private void updateView() {

        if (news == null) {
            return;
        }

        final String title = news.getDescription() + "\n";
        final String posted_by = news.getName() + "\n";
        final String posted_at = " " + Util.getTime(news.getUpdated_at()) + "\n";
        String short_description = " " + "\n\n\n\n\n";
        Spanned long_description = Html.fromHtml(news.getLong_description().replace("\n","<br />"));

        final TextView tvDemo = (TextView) findViewById(R.id.description_with_img_only);

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
        spanStringSd.setSpan(new RelativeSizeSpan(1.2f), 0,
                short_description.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStringSd.setSpan(new StyleSpan(typeface.getStyle()), 0,
                short_description.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spanStringld = new SpannableString(long_description);
        spanStringld.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
                long_description.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStringld.setSpan(new RelativeSizeSpan(1.0f), 0,
                long_description.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStringld.setSpan(new StyleSpan(typeface.getStyle()), 0,
                long_description.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        final CharSequence text = TextUtils.concat(spanString, pbspanString, paspanString, spanStringSd, spanStringld);
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
                Pagination pagination = new Pagination(text,
                        tvDemo.getWidth(),
                        tvDemo.getHeight(),
                        tvDemo.getPaint(),
                        tvDemo.getLineSpacingMultiplier(),
                        tvDemo.getLineSpacingExtra(),
                        tvDemo.getIncludeFontPadding());

                NewsDetailsForFlipModel newsDetailsForFlipModel = new NewsDetailsForFlipModel(Parcel.obtain());
                newsDetailsForFlipModel.setText_to_show(pagination.get(0).toString());
                newsDetailsForFlipModel.setImg_path(news.getFile_path());
                newsDetailsForFlipModels.add(newsDetailsForFlipModel);

                final List<FImageListItem> imageListItems = new ArrayList<FImageListItem>();
                FImageListItem imageListItem22 = new FImageListItem(Parcel.obtain());
                imageListItem22.setName(news.getName()+".");
                if(news != null && news.getFile_type() != null && news.getFile_type().trim().length() > 0
                        && news.getFile_type().trim().equalsIgnoreCase("Video")){
                    imageListItem22.setIs_video("1");
                } else if(news != null && news.getFile_type() != null && news.getFile_type().trim().length() > 0
                        && news.getFile_type().trim().equalsIgnoreCase("Audio")){
                    imageListItem22.setIs_audio("1");
                } else {
                    imageListItem22.setIs_image("1");
                    imageListItem22.setIs_video("0");
                    imageListItem22.setIs_audio("0");
                }
                imageListItem22.setFile_path(news.getFile_path());
                imageListItems.add(imageListItem22);

                final String rest_text = text.toString().replace(pagination.get(0), "");
                if(rest_text != null && rest_text.trim().length() > 0){
                    final TextView tvDemoFull = (TextView) findViewById(R.id.description_full);
                    tvDemoFull.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            // Removing layout listener to avoid multiple calls
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                tvDemoFull.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            } else {
                                tvDemoFull.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                            Pagination pagination = new Pagination(rest_text,
                                    tvDemoFull.getWidth(),
                                    tvDemoFull.getHeight(),
                                    tvDemoFull.getPaint(),
                                    tvDemoFull.getLineSpacingMultiplier(),
                                    tvDemoFull.getLineSpacingExtra(),
                                    tvDemoFull.getIncludeFontPadding());

                            for(int i = 0; i < pagination.size(); i++){
                                NewsDetailsForFlipModel newsDetailsForFlipModel = new NewsDetailsForFlipModel(Parcel.obtain());
                                newsDetailsForFlipModel.setText_to_show(pagination.get(i).toString());
                                newsDetailsForFlipModel.setImg_path(news.getFile_path());
                                newsDetailsForFlipModels.add(newsDetailsForFlipModel);
                            }

                            String postedAt = " " + Util.getTime(news.getUpdated_at());
                            String postedBy = news.getName();

                            RelativeLayout layoutParent = (RelativeLayout) findViewById(R.id.layoutParent);
                            flipView = new FlipViewController(CNewsDetailsActivity.this);
                            //Use RGB_565 can reduce peak memory usage on large screen device,
                            // but it's up to you to choose the best bitmap format
                            flipView.setAnimationBitmapFormat(Bitmap.Config.RGB_565);
                            flipView.setOverFlipEnabled(false);
                            flipView.setAdapter(new CJFlipAdapter(CNewsDetailsActivity.this, newsDetailsForFlipModels,
                                    title, postedAt, postedBy, imageListItems,
                                    CNewsDetailsActivity.this, CNewsDetailsActivity.this, news, mFBCount,
                                    mTwitterCount, mGPlusCount));
                            layoutParent.addView(flipView);
                        }
                    });
                }

                String postedAt = " " + Util.getTime(news.getUpdated_at());
                String postedBy = news.getName();

                List<FImageListItem> fImageListItems = new ArrayList<FImageListItem>();
                FImageListItem imageListItem = new FImageListItem(Parcel.obtain());
                imageListItem.setName(news.getName()+".");
                if(news != null && news.getFile_type() != null && news.getFile_type().trim().length() > 0
                        && news.getFile_type().trim().equalsIgnoreCase("Video")){
                    imageListItem.setIs_video("1");
                } else if(news != null && news.getFile_type() != null && news.getFile_type().trim().length() > 0
                        && news.getFile_type().trim().equalsIgnoreCase("Audio")){
                    imageListItem.setIs_audio("1");
                } else {
                    imageListItem.setIs_image("1");
                    imageListItem.setIs_video("0");
                    imageListItem.setIs_audio("0");
                }
                imageListItem.setFile_path(news.getFile_path());
                fImageListItems.add(imageListItem);
                RelativeLayout layoutParent = (RelativeLayout) findViewById(R.id.layoutParent);
                flipView = new FlipViewController(CNewsDetailsActivity.this);
                //Use RGB_565 can reduce peak memory usage on large screen device,
                // but it's up to you to choose the best bitmap format
                flipView.setAnimationBitmapFormat(Bitmap.Config.RGB_565);
                flipView.setOverFlipEnabled(false);
                flipView.setAdapter(new CJFlipAdapter(CNewsDetailsActivity.this, newsDetailsForFlipModels,
                        title, postedAt, postedBy, fImageListItems,
                        CNewsDetailsActivity.this, CNewsDetailsActivity.this, news, mFBCount, mTwitterCount, mGPlusCount));
                layoutParent.addView(flipView);
            }
        });
    }

    private int mFBCount = 0;
    private int mTwitterCount = 0;
    private int mGPlusCount = 0;

    private void getNewsDetails(final String slug){
        if(Util.getNetworkConnectivityStatus(CNewsDetailsActivity.this) == false){
            Util.showDialogToShutdownApp(CNewsDetailsActivity.this);
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
                    updateView();
                }
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                InputStream in = null;
                int resCode = -1;

                try {
                    String link = Config.API_CITIZEN_DETAILS_DETAILS + slug;

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

                    if(news == null){
                        news = new CitizenJournalistVideos(Parcel.obtain());
                    }

                    if(response != null) {
                        JSONObject _data = new JSONObject(response);

                        if(_data.isNull("id") == false){
                            String id = _data.getString("id");
                            news.setId(id);
                        }
                        if(_data.isNull("name") == false){
                            String name = _data.getString("name");
                            news.setName(name);
                        }
                        if(_data.isNull("file_path") == false){
                            String file_path = _data.getString("file_path");
                            news.setFile_path(file_path);
                        }
                        if(_data.isNull("file_type") == false){
                            String file_type = _data.getString("file_type");
                            news.setFile_type(file_type);
                        }
                        if(_data.isNull("description") == false){
                            String description = _data.getString("description");
                            news.setDescription(description);
                        }
                        if(_data.isNull("slug") == false){
                            String slug = _data.getString("slug");
                            news.setSlug(slug);
                        }
                        if(_data.isNull("is_anonymous") == false){
                            String is_anonymous = _data.getString("is_anonymous");
                            news.setIs_anonymous(is_anonymous);
                        }
                        if(_data.isNull("updated_at") == false){
                            String updated_at = _data.getString("updated_at");
                            news.setUpdated_at(updated_at);
                        }
                        if(_data.isNull("news_count") == false){
                            int news_count = _data.getInt("news_count");
                            news.setNews_count(news_count+"");
                        }
//                        if(_data.isNull("long_description") == false){
//                            String long_description = _data.getString("long_description");
//                            news.setLong_description(long_description);
//                        }

                        if(_data.isNull("long_description") == false){
                            String long_description = _data.getString("long_description").trim();

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

    private CallbackManager callbackManager;
    private ShareDialog shareDialog;

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void shareInTwitter(String link){
        initSocialAdapter(link);
    }

    private SocialAuthAdapter socialAuthAdapter;
    private ProgressDialog pd;

    private void initSocialAdapter(String link) {
        // Utils.isOnline method check the internet connection
        callbackManager = null;
        if (Utils.isOnline(getApplicationContext())) {
            // Initialize the socialAuthAdapter with ResponseListener
            pd = ProgressDialog.show(CNewsDetailsActivity.this, null, null);
            socialAuthAdapter = new SocialAuthAdapter(new ResponseListener(
                    null,
                    link));
            // Add Twitter to set as provider to post on twitter
            socialAuthAdapter.addProvider(SocialAuthAdapter.Provider.TWITTER, R.drawable.twitter);
            // this line is for Authorize start
            socialAuthAdapter.authorize(CNewsDetailsActivity.this, SocialAuthAdapter.Provider.TWITTER);
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
            Intent shareIntent = new PlusShare.Builder(CNewsDetailsActivity.this)
                    .setType("text/plain")
                    .setText(title)
                    .setContentUrl(Uri.parse(link))
                    .getIntent();
            startActivityForResult(shareIntent, 0);


//        if (Utils.isOnline(getApplicationContext())) {
//            pd = ProgressDialog.show(CNewsDetailsActivity.this, null, null);
//            socialAuthAdapter = new SocialAuthAdapter(new ResponseListener(
//                    null,
//                    link));
//            socialAuthAdapter.addProvider(SocialAuthAdapter.Provider.GOOGLEPLUS, R.drawable.googleplus);
//            socialAuthAdapter.authorize(CNewsDetailsActivity.this, SocialAuthAdapter.Provider.GOOGLEPLUS);
//        } else {
//            Toast.makeText(getApplicationContext(),
//                    "Check your internet connection..", Toast.LENGTH_LONG)
//                    .show();
//        }
    }

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
                    Toast.makeText(CNewsDetailsActivity.this, "Image Uploaded",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                    Toast.makeText(CNewsDetailsActivity.this, "Image not Uploaded",
                            Toast.LENGTH_SHORT).show();
                }

            } catch (NullPointerException e) {
                if (pd != null && pd.isShowing())
                    pd.dismiss();
                Toast.makeText(CNewsDetailsActivity.this, "Image not Uploaded",
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
        /*CharSequence colors[] = new CharSequence[]{"Facebook", "Twitter", "Google Plus", "Whats App", "Others"};

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder
                (this);
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
                        String link_share = news.getName() + "\n" + link;
                        if (link_share == null || link_share.trim().length() <= 0) {
                            return;
                        }
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
                                String link_share = news.getName() + "\n" + link;
                                if (link_share == null || link_share.trim().length() <= 0) {
                                    return;
                                }
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
    public void onShareClick(CitizenJournalistVideos news) {
        String link = "";
        if(news != null) {
            link = Config.DOMAIN + "/citizenews/" +news.getSlug();
            String description = news.getDescription();
            String title = news.getName();
            showAlertForShare(link, description, title);
        }
    }

    @Override
    public void onCommentClick() {
//            Intent intent = new Intent(CNewsDetailsActivity.this, CommentListActivity.class);
//            intent.putExtra("news", news);
//            startActivity(intent);
    }
}
