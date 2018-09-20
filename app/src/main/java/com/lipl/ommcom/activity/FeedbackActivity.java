package com.lipl.ommcom.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.crashlytics.android.Crashlytics;
import com.lipl.ommcom.R;
import com.lipl.ommcom.util.Config;
import com.lipl.ommcom.util.MultipartUtility;
import com.lipl.ommcom.util.Util;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.apache.http.conn.ConnectTimeoutException;
import org.brickred.socialauth.Feed;

import java.io.IOException;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_feedback);

        setToolBar();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(Util.getNetworkConnectivityStatus(FeedbackActivity.this) == false){
            Util.showDialogToShutdownApp(FeedbackActivity.this);
            return;
        }

        Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        Button btnReset = (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        MaterialEditText tvName = (MaterialEditText) findViewById(R.id.tvName);
        MaterialEditText tvemail = (MaterialEditText) findViewById(R.id.email);
        MaterialEditText tvmessage = (MaterialEditText) findViewById(R.id.message);
        MaterialEditText tvmobile = (MaterialEditText) findViewById(R.id.mobile);

        switch (id){
            case R.id.btnSubmit:

                String name = tvName.getText().toString().trim();
                String email = tvemail.getText().toString().trim();
                String message = tvmessage.getText().toString().trim();
                String mobile = tvmobile.getText().toString().trim();

                if(name == null || name.trim().length() <= 0){
                    tvName.setError("Enter name");
                    return;
                }

                if(email == null || email.trim().length() <= 0){
                    tvemail.setError("Enter email");
                    return;
                }

                if(mobile == null || mobile.trim().length() <= 10){
                    tvmobile.setError("Enter mobile");
                    return;
                }

                if(message == null || message.trim().length() <= 0){
                    tvmessage.setError("Enter message.");
                    return;
                }

                if(Util.getNetworkConnectivityStatus(FeedbackActivity.this) == false){
                    Util.showDialogToShutdownApp(FeedbackActivity.this);
                    return;
                }

                FeedbackAsyncTask asyncTask = new FeedbackAsyncTask();
                asyncTask.execute(name, email, message, mobile);

                break;
            case R.id.btnReset:
                tvName.setText("");
                tvemail.setText("");
                tvmessage.setText("");
                tvmobile.setText("");
                break;
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

    private class FeedbackAsyncTask extends AsyncTask<String, Void, Void> {

        String TAG = "PostCommentAsyncTask";
        private boolean is_success = false;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(FeedbackActivity.this, R.style.MyTheme);
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                progressDialog.show();
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            String charset = "UTF-8";
            String requestURL = "";
            requestURL = Config.API_BASE_URL + Config.FEEDBACK;

            try {
                String name = params[0];
                String email = params[1];
                String message = params[2];
                String mobile = params[3];
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                multipart.addFormField("name", name);
                multipart.addFormField("email", email);
                multipart.addFormField("message", message);
                multipart.addFormField("mobile", mobile);
                List<String> response = multipart.finish();
                System.out.println("SERVER REPLIED:");
                String res = "";
                for (String line : response) {
                    res = res + line + "\n";
                }
                Log.i(TAG, res);

                if(res!= null && res.trim().length() > 0 && res.trim().equalsIgnoreCase("1")){
                    is_success = true;
                } else{
                    is_success = false;
                }
            } catch(ConnectTimeoutException e){
                System.err.println(e);
            } catch(IOException ex) {
                System.err.println(ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {
            super.onPostExecute(result);

            if(progressDialog != null){
                progressDialog.dismiss();
            }

            if(is_success){
                new AlertDialog.Builder(FeedbackActivity.this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar)
                        .setIcon(R.mipmap.ic_done)
                        .setTitle("Feedback")
                        .setMessage("Submitted successfully.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .show();
            } else{
                new AlertDialog.Builder(FeedbackActivity.this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar)
                        .setIcon(R.mipmap.ic_sentiment_dissatisfied)
                        .setTitle("Feedback")
                        .setMessage("Submission failed.")
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
