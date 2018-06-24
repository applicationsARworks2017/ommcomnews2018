package com.lipl.ommcom.twitter;

import android.content.Context;
import android.widget.Toast;

import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

/**
 * Created by Android Luminous on 6/11/2016.
 */
public class UploadImageListener implements
        SocialAuthListener {

    private Context context;
    public UploadImageListener(Context context){
        this.context = context;
    }

    @Override
    public void onError(SocialAuthError e) {
    }

    @Override
    public void onExecute(String s, Object o) {
        if(!(o instanceof Integer)){
            return;
        }
        Integer status = (Integer)o;
        try {
            if (status.intValue() == 200 || status.intValue() == 201
                    || status.intValue() == 204) {
                Toast.makeText(context, "Image Uploaded",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Image not Uploaded",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (NullPointerException e) {
            Toast.makeText(context, "Image not Uploaded",
                    Toast.LENGTH_SHORT).show();
        }
    }
}