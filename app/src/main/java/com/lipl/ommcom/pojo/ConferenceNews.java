package com.lipl.ommcom.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by luminousinfoways on 09/10/15.
 */
public class ConferenceNews implements Parcelable {

    private String id;
    private String name;
    private String short_desc;
    private String long_desc;
    private String featured_image;
    private String conference_banner;
    private String started_at;
    private String start_time;
    private String end_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }

    public String getLong_desc() {
        return long_desc;
    }

    public void setLong_desc(String long_desc) {
        this.long_desc = long_desc;
    }

    public String getFeatured_image() {
        return featured_image;
    }

    public void setFeatured_image(String featured_image) {
        this.featured_image = featured_image;
    }

    public String getConference_banner() {
        return conference_banner;
    }

    public void setConference_banner(String conference_banner) {
        this.conference_banner = conference_banner;
    }

    public String getStarted_at() {
        return started_at;
    }

    public void setStarted_at(String started_at) {
        this.started_at = started_at;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public static final Creator<ConferenceNews> CREATOR = new Creator<ConferenceNews>() {

        @Override
        public ConferenceNews createFromParcel(Parcel source) {
            return new ConferenceNews(source);
        }

        @Override
        public ConferenceNews[] newArray(int size) {
            ConferenceNews[] currentLocations = new ConferenceNews[size];
            return currentLocations;
        }
    };

    public ConferenceNews(Parcel in){
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(short_desc);
        dest.writeString(long_desc);
        dest.writeString(featured_image);
        dest.writeString(conference_banner);
        dest.writeString(started_at);
        dest.writeString(start_time);
        dest.writeString(end_time);
    }

    private void readFromParcel(Parcel in){

        id = in.readString();
        name = in.readString();
        short_desc = in.readString();
        conference_banner = in.readString();
        started_at = in.readString();
        start_time = in.readString();
        end_time = in.readString();
    }
}