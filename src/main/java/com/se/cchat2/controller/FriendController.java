package com.se.cchat2.controller;

import com.se.cchat2.entity.Friend;
import com.se.cchat2.repository.FriendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class FriendController {

    @Autowired
    private FriendRepository friendRepository;

    @PostMapping("/addFriend")
    public String add(@RequestBody Friend newFriend) throws ExecutionException, InterruptedException {
        int newFid = Integer.parseInt(friendRepository.getLastFriendId()) + 1;
        String fid = String.valueOf(newFid);
        newFriend.setFid(fid);
        newFriend.setStatus("Pending");
        return friendRepository.create(newFriend);
    }

    @PutMapping("/acceptRequest/{fid}")
    public String accept(@PathVariable String fid) throws ExecutionException, InterruptedException {
        Friend newFriend = friendRepository.getById(fid);
        if(newFriend.getFid() == null){
            return "Friend not found";
        }
        else {
            newFriend.setFid(fid);
            newFriend.setStatus("Friend");
            return friendRepository.create(newFriend);
        }
    }

    @DeleteMapping("/deleteRequest/{fid}")
    public String deleteRequest(@PathVariable String fid) throws ExecutionException, InterruptedException {
        return friendRepository.delete(fid);
    }

    @DeleteMapping("/removeFriend")
    public String removeFriend(@RequestBody Friend uids) throws ExecutionException, InterruptedException {
        Friend f = friendRepository.findBy2Uid(uids.getUid1(), uids.getUid2());
        if(f.getFid() == null){
            return "Delete failed";
        }
        return friendRepository.delete(f.getFid());
    }

    @GetMapping("/findUserFriends/{uid}")
    public List<String> findUserFriends(@PathVariable String uid) throws ExecutionException, InterruptedException {
        return friendRepository.findUserFriends(uid);
    }

}
