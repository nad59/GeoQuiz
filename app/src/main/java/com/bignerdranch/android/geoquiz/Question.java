package com.bignerdranch.android.geoquiz;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Nate on 5/15/2017.
 */


public class Question implements Parcelable {
    private int mTextResId;
    private boolean mAnswerTrue;

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public boolean isCheated() {
        return mCheated;
    }

    public void setCheated(boolean cheated) {
        mCheated = cheated;
    }

    private boolean mCheated;

    public boolean isAnswered() {
        return mAnswered;
    }

    public void setAnswered(boolean answered) {
        mAnswered = answered;
    }

    private boolean mAnswered;

    public Question(int textResId,boolean answerTrue){
        mTextResId=textResId;
        mAnswerTrue=answerTrue;
        mAnswered=false;

    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private Question(Parcel in){

        mTextResId = in.readInt();
        mAnswerTrue = in.readByte() != 0;
        mCheated = in.readByte() != 0;
        mAnswered = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mAnswerTrue);
        dest.writeValue(mAnswered);
        dest.writeInt(mTextResId);
    }
}
