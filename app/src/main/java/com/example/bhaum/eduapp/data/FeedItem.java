package com.example.bhaum.eduapp.data;

import java.util.ArrayList;
import java.util.List;

public class FeedItem {


    private int id;
    private int user_id;
    private String name = "PRIT";
    private String status;
    private String ProfilePic;
    private String timeStamp;
    private String attachmentType;
    private String attachement ="https://api.androidhive.info/feed/img/cosmos.jpg";
    private int totalLikes;
    private List<Comments> list_comments = new ArrayList<>();
    private int totalComments = 0;



    public FeedItem(int id, String name, String status, String profilePic, String timeStamp, String attachmentType, String attachement, int totalLikes, int user_id) {
        this.id = id;
        this.name = name;
        this.status = status;
        ProfilePic = profilePic;
        this.timeStamp = timeStamp;
        this.attachmentType = attachmentType;
        this.attachement = attachement;
        this.totalLikes = totalLikes;
        this.user_id = user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public int getTotalLikes() {
        return totalLikes;
    }



    public FeedItem() {
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

    public void addComment(Comments c) { list_comments.add(c) ; }

    public int totalComments() { return list_comments.size(); }
}
