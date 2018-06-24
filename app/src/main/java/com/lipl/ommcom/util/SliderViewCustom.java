package com.lipl.ommcom.util;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.lipl.ommcom.R;

/**
 * Created by Android Luminous on 6/2/2016.
 */
public class SliderViewCustom extends BaseSliderView {
    private boolean show_play;
    public SliderViewCustom(Context context, boolean show_play) {
        super(context);
        this.show_play = show_play;
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.custom_slide_image_layout,null);
        ImageView target = (ImageView)v.findViewById(R.id.daimajia_slider_image);
        ImageView imgPlay = (ImageView)v.findViewById(R.id.imgPlay);
        if(show_play){
            imgPlay.setVisibility(View.VISIBLE);
        } else{
            imgPlay.setVisibility(View.GONE);
        }
        TextView description = (TextView)v.findViewById(R.id.description);
        description.setText(getDescription());
        bindEventAndShow(v, target);
        return v;
    }
}
