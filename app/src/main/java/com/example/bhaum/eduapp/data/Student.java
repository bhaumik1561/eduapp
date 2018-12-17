package com.example.bhaum.eduapp.data;

public class Student {

    int roll_no;
    int user_id;
    String user_type;
    String name;
    boolean attend_status;


    public Student(){

    }
    public Student(int roll_no, int user_id, String user_type, String name, boolean attend_status) {
        this.roll_no = roll_no;
        this.user_id = user_id;
        this.user_type = user_type;
        this.name = name;
        this.attend_status = attend_status;
    }

    public int getRoll_no() {
        return roll_no;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getName() {
        return name;
    }


    public void setRoll_no(int roll_no) {
        this.roll_no = roll_no;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAttend_status(boolean attend_status) {
        this.attend_status = attend_status;
    }

    public boolean isAttend_status() {
        return attend_status;
    }
}
