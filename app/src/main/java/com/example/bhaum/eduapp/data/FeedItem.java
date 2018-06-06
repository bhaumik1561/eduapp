package com.example.bhaum.eduapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FeedItem implements Parcelable{


    private int id;
    private int user_id;
    private String name = "PRIT";
    private String status;
    private String ProfilePic;
    private String timeStamp;
    private String attachmentType;
    private String attachement ="https://api.androidhive.info/feed/img/cosmos.jpg";


    private int totalLikes= 0;
    private int totalComments = 0;


    private List<Comments> list_comments = new ArrayList<Comments>();


    protected FeedItem(Parcel in) {
        id = in.readInt();
        user_id = in.readInt();
        name = in.readString();
        status = in.readString();
        ProfilePic = in.readString();
        timeStamp = in.readString();
        attachmentType = in.readString();
        attachement = in.readString();
        totalLikes = in.readInt();
        totalComments = in.readInt();
        liked_by =  new LinkedHashMap<Integer, String>();
        in.readMap(liked_by, String.class.getClassLoader());
        list_comments = new ArrayList<Comments>();
        list_comments = in.readArrayList(Comments.class.getClassLoader());
    }

    public static final Creator<FeedItem> CREATOR = new Creator<FeedItem>() {
        @Override
        public FeedItem createFromParcel(Parcel in) {
            return new FeedItem(in);
        }

        @Override
        public FeedItem[] newArray(int size) {
            return new FeedItem[size];
        }
    };

    public Map<Integer, String> getLiked_by() {
        return liked_by;
    }

    private Map<Integer,String> liked_by = new LinkedHashMap<>();

    public FeedItem() {
    }


    public FeedItem(int id, String name, String status, String profilePic, String timeStamp, String attachmentType, String attachement, int totalLikes, int user_id, int totalComments) {
        this.id = id;
        this.name = name;
        this.status = status;
        ProfilePic = profilePic;
        this.timeStamp = timeStamp;
        this.attachmentType = attachmentType;
        this.attachement = attachement;
        this.totalLikes = totalLikes;
        this.totalComments = totalComments;
        this.user_id = user_id;
    }





    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }






    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public String getAttachement() {
        return attachement;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public void setAttachement(String attachement) {
        this.attachement = attachement;
    }

    public void addComment(Comments p) { list_comments.add(p) ; }

    public int getTotalLikes() {
        totalLikes = liked_by.size();
        return totalLikes;
    }

    public int getTotalComments() {
        totalComments = list_comments.size();
        return totalComments;
    }


    public void addLiked_by(int usr, String name){ liked_by.put(usr, name); }

    public void removeLiked_by(int usr){ liked_by.remove(usr);}



    public List<Comments> getList_comments() {
        return list_comments;
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
        dest.writeInt(id);
        dest.writeInt(user_id);
        dest.writeString(name);
        dest.writeString(status);
        dest.writeString(ProfilePic);
        dest.writeString(timeStamp);
        dest.writeString(attachmentType);
        dest.writeString(attachement);
        dest.writeInt(totalLikes);
        dest.writeInt(totalComments);
        dest.writeMap(liked_by);
        dest.writeList(list_comments);
    }
}
