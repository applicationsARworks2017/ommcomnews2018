package com.lipl.ommcom.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Luminous on 5/6/2016.
 */
public class PollQuestion implements Parcelable {

    private String name;
    private String id;
    private String is_active;
    private String is_statitics_to_public;
    private String is_trash;
    private String created_at;
    private String updated_at;
    private List<PollQuestionOption> pollQuestionOptionList;

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getIs_statitics_to_public() {
        return is_statitics_to_public;
    }

    public void setIs_statitics_to_public(String is_statitics_to_public) {
        this.is_statitics_to_public = is_statitics_to_public;
    }

    public String getIs_trash() {
        return is_trash;
    }

    public void setIs_trash(String is_trash) {
        this.is_trash = is_trash;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public List<PollQuestionOption> getPollQuestionOptionList() {
        return pollQuestionOptionList;
    }

    public void setPollQuestionOptionList(List<PollQuestionOption> pollQuestionOptionList) {
        this.pollQuestionOptionList = pollQuestionOptionList;
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

    public static final Creator<PollQuestion> CREATOR = new Creator<PollQuestion>() {

        @Override
        public PollQuestion createFromParcel(Parcel source) {
            return new PollQuestion(source);
        }

        @Override
        public PollQuestion[] newArray(int size) {
            PollQuestion[] currentLocations = new PollQuestion[size];
            return currentLocations;
        }
    };

    public PollQuestion(Parcel in){
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
        dest.writeString(is_active);
        dest.writeString(is_statitics_to_public);
        dest.writeString(is_trash);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeTypedList(pollQuestionOptionList);
    }

    private void readFromParcel(Parcel in){

        name = in.readString();
        id = in.readString();
        is_active = in.readString();
        is_statitics_to_public = in.readString();
        is_trash = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        pollQuestionOptionList = new ArrayList<PollQuestionOption>();
        in.readTypedList(pollQuestionOptionList, PollQuestionOption.CREATOR);
    }
}
