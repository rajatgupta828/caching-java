package com.rajat.caching.controller;

import com.rajat.caching.entities.User;
import com.rajat.caching.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/")
    public EntityModel<User> saveUser(@RequestBody User user){
        log.info("Inside saveUser method of UserController");
        User returnedUser = userService.saveUser(user);
        return EntityModel.of(returnedUser,
                linkTo(methodOn(UserController.class).getUserByID(returnedUser.getId())).withSelfRel()
                );
    }

    @GetMapping("/user/{id}")
    public EntityModel<User> getUserByID(@PathVariable("id") int userID){
        log.info("Inside getUserByID method of User Controller");
        User returnedUser =  userService.getUserById(userID);
        return EntityModel.of(returnedUser,
                linkTo(methodOn(UserController.class).getUserByID(returnedUser.getId())).withSelfRel()
        );

    }
}
