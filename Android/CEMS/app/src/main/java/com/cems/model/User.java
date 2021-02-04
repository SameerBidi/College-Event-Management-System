package com.cems.model;

public class User {
    private String userID;

    private UserType userType;

    private String username;
    private String password;

    private String rollno;
    private String name;
    private String email;
    private String cno;

    private String year;
    private String branch;

    public User() { super(); }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCno() {
        return cno;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String batch) {
        this.branch = batch;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID='" + userID + '\'' +
                ", userType=" + userType +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", rollno='" + rollno + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", cno='" + cno + '\'' +
                ", year='" + year + '\'' +
                ", branch='" + branch + '\'' +
                '}';
    }
}
