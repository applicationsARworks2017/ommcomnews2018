package com.lipl.ommcom.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by luminousinfoways on 09/10/15.
 */
public class Advertisement implements Parcelable {

    private String id;
    private String name;
    private String file_path;
    private String file_type;
    private String section;
    private String url_link;

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getUrl_link() {
        return url_link;
    }

    public void setUrl_link(String url_link) {
        this.url_link = url_link;
    }

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

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public static final Creator<Advertisement> CREATOR = new Creator<Advertisement>() {

        @Override
        public Advertisement createFromParcel(Parcel source) {
            return new Advertisement(source);
        }

        @Override
        public Advertisement[] newArray(int size) {
            Advertisement[] currentLocations = new Advertisement[size];
            return currentLocations;
        }
    };

    public Advertisement(Parcel in){
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
        dest.writeString(section);
        dest.writeString(url_link);
    }

    private void readFromParcel(Parcel in){

        name = in.readString();
        file_path = in.readString();
        id = in.readString();
        file_type = in.readString();
        section = in.readString();
        url_link = in.readString();
    }
}