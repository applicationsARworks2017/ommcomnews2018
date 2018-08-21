package com.lipl.ommcom.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.crashlytics.android.Crashlytics;
import com.lipl.ommcom.R;
import com.lipl.ommcom.util.Config;
import com.lipl.ommcom.util.MultipartUtility;
import com.lipl.ommcom.util.Util;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class LanguageActivity extends AppCompatActivity  {
    LinearLayout tv_english1,tv_odia1;
    String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_language);

        tv_english1=(LinearLayout)findViewById(R.id.tv_english1);
        tv_odia1=(LinearLayout)findViewById(R.id.tv_odia1);

        tv_english1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lang="English";
                SharedPreferences sharedPreferences = getSharedPreferences(Config.SHAREDPREFERENCE_LANGUAGE, 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Config.LANG, lang);
                editor.commit();
                Intent intent = new Intent(LanguageActivity.this,HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        tv_odia1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lang="Odia";
                SharedPreferences sharedPreferences = getSharedPreferences(Config.SHAREDPREFERENCE_LANGUAGE, 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Config.LANG, lang);
                editor.commit();
                Intent intent = new Intent(LanguageActivity.this,HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        setToolBar();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(Util.getNetworkConnectivityStatus(LanguageActivity.this) == false){
            Util.showDialogToShutdownApp(LanguageActivity.this);
            return;
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
