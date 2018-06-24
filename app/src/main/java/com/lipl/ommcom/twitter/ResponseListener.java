package com.lipl.ommcom.twitter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;

/**
 * Created by Android Luminous on 6/11/2016.
 */
public class ResponseListener implements DialogListener {
    Bitmap bitmap;
    String message;
    Context context;
    SocialAuthAdapter socialAuthAdapter;
    public ResponseListener(Bitmap bitmap, String message, Context context, SocialAuthAdapter socialAuthAdapter) {
        this.bitmap = bitmap;
        this.message = message;
        this.context = context;
        this.socialAuthAdapter = socialAuthAdapter;
    }

    @Override
    public void onComplete(final Bundle values) {
        try {
            socialAuthAdapter.uploadImageAsync(message, "The AppGuruz.png",
                    bitmap, 100, new UploadImageListener(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(SocialAuthError error) {
//        if (pd != null && pd.isShowing())
//            Log.d("ShareTwitter", "Authentication Error: " + error.getMessage());
    }

    @Override
    public void onCancel() {
        Log.d("ShareTwitter", "Authentication Cancelled");
    }

    @Override
    public void onBack() {
        Log.d("ShareTwitter", "Dialog Closed by pressing Back Key");
    }
}