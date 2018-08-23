package com.lipl.ommcom.activity;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lipl.ommcom.R;
import com.lipl.ommcom.adapter.AllOdishaNewsAdapter;
import com.lipl.ommcom.pojo.OdishaNews;
import com.lipl.ommcom.util.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AllOdishaNewsList extends AppCompatActivity {
    private XRecyclerView mRecyclerView;
    private ProgressBar pBar;
    ArrayList<OdishaNews> arrayList_odishanews;
    int page=1;
    Boolean scroll_allow = false;
    int total;
    String lang;
    AllOdishaNewsAdapter mAdapter;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_odisha_news_list);
        mRecyclerView=(XRecyclerView)findViewById(R.id.rv_allnews);
        pBar=(ProgressBar)findViewById(R.id.pBar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        arrayList_odishanews =new ArrayList<>();
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllOdishaNewsList.this.finish();
            }
        });
        lang = getSharedPreferences(Config.SHAREDPREFERENCE_LANGUAGE, 0).getString(Config.LANG, null);

        getNewsList();
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                // list_visitors.clear();
                arrayList_odishanews =new ArrayList<>();
                page=1;
                getNewsList();            }

            @Override
            public void onLoadMore() {
                if(scroll_allow =true){
                    page++;
                    getNewsList();
                }
                else{
                    mRecyclerView.setIsnomore(true);
                }
            }
        });

    }

    private void getNewsList() {
        new OdihaNews().execute();
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

        pBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... params) {


            try {
                InputStream in = null;
                int resCode = -1;
                String link= null;
                if(lang==null || lang.contentEquals("English")) {
                    link= "https://www.ommcomnews.com/public/api/v0.1/viewMoreNews"+Config.EN_CONENT;

                }
                else{
                    link= "https://www.ommcomnews.com/public/api/v0.1/viewMoreNews"+Config.OD_CONENT;
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
                        .appendQueryParameter("page_no", String.valueOf(page));

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
                    server_status=res.getInt("status");
                    if(server_status==1){
                        JSONArray news_array=res.getJSONArray("odisha_news");
                        if(news_array.length()>10){
                            scroll_allow=false;
                        }
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
            pBar.setVisibility(View.GONE);
            if(server_status==1){
                mAdapter = new AllOdishaNewsAdapter(AllOdishaNewsList.this,arrayList_odishanews);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.loadMoreComplete();
                mAdapter.notifyDataSetChanged();
                mRecyclerView.refreshComplete();
            }

            if(page>1){
                mRecyclerView.scrollToPosition(total-2);
                total=mRecyclerView.getAdapter().getItemCount()-1;
            }
            else{
                total=mRecyclerView.getAdapter().getItemCount()-1;
            }

        }
    }
}
