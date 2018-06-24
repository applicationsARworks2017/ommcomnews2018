package com.lipl.ommcom.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luminousinfoways on 09/10/15.
 */
public class BreakingNews implements Parcelable {

    private List<BNews> newsdetails;
    private List<BNews> bNewsList;

    public List<BNews> getNewsdetails() {
        return newsdetails;
    }

    public void setNewsdetails(List<BNews> newsdetails) {
        this.newsdetails = newsdetails;
    }

    public List<BNews> getbNewsList() {
        return bNewsList;
    }

    public void setbNewsList(List<BNews> bNewsList) {
        this.bNewsList = bNewsList;
    }

    public static final Creator<BreakingNews> CREATOR = new Creator<BreakingNews>() {

        @Override
        public BreakingNews createFromParcel(Parcel source) {
            return new BreakingNews(source);
        }

        @Override
        public BreakingNews[] newArray(int size) {
            BreakingNews[] currentLocations = new BreakingNews[size];
            return currentLocations;
        }
    };

    public BreakingNews(Parcel in){
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeTypedList(newsdetails);
        dest.writeTypedList(bNewsList);
    }

    private void readFromParcel(Parcel in){

        newsdetails = new ArrayList<BNews>();
        in.readTypedList(newsdetails, BNews.CREATOR);
        bNewsList = new ArrayList<BNews>();
        in.readTypedList(bNewsList, BNews.CREATOR);
    }
}