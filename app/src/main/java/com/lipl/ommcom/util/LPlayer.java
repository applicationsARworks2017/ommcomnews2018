package com.lipl.ommcom.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by LIPL on 27/09/16.
 */
public class LPlayer implements SurfaceHolder.Callback {
    private static LPlayer sInstance;
    private LPlayer.LPlayerDelegate mLPlayerDelegate = null;
    private AudioManager am = null;

    static {
        System.loadLibrary("NodeMediaClient");
    }

    public LPlayer() {
    }

    public static int init(Context ctx) {
        if(sInstance == null) {
            sInstance = new LPlayer();
            sInstance.am = (AudioManager)ctx.getSystemService(Context.AUDIO_SERVICE);
            sInstance.am.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    Log.i("NodeMedia.LPlayer", "onAudioFocusChange:" + focusChange);
                    if(focusChange == -2) {
                        LPlayer.sInstance.jniPause();
                    } else if(focusChange == 1) {
                        LPlayer.sInstance.jniResume();
                    }

                }
            }, 3, 1);
            return sInstance.jniInit(ctx);
        } else {
            return 0;
        }
    }

    public static void setUIVIew(SurfaceView UIView) {
        if(UIView == null) {
            sInstance.jniSetUIVIew((Object)null);
        } else {
            UIView.getHolder().setKeepScreenOn(true);
            UIView.getHolder().addCallback(sInstance);
        }

    }

    public static void setBufferTime(int bufferTime) {
        sInstance.jniSetBufferTime(bufferTime);
    }

    public static void setMaxBufferTime(int maxBufferTime) {
        sInstance.jniSetMaxBufferTime(maxBufferTime);
    }

    public static void startPlay(String rtmpUrl) {
        startPlay(rtmpUrl, "", "");
    }

    public static void startPlay(String rtmpUrl, String pageUrl, String swfUrl) {
        sInstance.jniStartPlay(rtmpUrl, pageUrl, swfUrl);
    }

    public static void stopPlay() {
        sInstance.jniStopPlay();
    }

    public static boolean capturePicture(String savePath) {
        if(sInstance.jniGetVideoWidth() != 0 && sInstance.jniGetVideoHeight() != 0) {
            try {
                File e = new File(savePath);
                FileOutputStream out = new FileOutputStream(e);
                Bitmap bitmap = Bitmap.createBitmap(sInstance.jniGetVideoWidth(), sInstance.jniGetVideoHeight(), Bitmap.Config.RGB_565);
                byte[] imageBuffer = sInstance.jniCapturePicture();
                ByteBuffer bytebuffer = ByteBuffer.wrap(imageBuffer);
                bitmap.copyPixelsFromBuffer(bytebuffer);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);
                out.flush();
                out.close();
                return true;
            } catch (IOException var6) {
                var6.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public static int getBufferLength() {
        return sInstance.jniGetBufferLength();
    }

    public static void setDelegate(LPlayer.LPlayerDelegate delegate) {
        sInstance.mLPlayerDelegate = delegate;
    }

    private void onEvent(int event, String msg) {
        if(this.mLPlayerDelegate != null) {
            Log.d("NodeMedia.JAVA", "event:" + event + "  msg:" + msg);
            this.mLPlayerDelegate.onEventCallback(event, msg);
        }

    }

    private native int jniInit(Object var1);

    private native int jniSetUIVIew(Object var1);

    private native int jniSetBufferTime(int var1);

    private native int jniSetMaxBufferTime(int var1);

    private native int jniStartPlay(String var1, String var2, String var3);

    private native int jniStopPlay();

    private native int jniGetVideoWidth();

    private native int jniGetVideoHeight();

    private native void jniPause();

    private native void jniResume();

    private native byte[] jniCapturePicture();

    private native int jniGetBufferLength();

    public void surfaceCreated(SurfaceHolder holder) {
        sInstance.jniSetUIVIew(holder.getSurface());
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        sInstance.jniSetUIVIew((Object)null);
    }

    public interface LPlayerDelegate {
        void onEventCallback(int var1, String var2);
    }
}
