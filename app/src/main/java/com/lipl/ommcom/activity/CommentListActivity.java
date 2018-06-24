package com.lipl.ommcom.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lipl.ommcom.R;
import com.lipl.ommcom.adapter.CommentListAdapter;
import com.lipl.ommcom.pojo.Comment;
import com.lipl.ommcom.pojo.News;
import com.lipl.ommcom.pojo.NewsImage;
import com.lipl.ommcom.pojo.NewsVideo;
import com.lipl.ommcom.util.Config;
import com.lipl.ommcom.util.CustomTextView;
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
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class CommentListActivity extends AppCompatActivity implements CommentListAdapter.OnItemClickListener {

    private XRecyclerView mRecyclerView;
    //private ListView mRecyclerView;
    private CommentListAdapter mAdapter;
    private List<Comment> listData;
    private News mNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.acticity_comment);

        if(getIntent() != null
                && getIntent().getExtras() != null){
            mNews = getIntent().getExtras().getParcelable("news");
        }

        mRecyclerView = (XRecyclerView) this.findViewById(R.id.recyclerview);
       LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        setCommentList();

        //mRecyclerView.setRefreshing(true);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //refresh data here
                if(mNews != null
                        && mNews.getSlug() != null
                        && mNews.getSlug().trim().length() > 0) {
                    getCommentList(mNews.getSlug().trim());
                }
            }

            @Override
            public void onLoadMore() {
                // load more data here
            }
        });
        setToolBar();

        mRecyclerView.setLoadingMoreEnabled(false);

        CustomTextView tvNoItems = (CustomTextView) findViewById(R.id.tvNoItems);
        if(listData.size() <= 0){
            tvNoItems.setVisibility(View.VISIBLE);
        } else{
            tvNoItems.setVisibility(View.GONE);
        }

        FloatingActionButton fabPostComment = (FloatingActionButton) findViewById(R.id.fabPostComment);
        fabPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNews != null && mNews.getId() != null
                        && mNews.getId().trim().length() > 0) {
                    Intent intent = new Intent(CommentListActivity.this, PostCommentActivity.class);
                    intent.putExtra("news", mNews);
                    startActivity(intent);
                } else {
                    // Show snackbar something went wrong
                    CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinaterlayout);
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Something went wrong.", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });
    }

    private static final String NEWS_DETAILS_API_BASE_URL =
            Config.API_BASE_URL + Config.NEWS_DETAILS_API + Config.FOLDER_SLUG;
    private static final String TAG = "CommentListActivity";

    private void getCommentList(final String slug){
            if(Util.getNetworkConnectivityStatus(CommentListActivity.this) == false){
                Util.showDialogToShutdownApp(CommentListActivity.this);
                return;
            }
            new AsyncTask<Void, Void, Boolean>(){
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected void onPostExecute(Boolean aVoid) {
                    super.onPostExecute(aVoid);
                    mRecyclerView.refreshComplete();
                    setCommentList();
                }

                @Override
                protected Boolean doInBackground(Void... params) {
                    InputStream in = null;
                    int resCode = -1;

                    try {
                        String link = NEWS_DETAILS_API_BASE_URL + slug;

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

                        if(mNews == null){
                            mNews = new News(Parcel.obtain());
                        }

                        JSONObject res = new JSONObject(response);
                        if(res.isNull("News") == false){
                            JSONObject jsonObj = res.getJSONObject("News");

                            if(jsonObj.isNull("id") == false){
                                String id = jsonObj.getString("id");
                                mNews.setId(id);
                            }

                            if(jsonObj.isNull("name") == false){
                                String name = jsonObj.getString("name");
                                mNews.setName(name);
                            }

                            if(jsonObj.isNull("slug") == false){
                                String slug = jsonObj.getString("slug");
                                mNews.setSlug(slug);
                            }

                            if(jsonObj.isNull("cat_id") == false){
                                String cat_id = jsonObj.getString("cat_id");
                                mNews.setCat_id(cat_id);
                            }

                            if(jsonObj.isNull("sub_cat_id") == false){
                                String sub_cat_id = jsonObj.getString("sub_cat_id");
                                mNews.setSub_cat_id(sub_cat_id);
                            }

                            if(jsonObj.isNull("is_hot") == false){
                                String is_hot = jsonObj.getString("is_hot");
                                mNews.setIs_hot(is_hot);
                            }

                            if(jsonObj.isNull("featured_image") == false){
                                String featured_image = jsonObj.getString("featured_image");
                                mNews.setImage(featured_image);
                            }

                            if(jsonObj.isNull("short_description") == false){
                                String short_description = jsonObj.getString("short_description");
                                mNews.setShort_description(short_description);
                            }

                            if(jsonObj.isNull("long_description") == false){
                                String long_description = jsonObj.getString("long_description");
                                mNews.setLong_description(long_description);
                            }

                            if(jsonObj.isNull("tags") == false){
                                String tags = jsonObj.getString("tags");
                                mNews.setTags(tags);
                            }

                            if(jsonObj.isNull("is_video") == false){
                                String is_video = jsonObj.getString("is_video");
                                mNews.setIs_video(is_video);
                            }

                            if(jsonObj.isNull("is_image") == false){
                                String is_image = jsonObj.getString("is_image");
                                mNews.setIs_image(is_image);
                            }

                            if(jsonObj.isNull("file_path") == false){
                                String file_path = jsonObj.getString("file_path");
                                mNews.setFile_path(file_path);
                            }

                            if(jsonObj.isNull("is_draft") == false){
                                String is_draft = jsonObj.getString("is_draft");
                                mNews.setIs_draft(is_draft);
                            }

                            if(jsonObj.isNull("is_publish") == false){
                                String is_publish = jsonObj.getString("is_publish");
                                mNews.setIs_publish(is_publish);
                            }

                            if(jsonObj.isNull("user_id") == false){
                                String user_id = jsonObj.getString("user_id");
                                mNews.setUser_id(user_id);
                            }

                            if(jsonObj.isNull("is_archive") == false){
                                String is_archive = jsonObj.getString("is_archive");
                                mNews.setIs_archive(is_archive);
                            }

                            if(jsonObj.isNull("publish_date") == false){
                                String publish_date = jsonObj.getString("publish_date");
                                mNews.setPublish_date(publish_date);
                            }

                            if(jsonObj.isNull("archive_date") == false){
                                String archive_date = jsonObj.getString("archive_date");
                                mNews.setArchive_date(archive_date);
                            }

                            if(jsonObj.isNull("last_save_time") == false){
                                String last_save_time = jsonObj.getString("last_save_time");
                                mNews.setLast_save_time(last_save_time);
                            }

                            if(jsonObj.isNull("is_approved") == false){
                                String is_approved = jsonObj.getString("is_approved");
                                mNews.setIs_approved(is_approved);
                            }

                            if(jsonObj.isNull("approved_by") == false){
                                String approved_by = jsonObj.getString("approved_by");
                                mNews.setApproved_by(approved_by);
                            }

                            if(jsonObj.isNull("approved_date") == false){
                                String approved_date = jsonObj.getString("approved_date");
                                mNews.setApproved_date(approved_date);
                            }

                            if(jsonObj.isNull("is_enable") == false){
                                String is_enable = jsonObj.getString("is_enable");
                                mNews.setIs_enable(is_enable);
                            }

                            if(jsonObj.isNull("created_at") == false){
                                String created_at = jsonObj.getString("created_at");
                                mNews.setCreated_at(created_at);
                            }

                            if(jsonObj.isNull("updated_at") == false){
                                String updated_at = jsonObj.getString("updated_at");
                                mNews.setUpdated_at(updated_at);
                            }

                            if(jsonObj.isNull("allow_comment") == false){
                                String allow_comment = jsonObj.getString("allow_comment");
                                mNews.setAllow_comment(allow_comment);
                            }

                            if(jsonObj.isNull("position") == false){
                                String position = jsonObj.getString("position");
                                mNews.setPosition(position);
                            }

                            if(jsonObj.isNull("is_featured") == false){
                                String is_featured = jsonObj.getString("is_featured");
                                mNews.setIs_featured(is_featured);
                            }

                            if(jsonObj.isNull("is_top_story") == false){
                                String is_top_story = jsonObj.getString("is_top_story");
                                mNews.setIs_top_story(is_top_story);
                            }

                            if(jsonObj.isNull("is_viral") == false){
                                String is_viral = jsonObj.getString("is_viral");
                                mNews.setIs_viral(is_viral);
                            }

                            if(jsonObj.isNull("meta_desc") == false){
                                String meta_desc = jsonObj.getString("meta_desc");
                                mNews.setMeta_desc(meta_desc);
                            }

                            if(jsonObj.isNull("meta_keywords") == false){
                                String meta_keywords = jsonObj.getString("meta_keywords");
                                mNews.setMeta_keywords(meta_keywords);
                            }

                            if(jsonObj.isNull("news_count") == false){
                                int news_count = jsonObj.getInt("news_count");
                                mNews.setNews_count(news_count);
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
                                    mNews.setImgList(newsImageList);
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
                                    mNews.setVideoList(newsVideoList);
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
                                mNews.setCommentList(comments);
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
                                mNews.setRelatedNewsList(relatedNews);
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

    private void setCommentList(){
        if(mNews != null && mNews.getCommentList() != null){
            if(listData != null){
                listData.clear();
            }
            listData = mNews.getCommentList();
            if(mNews.getName() != null && mNews.getName().trim().length() > 0) {
                TextView tvNewsTitle = (TextView) findViewById(R.id.tvNewsTitle);
                tvNewsTitle.setText(mNews.getName());
            }
        }
        if(listData == null) {
            listData = new ArrayList<Comment>();
        }
        mAdapter = new CommentListAdapter(listData, CommentListActivity.this, this);
        mRecyclerView.setAdapter(mAdapter);
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
    public void onItemClick(View view, int position) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
