package com.lipl.ommcom.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by luminousinfoways on 09/10/15.
 */
public class CitizenJournalistVideos implements Parcelable {

    private String name;
    private String file_path;
    private String id;
    private String file_type;
    private String description;
    private String slug;
    private String is_anonymous;
    private String news_count;
    private String updated_at;
    private String long_description;

    public String getLong_description() {
        return long_description;
    }

    public void setLong_description(String long_description) {
        this.long_description = long_description;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getNews_count() {
        return news_count;
    }

    public void setNews_count(String news_count) {
        this.news_count = news_count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getIs_anonymous() {
        return is_anonymous;
    }

    public void setIs_anonymous(String is_anonymous) {
        this.is_anonymous = is_anonymous;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public static final Creator<CitizenJournalistVideos> CREATOR = new Creator<CitizenJournalistVideos>() {

        @Override
        public CitizenJournalistVideos createFromParcel(Parcel source) {
            return new CitizenJournalistVideos(source);
        }

        @Override
        public CitizenJournalistVideos[] newArray(int size) {
            CitizenJournalistVideos[] currentLocations = new CitizenJournalistVideos[size];
            return currentLocations;
        }
    };

    public CitizenJournalistVideos(Parcel in){
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(file_path);
        dest.writeString(id);
        dest.writeString(file_type);
        dest.writeString(description);
        dest.writeString(slug);
        dest.writeString(is_anonymous);
        dest.writeString(news_count);
        dest.writeString(updated_at);
        dest.writeString(long_description);
    }

    private void readFromParcel(Parcel in){

        name = in.readString();
        file_path = in.readString();
        id = in.readString();
        file_type = in.readString();
        description = in.readString();
        slug = in.readString();
        is_anonymous = in.readString();
        news_count = in.readString();
        updated_at = in.readString();
        long_description = in.readString();
    }
}