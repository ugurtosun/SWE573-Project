package com.tosun.medipub.controller;

import com.tosun.medipub.model.request.UserRequest;
import com.tosun.medipub.model.response.UserRest;
import com.tosun.medipub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("user")
public class UserController {

    public static Map<String, UserRest> usersMap;
    UserService userService;

    public UserController(){ }

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRest> createUser(@Valid @RequestBody UserRequest userRequest){

        UserRest userResponse = userService.createUser(userRequest);
        return new ResponseEntity<UserRest>(userResponse, HttpStatus.CREATED);
    }

}
