package com.se.cchat2.controller;

import com.se.cchat2.entity.Member;
import com.se.cchat2.entity.Message;
import com.se.cchat2.entity.User;
import com.se.cchat2.repository.MemberRepository;
import com.se.cchat2.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MessageRepository messageRepository;

//    @PostMapping("/newMem")
//    public String create(@RequestBody Member newMem) throws ExecutionException, InterruptedException {
//        return memberRepository.create(newMem.getCid(), newMem.getUid(), newMem.getRole());
//    }

    @PostMapping("/newMems/{cid}")
    public String create(@PathVariable String cid, @RequestBody List<String> newMems) throws ExecutionException, InterruptedException {
        for(String u : newMems){
            memberRepository.create(cid, u, "Member");
        }
        return "Done";
    }

    @GetMapping("/getUserRole/{uid}/{cid}")
    public String getUserRole(@PathVariable String uid, @PathVariable String cid) throws ExecutionException, InterruptedException {
        return memberRepository.getByCidUid(cid,uid).getRole();
    }

    @DeleteMapping("/leaveGroup/{uid}/{cid}")
    public String leaveGroup(@PathVariable String uid, @PathVariable String cid) throws ExecutionException, InterruptedException {
        Member m = memberRepository.getByCidUid(cid, uid);
        if(m.getRole().equals("Owner")){
            return "Owner is not allowed to leave group";
        }
        else {
            List<Message> list = messageRepository.getMessageId(cid, uid);
            for (Message idn : list){
                messageRepository.deleteMessage(idn.getMsid());
            }
            return memberRepository.deleteMember(m);
        }
    }

    @PutMapping("/nRole/{cid}/{uid}/{muid}")
    public String nRole(@PathVariable String cid, @PathVariable String uid, @PathVariable String muid) throws ExecutionException, InterruptedException {
        return memberRepository.nRole(cid, uid, muid);
    }

}
