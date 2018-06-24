package com.lipl.ommcom.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lipl.ommcom.R;
import com.lipl.ommcom.activity.CNewsDetailsActivity;
import com.lipl.ommcom.activity.NewsDetailsActivity;
import com.lipl.ommcom.activity.NewsListActivity;
import com.lipl.ommcom.activity.VideoPlayerActivity;
import com.lipl.ommcom.adapter.VideoListAdapter;
import com.lipl.ommcom.pojo.CitizenJournalistVideos;
import com.lipl.ommcom.pojo.News;
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
import java.util.List;

public class LatestVideosFragment extends Fragment
        implements VideoListAdapter.OnItemClickListener,
        SearchView.OnQueryTextListener {

    private XRecyclerView mRecyclerView;
    private VideoListAdapter mAdapter;
    private int refreshTime = 0;
    private int times = 0;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private static final String TAG = "LatestVideo";
    private List<CitizenJournalistVideos> latestVideos;

    private ProgressBar pBar;
    private OnFragmentInteractionListener mListener;

    private PopupAdvertisement popupAdvertisement;

    public LatestVideosFragment() {
        // Required empty public constructor
    }

    public static LatestVideosFragment newInstance(String param1, String param2) {
        LatestVideosFragment fragment = new LatestVideosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_most_viewed_videos, container, false);

        mRecyclerView = (XRecyclerView) view.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        pBar = (ProgressBar) view.findViewById(R.id.pBar);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mRecyclerView.setLoadingMoreEnabled(false);

        pBar = (ProgressBar) view.findViewById(R.id.pBar);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if(Util.getNetworkConnectivityStatus(getActivity()) == true) {
                    loadLatestVideos();
                }
            }

            @Override
            public void onLoadMore() {

            }
        });

        latestVideos = new  ArrayList<CitizenJournalistVideos>();
        mAdapter = new VideoListAdapter(latestVideos, getActivity(), this, false);
        mRecyclerView.setAdapter(mAdapter);
        loadLatestVideos();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);y6yy
//
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onItemClick(View view, int position) {
        String video_file_path = "";
        String file_type = latestVideos.get(position).getFile_type();
        /*if(file_type != null && file_type.trim().equalsIgnoreCase("Video")){
            video_file_path = Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_CITIZEN_NEWS
                    + Config.FOLDER_VIDEO + "/" + latestVideos.get(position).getFile_path();

            if(video_file_path != null && video_file_path.trim().length() > 0) {
                Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
                intent.putExtra("video_url", video_file_path);
                startActivity(intent);
            }
        } else if(file_type != null && file_type.trim().equalsIgnoreCase("Audio")){
            String url = Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_CITIZEN_NEWS
                    + Config.FOLDER_VIDEO + "/" + latestVideos.get(position).getFile_path();
            Uri myUri = Uri.parse(url);
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(myUri, "audio/*");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            CitizenJournalistVideos cv = latestVideos.get(position);
            Intent intent = new Intent(getActivity(), CNewsDetailsActivity.class);
            intent.putExtra("news", cv);
            startActivity(intent);
        }*/
        CitizenJournalistVideos cv = latestVideos.get(position);
        Intent intent = new Intent(getActivity(), CNewsDetailsActivity.class);
        intent.putExtra("news", cv);
        startActivity(intent);
    }

    private void loadLatestVideos(){
        if(getActivity() == null){
            return;
        }
        if(Util.getNetworkConnectivityStatus(getActivity()) == false){
            Util.showDialogToShutdownApp(getActivity());
            return;
        }
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(pBar != null) {
                    pBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(pBar != null) {
                    pBar.setVisibility(View.GONE);
                }

                mAdapter = new VideoListAdapter(latestVideos, getActivity(), LatestVideosFragment.this, false);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.refreshComplete();
            }

            @Override
            protected Void doInBackground(Void... params) {
                InputStream in = null;
                int resCode = -1;

                try {
                    String link = Config.API_BASE_URL + Config.CITIZEN_NEWS_LATEST_VIDEOS;
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
                        JSONArray _data = new JSONArray(response);
                        if(_data != null && _data.length() > 0){
                            if(latestVideos == null){
                                latestVideos = new ArrayList<CitizenJournalistVideos>();
                            } else{
                                latestVideos.clear();
                            }
                            for(int i = 0; i < _data.length(); i++){
                                CitizenJournalistVideos latestVideo = new CitizenJournalistVideos(Parcel.obtain());
                                if(_data.getJSONObject(i).isNull("id") == false){
                                    String id = _data.getJSONObject(i).getString("id");
                                    latestVideo.setId(id);
                                }
                                if(_data.getJSONObject(i).isNull("name") == false){
                                    String name = _data.getJSONObject(i).getString("name");
                                    latestVideo.setName(name);
                                }
                                if(_data.getJSONObject(i).isNull("file_path") == false){
                                    String file_path = _data.getJSONObject(i).getString("file_path");
                                    latestVideo.setFile_path(file_path);
                                }
                                if(_data.getJSONObject(i).isNull("file_type") == false){
                                    String file_type = _data.getJSONObject(i).getString("file_type");
                                    latestVideo.setFile_type(file_type);
                                }
                                if(_data.getJSONObject(i).isNull("description") == false){
                                    String description = _data.getJSONObject(i).getString("description");
                                    latestVideo.setDescription(description);
                                }
                                if(_data.getJSONObject(i).isNull("slug") == false){
                                    String slug = _data.getJSONObject(i).getString("slug");
                                    latestVideo.setSlug(slug);
                                }
                                if(_data.getJSONObject(i).isNull("is_anonymous") == false){
                                    String is_anonymous = _data.getJSONObject(i).getString("is_anonymous");
                                    latestVideo.setIs_anonymous(is_anonymous);
                                }
                                if(_data.getJSONObject(i).isNull("updated_at") == false){
                                    String updated_at = _data.getJSONObject(i).getString("updated_at");
                                    latestVideo.setUpdated_at(updated_at);
                                }
                                if(latestVideo != null && latestVideo.getIs_anonymous() != null
                                        && latestVideo.getIs_anonymous().trim().equalsIgnoreCase("0")) {
                                    latestVideos.add(latestVideo);
                                }
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.news_list_menu, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        mAdapter.setFilter(latestVideos, getActivity(), LatestVideosFragment.this, false);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<CitizenJournalistVideos> filteredModelList = filter(latestVideos, newText);
        mAdapter.setFilter(filteredModelList, getActivity(), this, false);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<CitizenJournalistVideos> filter(List<CitizenJournalistVideos> models, String query) {
        query = query.toLowerCase();

        final List<CitizenJournalistVideos> filteredModelList = new ArrayList<>();
        for (CitizenJournalistVideos model : models) {
            final String text = model.getDescription().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}
