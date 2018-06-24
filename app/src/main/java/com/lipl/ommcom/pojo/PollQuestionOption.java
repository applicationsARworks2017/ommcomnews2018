package com.lipl.ommcom.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Android Luminous on 5/6/2016.
 */
public class PollQuestionOption implements Parcelable {

    private String name;
    private String id;
    private String question_id;
    private String position;
    private String created_at;
    private String updated_at;

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

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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

    public static final Creator<PollQuestionOption> CREATOR = new Creator<PollQuestionOption>() {

        @Override
        public PollQuestionOption createFromParcel(Parcel source) {
            return new PollQuestionOption(source);
        }

        @Override
        public PollQuestionOption[] newArray(int size) {
            PollQuestionOption[] currentLocations = new PollQuestionOption[size];
            return currentLocations;
        }
    };

    public PollQuestionOption(Parcel in){
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
        dest.writeString(question_id);
        dest.writeString(position);
        dest.writeString(created_at);
        dest.writeString(updated_at);
    }

    private void readFromParcel(Parcel in){

        name = in.readString();
        id = in.readString();
        question_id = in.readString();
        position = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
    }
}
