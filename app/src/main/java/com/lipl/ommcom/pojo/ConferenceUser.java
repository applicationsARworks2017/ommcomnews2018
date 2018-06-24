package com.lipl.ommcom.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by luminousinfoways on 09/10/15.
 */
public class ConferenceUser implements Parcelable {

    private String id;
    private String name;
    private String conference_id;
    private String stream;
    private String user_type;
    private String user_type_id;
    private String is_trash;
    private String is_enable;
    private String is_fullscreen;
    private String is_mute;
    private String is_main_screen;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getConference_id() {
        return conference_id;
    }

    public void setConference_id(String conference_id) {
        this.conference_id = conference_id;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUser_type_id() {
        return user_type_id;
    }

    public void setUser_type_id(String user_type_id) {
        this.user_type_id = user_type_id;
    }

    public String getIs_trash() {
        return is_trash;
    }

    public void setIs_trash(String is_trash) {
        this.is_trash = is_trash;
    }

    public String getIs_enable() {
        return is_enable;
    }

    public void setIs_enable(String is_enable) {
        this.is_enable = is_enable;
    }

    public String getIs_fullscreen() {
        return is_fullscreen;
    }

    public void setIs_fullscreen(String is_fullscreen) {
        this.is_fullscreen = is_fullscreen;
    }

    public String getIs_mute() {
        return is_mute;
    }

    public void setIs_mute(String is_mute) {
        this.is_mute = is_mute;
    }

    public String getIs_main_screen() {
        return is_main_screen;
    }

    public void setIs_main_screen(String is_main_screen) {
        this.is_main_screen = is_main_screen;
    }

    public static final Creator<ConferenceUser> CREATOR = new Creator<ConferenceUser>() {

        @Override
        public ConferenceUser createFromParcel(Parcel source) {
            return new ConferenceUser(source);
        }

        @Override
        public ConferenceUser[] newArray(int size) {
            ConferenceUser[] currentLocations = new ConferenceUser[size];
            return currentLocations;
        }
    };

    public ConferenceUser(Parcel in){
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
        dest.writeString(conference_id);
        dest.writeString(stream);
        dest.writeString(user_type);
        dest.writeString(user_type_id);
        dest.writeString(is_trash);
        dest.writeString(is_enable);
        dest.writeString(is_fullscreen);
        dest.writeString(is_mute);
        dest.writeString(is_main_screen);
        dest.writeString(description);
    }

    private void readFromParcel(Parcel in){
        id = in.readString();
        name = in.readString();
        conference_id = in.readString();
        stream = in.readString();
        user_type = in.readString();
        user_type_id = in.readString();
        is_trash = in.readString();
        is_enable = in.readString();
        is_fullscreen = in.readString();
        is_mute = in.readString();
        is_main_screen = in.readString();
        description = in.readString();
    }
}
