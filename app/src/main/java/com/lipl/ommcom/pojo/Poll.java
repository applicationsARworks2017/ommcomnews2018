package com.lipl.ommcom.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Luminous on 5/6/2016.
 */
public class Poll implements Parcelable {

    private PollQuestion question;
    private List<PollAnswer> answerList;

    public PollQuestion getQuestion() {
        return question;
    }

    public void setQuestion(PollQuestion question) {
        this.question = question;
    }

    public List<PollAnswer> getAnswer() {
        return answerList;
    }

    public void setAnswer(List<PollAnswer> answer) {
        this.answerList = answer;
    }

    public static final Creator<Poll> CREATOR = new Creator<Poll>() {

        @Override
        public Poll createFromParcel(Parcel source) {
            return new Poll(source);
        }

        @Override
        public Poll[] newArray(int size) {
            Poll[] currentLocations = new Poll[size];
            return currentLocations;
        }
    };

    public Poll(Parcel in){
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeParcelable(question, flags);
        dest.writeTypedList(answerList);
    }

    private void readFromParcel(Parcel in){

        question = (PollQuestion)in.readParcelable(PollQuestion.class.getClassLoader());
        answerList = new ArrayList<PollAnswer>();
        in.readTypedList(answerList, PollAnswer.CREATOR);
    }
}
