package com.tosun.medipub.controller;

import com.tosun.medipub.model.User;
import com.tosun.medipub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:*")
@RestController
@RequestMapping("user")
public class UserController {

    public static Map<String, User> usersMap;
    UserService userService;

    public UserController(){ }

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public Object createUser(@Valid @RequestBody User user){

        return userService.createUser(user);
    }

    @CrossOrigin(origins = "http://localhost:34565")
    @PostMapping("/login")
    public boolean loginUser(@Valid @RequestBody User user) {
        return userService.login(user);

    }
}
