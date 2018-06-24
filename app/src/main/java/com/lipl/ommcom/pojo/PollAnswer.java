package com.lipl.ommcom.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Android Luminous on 5/6/2016.
 */
public class PollAnswer implements Parcelable {

    private String name;
    private int percentage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public static final Creator<PollAnswer> CREATOR = new Creator<PollAnswer>() {

        @Override
        public PollAnswer createFromParcel(Parcel source) {
            return new PollAnswer(source);
        }

        @Override
        public PollAnswer[] newArray(int size) {
            PollAnswer[] currentLocations = new PollAnswer[size];
            return currentLocations;
        }
    };

    public PollAnswer(Parcel in){
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeInt(percentage);
    }

    private void readFromParcel(Parcel in){

        name = in.readString();
        percentage = in.readInt();
    }
}
