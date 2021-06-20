package com.tosun.medipub.service;

import com.tosun.medipub.model.User;

public interface UserService {

    boolean createUser(User user);

    boolean login(User user);

}
