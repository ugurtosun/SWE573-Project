package com.tosun.medipub.controller;

import com.tosun.medipub.model.request.UserRequest;
import com.tosun.medipub.model.response.UserRest;
import com.tosun.medipub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("users")
public class UserController {

    public static Map<String, UserRest> usersMap;
    UserService userService;

    public UserController(){ }

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping()
    public String getUsers(@RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "limit", defaultValue = "50") int limit){

        return "get users was called with page: " + page + " and limit: " + limit;
    }

    @GetMapping(path = "/{userId}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserRest> getUser(@PathVariable String userId){

        if(usersMap.containsKey(userId)){
            return new ResponseEntity<UserRest>(usersMap.get(userId), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping
    public ResponseEntity<UserRest> createUser(@Valid @RequestBody UserRequest userRequest){

        UserRest userResponse = userService.createUser(userRequest);
        return new ResponseEntity<UserRest>(userResponse, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{userId}")
    public ResponseEntity<UserRest> updateUser(@PathVariable String userId, @Valid @RequestBody UserRequest userRequest){

        if(usersMap != null && usersMap.containsKey(userId)){

            UserRest user = usersMap.get(userId);
            user.setFirstName(userRequest.getFirstName());
            user.setLastName(userRequest.getLastName());
            user.setEmail(userRequest.getEmail());

            usersMap.put(userId, user);

            return new ResponseEntity<UserRest>(user, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<UserRest> deleteUser(@PathVariable String userId){

        if(usersMap.containsKey(userId)){
            UserRest user = usersMap.get(userId);
            usersMap.remove(userId);
            return  new ResponseEntity<UserRest>(user, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
