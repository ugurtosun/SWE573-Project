package com.tosun.medipub.model.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserRequest {

    @NotNull(message = "Username cannot be null")
    private String userName;

    @Email(message = "Wrong email address format")
    private String email;

    @NotNull(message = "Password cannot be null")
    @Size(min = 4, max = 16, message = "Password length should in range [4, 16]")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
