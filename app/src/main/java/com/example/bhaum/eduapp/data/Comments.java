package com.example.bhaum.eduapp.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Comments implements Parcelable {

    private int comment_id;
    private int news_id;
    private int user_id;
    private String description;
    private String timestamp;
    private String commented_by;
    private String c_profile_pic = "male.png";

    public Comments(){
    }
    public Comments(int comment_id, int news_id, int user_id, String description, String timestamp) {
        this.comment_id = comment_id;
        this.news_id = news_id;
        this.user_id = user_id;
        this.description = description;
        this.timestamp = timestamp;
    }


    protected Comments(Parcel in) {
        comment_id = in.readInt();
        news_id = in.readInt();
        user_id = in.readInt();
        description = in.readString();
        timestamp = in.readString();
        commented_by = in.readString();
        c_profile_pic = in.readString();
    }

    public static final Creator<Comments> CREATOR = new Creator<Comments>() {
        @Override
        public Comments createFromParcel(Parcel in) {
            return new Comments(in);
        }

        @Override
        public Comments[] newArray(int size) {
            return new Comments[size];
        }
    };

    public void setCommented_by(String commented_by) {
        this.commented_by = commented_by;
    }
    public void setC_profile_pic(String c_profile_pic) {
        this.c_profile_pic = c_profile_pic;
    }
    public String getCommented_by() {
        return commented_by;
    }

    public String getC_profile_pic() {
        return c_profile_pic;
    }
    public int getComment_id() {
        return comment_id;
    }

    public int getNews_id() {
        return news_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getDescription() {
        return description;
    }

    public String getTimestamp() {
        return timestamp;
    }


    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public void setNews_id(int news_id) {
        this.news_id = news_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(comment_id);
        dest.writeInt(news_id);
        dest.writeInt(user_id);
        dest.writeString(description);
        dest.writeString(timestamp);
        dest.writeString(commented_by);
        dest.writeString(c_profile_pic);
    }
}
