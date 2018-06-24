package com.lipl.ommcom.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by luminousinfoways on 09/10/15.
 */
public class NewsDetailsForFlipModel implements Parcelable {

    private String img_path;
    private String text_to_show;

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getText_to_show() {
        return text_to_show;
    }

    public void setText_to_show(String text_to_show) {
        this.text_to_show = text_to_show;
    }

    public static final Creator<NewsDetailsForFlipModel> CREATOR = new Creator<NewsDetailsForFlipModel>() {

        @Override
        public NewsDetailsForFlipModel createFromParcel(Parcel source) {
            return new NewsDetailsForFlipModel(source);
        }

        @Override
        public NewsDetailsForFlipModel[] newArray(int size) {
            NewsDetailsForFlipModel[] currentLocations = new NewsDetailsForFlipModel[size];
            return currentLocations;
        }
    };

    public NewsDetailsForFlipModel(Parcel in){
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(img_path);
        dest.writeString(text_to_show);
    }

    private void readFromParcel(Parcel in){

        img_path = in.readString();
        text_to_show = in.readString();
    }
}