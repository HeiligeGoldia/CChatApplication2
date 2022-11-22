package com.se.cchat2.controller;

import com.se.cchat2.entity.User;
import com.se.cchat2.repository.UserRepository;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    final String password = "cchat128";

    @PostMapping("/register")
    public String createAccount1(@RequestBody User newAcc) throws ExecutionException, InterruptedException {
        return userRepository.register1(newAcc);
    }

    @PostMapping("/register/{otp}")
    public String createAccount2(@PathVariable String otp ,@RequestBody User newAcc) throws ExecutionException, InterruptedException {
        String uuid = UUID.randomUUID().toString();
        newAcc.setUid(uuid);
        return userRepository.register2(newAcc, otp);
    }

    @PostMapping("/login")
    public String login(@RequestBody User newAcc) throws ExecutionException, InterruptedException {
        User u = userRepository.findBySdt(newAcc.getPhoneNumber());
        if(u.getUid() == null){
            return "Sai thong tin dang nhap";
        }
        else {
            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            encryptor.setPassword(password);
            String decrypted = encryptor.decrypt(u.getPassword());
            u.setPassword(decrypted);
            if(u.getPassword().equals(newAcc.getPassword())){
                return u.getUid();
            }
            else {
                return "Sai thong tin dang nhap";
            }
        }
    }

    @GetMapping("/getUser/{uid}")
    public User getUserById(@PathVariable("uid") String uid) throws ExecutionException, InterruptedException {
        return userRepository.findByUid(uid);
    }

    @PutMapping("/updateUser/{uid}")
    public User updateUser(@PathVariable("uid") String uid, @RequestBody User newAcc) throws ExecutionException, InterruptedException {
        User u = userRepository.findByUid(uid);
        newAcc.setUid(uid);
        newAcc.setPassword(u.getPassword());
        userRepository.updateInfo(newAcc);
        return newAcc;
    }

    @PutMapping("/updatePassword/{uid}")
    public User updatePassword(@PathVariable("uid") String uid, @RequestBody User newAcc) throws ExecutionException, InterruptedException {
        User u = userRepository.findByUid(uid);
        u.setPassword(newAcc.getPassword());
        userRepository.create(u);
        return u;
    }

    @DeleteMapping("/deleteUser/{uid}")
    public String delete(@PathVariable("uid") String uid) throws ExecutionException, InterruptedException {
        return userRepository.delete(uid);
    }

}
