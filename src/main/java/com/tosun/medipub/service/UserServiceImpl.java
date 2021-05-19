package com.tosun.medipub.service;

import com.tosun.medipub.model.request.UserRequest;
import com.tosun.medipub.model.response.UserRest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.tosun.medipub.controller.UserController.usersMap;

@Service
public class UserServiceImpl implements UserService{

 //   Map<String, UserRest> usersMap;

    @Override
    public UserRest createUser(UserRequest userRequest) {

        UserRest userResponse = new UserRest();
        userResponse.setFirstName(userRequest.getFirstName());
        userResponse.setLastName(userRequest.getLastName());
        userResponse.setEmail(userRequest.getEmail());

        String userId = UUID.randomUUID().toString();
        userResponse.setUserId(userId);

        if (usersMap == null) usersMap = new HashMap<>();
        usersMap.put(userId, userResponse);

        return userResponse;
    }
}
