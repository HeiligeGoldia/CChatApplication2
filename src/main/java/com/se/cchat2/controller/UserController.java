package com.se.cchat2.controller;

import com.se.cchat2.entity.User;
import com.se.cchat2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public String createAccount1(@RequestBody User newAcc) throws ExecutionException, InterruptedException {
        return userRepository.register1(newAcc);
    }
    @PostMapping("/register/{otp}")
    public String createAccount2(@PathVariable String otp ,@RequestBody User newAcc) throws ExecutionException, InterruptedException, NoSuchAlgorithmException {
        String uuid = UUID.randomUUID().toString();
        newAcc.setUid(uuid);
        return userRepository.register2(newAcc, otp);
    }

    @PostMapping("/login")
    public String login(@RequestBody User newAcc) throws ExecutionException, InterruptedException, NoSuchAlgorithmException {
        User u = userRepository.findBySdt(newAcc.getPhoneNumber());
        if(u.getUid() == null){
            return "Sai thong tin dang nhap";
        }
        else {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(newAcc.getPassword().getBytes());
            byte[] bytes = digest.digest();
            StringBuilder s = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            newAcc.setPassword(s.toString());
            if(u.getPassword().equals(newAcc.getPassword())){
                return u.getUid();
            }
            else {
                return "Sai thong tin dang nhap - "+newAcc.getPassword() + " - "+u.getPassword();
            }
        }
    }

    @GetMapping("/getUser/{uid}")
    public User getUserById(@PathVariable("uid") String uid) throws ExecutionException, InterruptedException {
        return userRepository.findByUid(uid);
    }

    @PutMapping("/updateUser/{uid}")
    public User createAccount(@PathVariable("uid") String uid, @RequestBody User newAcc) throws ExecutionException, InterruptedException, NoSuchAlgorithmException {
        newAcc.setUid(uid);
        userRepository.create(newAcc);
        return newAcc;
    }

    @DeleteMapping("/deleteUser/{uid}")
    public String delete(@PathVariable("uid") String uid) throws ExecutionException, InterruptedException {
        return userRepository.delete(uid);
    }

}
