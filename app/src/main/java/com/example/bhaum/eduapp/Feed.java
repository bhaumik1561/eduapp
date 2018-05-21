package com.example.bhaum.eduapp;

public class Feed {

    private String Author;
    private String Content;

    public Feed(String author, String content) {
        Author = author;
        Content = content;
    }

    public String getAuthor() {
        return Author;
    }

    public String getContent() {
        return Content;
    }

}
