package com.example.bhaum.eduapp.data;

public class FeedItem {


    private int id;
    private String name = "PRIT";
    private String status;
    private String ProfilePic;
    private String timeStamp;
    private String attachmentType;
    private String attachement ="https://api.androidhive.info/feed/img/cosmos.jpg";

    public FeedItem() {
    }

    public FeedItem(int id, String name, String status, String profilePic, String timeStamp, String attachmentType, String attachement) {
        this.id = id;
        this.name = name;
        this.status = status;
        ProfilePic = profilePic;
        this.timeStamp = timeStamp;
        this.attachmentType = attachmentType;
        this.attachement = attachement;
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

}
