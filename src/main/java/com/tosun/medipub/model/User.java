package com.tosun.medipub.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class User {

    private String userID;

    @NotNull(message = "Username cannot be null")
    private String userName;

    @Email(message = "Wrong email address format")
    private String emailAddress;

    @NotNull(message = "Password cannot be null")
    @Size(min = 4, max = 16, message = "Password length should in range [4, 16]")
    private String password;

    public User() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
