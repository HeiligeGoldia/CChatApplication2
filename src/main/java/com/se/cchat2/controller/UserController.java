package com.se.cchat2.controller;

import com.se.cchat2.entity.User;
import com.se.cchat2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public String createAccount(@RequestBody User newAcc) throws ExecutionException, InterruptedException {
        String uuid = UUID.randomUUID().toString();
        newAcc.setUid(uuid);
        userRepository.create(newAcc);
        return uuid;
    }

    @GetMapping("/getUser/{uid}")
    public User getUserById(@PathVariable("uid") String uid) throws ExecutionException, InterruptedException {
        User u = userRepository.findByUid(uid);
        return u;
    }

}
