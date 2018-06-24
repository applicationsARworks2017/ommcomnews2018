package com.lipl.ommcom.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by luminousinfoways on 09/10/15.
 */
public class NewsVideo implements Parcelable {

    private String id;
    private String position;
    private String is_enable;
    private String is_trash;
    private String updated_at;
    private String created_at;
    private String name;
    private String is_video;
    private String is_audio;
    private String file_link;
    private String news_id;

    public String getIs_video() {
        return is_video;
    }

    public void setIs_video(String is_video) {
        this.is_video = is_video;
    }

    public String getIs_audio() {
        return is_audio;
    }

    public void setIs_audio(String is_audio) {
        this.is_audio = is_audio;
    }

    public String getFile_link() {
        return file_link;
    }

    public void setFile_link(String file_link) {
        this.file_link = file_link;
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getIs_enable() {
        return is_enable;
    }

    public void setIs_enable(String is_enable) {
        this.is_enable = is_enable;
    }

    public String getIs_trash() {
        return is_trash;
    }

    public void setIs_trash(String is_trash) {
        this.is_trash = is_trash;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public static final Creator<NewsVideo> CREATOR = new Creator<NewsVideo>() {

        @Override
        public NewsVideo createFromParcel(Parcel source) {
            return new NewsVideo(source);
        }

        @Override
        public NewsVideo[] newArray(int size) {
            NewsVideo[] currentLocations = new NewsVideo[size];
            return currentLocations;
        }
    };

    public NewsVideo(Parcel in){
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(position);
        dest.writeString(is_enable);
        dest.writeString(is_trash);
        dest.writeString(updated_at);
        dest.writeString(created_at);
        dest.writeString(is_video);
        dest.writeString(is_audio);
        dest.writeString(file_link);
        dest.writeString(news_id);
    }

    private void readFromParcel(Parcel in){

        name = in.readString();
        id = in.readString();
        position = in.readString();
        is_enable = in.readString();
        is_trash = in.readString();
        updated_at = in.readString();
        created_at = in.readString();
        is_video = in.readString();
        is_audio = in.readString();
        file_link = in.readString();
        news_id = in.readString();
    }
}