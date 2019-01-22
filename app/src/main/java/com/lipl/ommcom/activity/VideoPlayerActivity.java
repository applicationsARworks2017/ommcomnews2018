package com.lipl.ommcom.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.crashlytics.android.Crashlytics;
import com.lipl.ommcom.R;
import com.lipl.ommcom.util.Util;

import io.fabric.sdk.android.Fabric;

public class VideoPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_video_player);

        if(Util.getNetworkConnectivityStatus(VideoPlayerActivity.this) == false){
            Util.showDialogToShutdownApp(VideoPlayerActivity.this);
            return;
        }

        if(getIntent().getExtras() != null){
            String video_url = getIntent().getExtras().getString("video_url");
            final VideoView videoView = (VideoView) findViewById(R.id.videoView);
            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            Uri video = Uri.parse(video_url);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(video);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    videoView.start();
                    progressBar.setVisibility(View.GONE);
                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    finish();
                }
            });
        }
    }
}
