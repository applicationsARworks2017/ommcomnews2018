package com.lipl.ommcom.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.VideoView;

import com.crashlytics.android.Crashlytics;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.lipl.ommcom.R;
import com.lipl.ommcom.pojo.Poll;
import com.lipl.ommcom.pojo.PollAnswer;
import com.lipl.ommcom.pojo.PollQuestion;
import com.lipl.ommcom.pojo.PollQuestionOption;
import com.lipl.ommcom.util.AnimationUtil;
import com.lipl.ommcom.util.Config;
import com.lipl.ommcom.util.CustomTextView;
import com.lipl.ommcom.util.Util;

import org.json.JSONArray;
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
import java.util.Collections;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class PollingActivity extends AppCompatActivity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener,
        OnChartValueSelectedListener {

    private RadioGroup mRadioGroup;
    private static final String TAG ="PollingActivity";
    private Poll mPoll;
    private ProgressBar pBar;
    protected HorizontalBarChart mChart;
    private Typeface tf;
    private boolean is_checked = false;
    private int checked_radio_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_polling);

        setToolBar();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(Util.getNetworkConnectivityStatus(PollingActivity.this) == false){
            Util.showDialogToShutdownApp(PollingActivity.this);
            return;
        }
        mPoll = new Poll(Parcel.obtain());
        pBar = (ProgressBar) findViewById(R.id.pBar);
        getData();
    }

    private void setView(){

        if(mPoll == null){
            return;
        }

        String question = mPoll.getQuestion().getName();
        CustomTextView tvQuestion = (CustomTextView) findViewById(R.id.tvQuestion);
        tvQuestion.setText(question);

        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        if(mPoll.getQuestion() != null
                && mPoll.getQuestion().getPollQuestionOptionList() != null
                && mPoll.getQuestion().getPollQuestionOptionList().size() > 0) {
            for (int i = 0; i < mPoll.getQuestion().getPollQuestionOptionList().size(); i++) {
                String name = mPoll.getQuestion().getPollQuestionOptionList().get(i).getName();
                String id = mPoll.getQuestion().getPollQuestionOptionList().get(i).getId();
                RadioButton newRadioButton = new RadioButton(this);
                newRadioButton.setTextColor(getResources().getColor(R.color.news_long_desc_text_color));
                newRadioButton.setText(name);
                try {
                    newRadioButton.setId(Integer.parseInt(id));
                } catch(Exception exception){
                    newRadioButton.setId(i + 1);
                    Log.e(TAG, "setView()", exception);
                }
                LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                        RadioGroup.LayoutParams.WRAP_CONTENT,
                        RadioGroup.LayoutParams.WRAP_CONTENT);
                mRadioGroup.addView(newRadioButton, 0, layoutParams);
                mRadioGroup.setOnCheckedChangeListener(this);
            }
        }

        Button btnVoteNow = (Button) findViewById(R.id.btnVoteNow);
        btnVoteNow.setOnClickListener(this);
        Button btnResult = (Button) findViewById(R.id.btnResult);
        btnResult.setOnClickListener(this);
    }

    /**
     * Set data to chart to show the result status
     * */
    private void setData() {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<String> xVals = new ArrayList<String>();

        if(mPoll != null
                && mPoll.getAnswer() != null
                && mPoll.getAnswer().size() > 0) {
            int size = mPoll.getAnswer().size();
            for (int i = 0; i < size; i++) {
                xVals.add(mPoll.getAnswer().get(i).getName());
                yVals1.add(new BarEntry(mPoll.getAnswer().get(i).getPercentage(), i)); // (Value, index)
            }

            BarDataSet set1 = new BarDataSet(yVals1, "Result");

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(xVals, dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(tf);

            mChart.setData(data);
        }

    }

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;

        RectF bounds = mChart.getBarBounds((BarEntry) e);
        PointF position = mChart.getPosition(e, mChart.getData().getDataSetByIndex(dataSetIndex)
                .getAxisDependency());

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());
    }

    public void onNothingSelected() {
    };

    private void setChart(){

        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setDescription("");
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);
        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);
        // mChart.setDrawXLabels(false);
        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);
        tf = Typeface.createFromAsset(getAssets(), "font/helvetica_neue_bd.ttf");

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTypeface(tf);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(true);
        xl.setGridLineWidth(0.3f);

        YAxis yl = mChart.getAxisLeft();
        yl.setTypeface(tf);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setGridLineWidth(0.3f);
        yl.setAxisMinValue(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

        YAxis yr = mChart.getAxisRight();
        yr.setTypeface(tf);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinValue(0f); // this replaces setStartAtZero(true)
//        yr.setInverted(true);

        setData();
        mChart.animateY(2500);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);

        // mChart.setDrawLegend(false);
    }

    public void onCheckedChanged(RadioGroup group, int checkedId) {
        checked_radio_id = group.getCheckedRadioButtonId();
        if(checked_radio_id > 0){
            is_checked = true;
        } else{
            is_checked = false;
        }
    }

    private void getData(){
        if(Util.getNetworkConnectivityStatus(PollingActivity.this) == false){
            Util.showDialogToShutdownApp(PollingActivity.this);
            return;
        }

        new AsyncTask<Void, Void, Boolean>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(Boolean aVoid) {
                super.onPostExecute(aVoid);
                pBar.setVisibility(View.GONE);
                if(aVoid) {
                    setView();
                } else{
                    new AlertDialog.Builder(PollingActivity.this,
                            android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar)
                            .setIcon(R.mipmap.ic_sentiment_dissatisfied)
                            .setTitle("Polling")
                            .setMessage("Something went wrong.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                InputStream in = null;
                int resCode = -1;

                try {
                    String link = Config.API_BASE_URL + Config.POLL_QUESTION_API;
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
                    *
                    * {
                        "QUESTION": {
                                "id": "2",
                                "name": "Is India a developing country?",
                                "is_statitics_to_public": "1",
                                "is_active": "1",
                                "is_trash": "0",
                                "created_at": null,
                                "updated_at": "2016-05-04 07:59:18",
                                "question_option": [
                                {
                                "id": "4",
                                "question_id": "2",
                                "name": "Yes",
                                "position": "1",
                                "created_at": null,
                                "updated_at": null
                                },
                                {
                                "id": "5",
                                "question_id": "2",
                                "name": "No",
                                "position": "2",
                                "created_at": null,
                                "updated_at": null
                                }
                            ]
                        },
                        "ANSWER": {
                            "1": {
                                "name": "Yes",
                                "percentage": 100
                            }
                         }
                        }
                    * */

                    JSONObject res = new JSONObject(response);
                    if (res != null && res.isNull("QUESTION") == false) {
                        JSONObject qus = res.getJSONObject("QUESTION");

                        PollQuestion poll_qus = new PollQuestion(Parcel.obtain());
                        if (qus.isNull("id") == false) {
                            String id = qus.getString("id");
                            poll_qus.setId(id);
                        }
                        if (qus.isNull("name") == false) {
                            String name = qus.getString("name");
                            poll_qus.setName(name);
                        }
                        if (qus.isNull("is_statitics_to_public") == false) {
                            String is_statitics_to_public = qus.getString("is_statitics_to_public");
                            poll_qus.setIs_statitics_to_public(is_statitics_to_public);
                        }
                        if (qus.isNull("is_active") == false) {
                            String is_active = qus.getString("is_active");
                            poll_qus.setIs_active(is_active);
                        }
                        if (qus.isNull("is_trash") == false) {
                            String is_trash = qus.getString("is_trash");
                            poll_qus.setIs_trash(is_trash);
                        }
                        if (qus.isNull("created_at") == false) {
                            String created_at = qus.getString("created_at");
                            poll_qus.setCreated_at(created_at);
                        }
                        if (qus.isNull("updated_at") == false) {
                            String updated_at = qus.getString("updated_at");
                            poll_qus.setUpdated_at(updated_at);
                        }
                        if (qus.isNull("question_option") == false) {
                            JSONArray question_option = qus.getJSONArray("question_option");
                            if (question_option != null && question_option.length() > 0) {
                                List<PollQuestionOption> pollQuestionOptionList = new ArrayList<PollQuestionOption>();
                                for (int i = 0; i < question_option.length(); i++) {
                                    PollQuestionOption poll_option = new PollQuestionOption(Parcel.obtain());
                                    if (question_option.getJSONObject(i).isNull("id") == false) {
                                        String id = question_option.getJSONObject(i).getString("id");
                                        poll_option.setId(id);
                                    }
                                    if (question_option.getJSONObject(i).isNull("question_id") == false) {
                                        String question_id = question_option.getJSONObject(i).getString("question_id");
                                        poll_option.setQuestion_id(question_id);
                                    }
                                    if (question_option.getJSONObject(i).isNull("name") == false) {
                                        String name = question_option.getJSONObject(i).getString("name");
                                        poll_option.setName(name);
                                    }
                                    if (question_option.getJSONObject(i).isNull("position") == false) {
                                        String position = question_option.getJSONObject(i).getString("position");
                                        poll_option.setPosition(position);
                                    }
                                    if (question_option.getJSONObject(i).isNull("created_at") == false) {
                                        String created_at = question_option.getJSONObject(i).getString("created_at");
                                        poll_option.setCreated_at(created_at);
                                    }
                                    if (question_option.getJSONObject(i).isNull("updated_at") == false) {
                                        String updated_at = question_option.getJSONObject(i).getString("updated_at");
                                        poll_option.setUpdated_at(updated_at);
                                    }
                                    pollQuestionOptionList.add(poll_option);
                                }
                                Collections.reverse(pollQuestionOptionList);
                                poll_qus.setPollQuestionOptionList(pollQuestionOptionList);
                            }
                        }
                        mPoll.setQuestion(poll_qus);
                    }

                    int count = 0;
                    if (res != null && res.isNull("COUNT") == false) {
                        count = res.getInt("COUNT");
                    }

                    if (res != null && res.isNull("ANSWER") == false) {
                        JSONObject ans = res.getJSONObject("ANSWER");
                        if (count > 0) {
                            List<PollAnswer> answers = new ArrayList<PollAnswer>();
                            for (int i = 1; i <= count; i++) {
                                PollAnswer answer = new PollAnswer(Parcel.obtain());
                                if (ans.getJSONObject(i + "").isNull("name") == false) {
                                    String name = ans.getJSONObject(i + "").getString("name");
                                    answer.setName(name);
                                }
                                if (ans.getJSONObject(i + "").isNull("percentage") == false) {
                                    int percentage = ans.getJSONObject(i + "").getInt("percentage");
                                    answer.setPercentage(percentage);
                                }
                                answers.add(answer);
                            }
                            Collections.reverse(answers);
                            mPoll.setAnswer(answers);
                        }
                    }

                    return true;
                } catch (SocketTimeoutException exception) {
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                    return false;
                } catch (ConnectException exception) {
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                    return false;
                } catch (MalformedURLException exception) {
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                    return false;
                } catch (IOException exception) {
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                    return false;
                } catch (Exception exception) {
                    Log.e(TAG, "LoginAsync : doInBackground", exception);
                    return false;
                }
            }
        }.execute();
    }

    private void submitAnswer(final String question_id,
                              final String question_option_id, final String ip_address){

        if(is_checked == false){
            new AlertDialog.Builder(PollingActivity.this,
                    android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar)
                    .setIcon(R.mipmap.ic_sentiment_dissatisfied)
                    .setTitle("Polling")
                    .setMessage("Select any one.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setCancelable(false)
                    .show();
        } else{

            if(Util.getNetworkConnectivityStatus(PollingActivity.this) == false){
                Util.showDialogToShutdownApp(PollingActivity.this);
                return;
            }

            new AsyncTask<Void, Void, Integer>() {
                private ProgressDialog progressDialog;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    if (progressDialog == null) {
                        progressDialog = new ProgressDialog(PollingActivity.this, R.style.MyTheme);
                        progressDialog.setCancelable(false);
                        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                        progressDialog.show();
                    }
                }

                @Override
                protected Integer doInBackground(Void... params) {

                    InputStream in = null;
                    int resCode = -1;

                    try {
                        Thread.sleep(3000);
                        String link = Config.API_BASE_URL + Config.POLL_SUBMIT_API;
                        URL url = new URL(link);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(10000);
                        conn.setConnectTimeout(15000);
                        //conn.setDoInput(true);
                        conn.setDoOutput(true);
                        conn.setAllowUserInteraction(false);
                        conn.setRequestMethod("POST");
                        conn.connect();

                        Uri.Builder builder = new Uri.Builder()
                                .appendQueryParameter("question_id", question_id)
                                .appendQueryParameter("question_option_id", question_option_id)
                                .appendQueryParameter("ip_address", ip_address)
                                .appendQueryParameter("user_agent", "Android");
                        //.appendQueryParameter("deviceid", deviceid);
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
                        if(in == null){
                            return null;
                        }
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                        String response = "", data = "";

                        while ((data = reader.readLine()) != null) {
                            response += data + "\n";
                        }

                        Log.i(TAG, "Response : " + response);

                        if(response != null && response.trim().equalsIgnoreCase("1")){
                            return 1;
                        } else{
                            return 0;
                        }
                    } catch (SocketTimeoutException exception) {
                        Log.e(TAG, "LoginAsync : doInBackground", exception);
                    } catch (ConnectException exception) {
                        Log.e(TAG, "LoginAsync : doInBackground", exception);
                    } catch (MalformedURLException exception) {
                        Log.e(TAG, "LoginAsync : doInBackground", exception);
                    } catch (IOException exception) {
                        Log.e(TAG, "LoginAsync : doInBackground", exception);
                    } catch (Exception exception) {
                        Log.e(TAG, "LoginAsync : doInBackground", exception);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Integer aVoid) {
                    super.onPostExecute(aVoid);
                    if(progressDialog != null){
                        progressDialog.dismiss();
                    }
                    if(aVoid.intValue() == 1){
                        new AlertDialog.Builder(PollingActivity.this,
                                android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar)
                                .setIcon(R.mipmap.ic_done)
                                .setTitle("Polling")
                                .setMessage("Voting done successfully.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                .setCancelable(false)
                                .show();
                    } else{
                        new AlertDialog.Builder(PollingActivity.this,
                                android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar)
                                .setIcon(R.mipmap.ic_warning)
                                .setTitle("Polling")
                                .setMessage("Some error occurred.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setCancelable(false)
                                .show();
                    }
                }
            }.execute();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btnVoteNow:
                String question_id = mPoll.getQuestion().getId();
                String question_option_id = checked_radio_id + "";
                String mac_id = Util.getMacAddress(PollingActivity.this);
                submitAnswer(question_id, question_option_id, mac_id);
                break;
            case R.id.btnResult:
                showResult();
                break;
            default:
                break;
        }
    }

    private void showResult(){
        mChart = (HorizontalBarChart) findViewById(R.id.chart1);
        mChart.setVisibility(View.VISIBLE);
        setChart();
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
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_citizen_journalist, menu);
        return true;
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
}
