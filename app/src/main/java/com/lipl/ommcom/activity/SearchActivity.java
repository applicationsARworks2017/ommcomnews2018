package com.lipl.ommcom.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lipl.ommcom.R;
import com.lipl.ommcom.adapter.NewsListAdapter;
import com.lipl.ommcom.pojo.News;
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

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, NewsListAdapter.OnItemClickListener {

    private EditText meditTextSearch;
    private Button mbtnSearch;
    private static final String TAG = "SearchActivity";
    private XRecyclerView mRecyclerView;
    private NewsListAdapter mAdapter;
    private ArrayList<News> mNewsList;
    private ProgressBar pBar = null;
    private int pagination_count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_search);

        setToolBar();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(Util.getNetworkConnectivityStatus(SearchActivity.this) == false){
            Util.showDialogToShutdownApp(SearchActivity.this);
            return;
        }

        pBar = (ProgressBar) findViewById(R.id.pBar);
        mNewsList = new ArrayList<News>();

        meditTextSearch = (EditText) findViewById(R.id.editTextSearch);
        mbtnSearch = (Button) findViewById(R.id.btnSearch);
        mbtnSearch.setOnClickListener(this);

        mRecyclerView = (XRecyclerView)this.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(SearchActivity.this, NewsDetailsActivity.class);
        intent.putExtra("news", mNewsList.get(position));
        startActivity(intent);
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

    private void updateView(){

        TextView text_empty = (TextView) findViewById(R.id.text_empty);
        text_empty.setVisibility(View.INVISIBLE);
        if(mNewsList == null || mNewsList.size() <= 0){
            //Show there is no news here
            text_empty.setText("No news as of now.");
            text_empty.setVisibility(View.VISIBLE);

            return;
        }

        mAdapter = new NewsListAdapter(mNewsList, SearchActivity.this, SearchActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setRefreshing(false);
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pagination_count = 1;
                mNewsList = new ArrayList<News>();
                String search_text = meditTextSearch.getText().toString().trim();
                getData(search_text);
            }

            @Override
            public void onLoadMore() {
                String search_text = meditTextSearch.getText().toString().trim();
                getData(search_text);
            }
        });

        setToolBar();
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



    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btnSearch:
                pagination_count = 1;
                mNewsList = new ArrayList<News>();
                String search_text = meditTextSearch.getText().toString().trim();
                getData(search_text);
                break;
            default:
                break;
        }
    }

    private void getData(final String search_text){
        if(Util.getNetworkConnectivityStatus(SearchActivity.this) == false){
            Util.showDialogToShutdownApp(SearchActivity.this);
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
                pBar.setVisibility(View.INVISIBLE);

                if(pagination_count == 1) {
                    updateView();
                } else {
                    if(mRecyclerView != null &&  mRecyclerView.getAdapter() != null) {
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                }
                pagination_count++;

                if(mRecyclerView != null){
                    mRecyclerView.loadMoreComplete();
                    mRecyclerView.refreshComplete();
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                InputStream in = null;
                int resCode = -1;

                try {
                    String link = "";
                    link = Config.API_BASE_URL + Config.SEARCH_NEWS_API + "/" + search_text+ "?page=" + pagination_count;

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
                     * ]}"topNews": {
                     "name": "First Top News Video",
                     "video_file": "64476924314647726981076688814.mp4"
                     }
                     * */

                    JSONObject res = new JSONObject(response);
                    if(res.isNull("data") == false) {
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

                                mNewsList.add(news);
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
