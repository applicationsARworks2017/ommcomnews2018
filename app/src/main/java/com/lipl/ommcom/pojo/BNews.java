package com.lipl.ommcom.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by luminousinfoways on 09/10/15.
 */
public class BNews implements Parcelable {

    private String id;
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static final Creator<BNews> CREATOR = new Creator<BNews>() {

        @Override
        public BNews createFromParcel(Parcel source) {
            return new BNews(source);
        }

        @Override
        public BNews[] newArray(int size) {
            BNews[] currentLocations = new BNews[size];
            return currentLocations;
        }
    };

    public BNews(Parcel in){
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(title);
    }

    private void readFromParcel(Parcel in){

        id = in.readString();
        title = in.readString();
    }
}