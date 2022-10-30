package com.se.cchat2.controller;

import com.se.cchat2.entity.Conversation;
import com.se.cchat2.entity.Friend;
import com.se.cchat2.entity.Member;
import com.se.cchat2.entity.User;
import com.se.cchat2.repository.ConversationRepository;
import com.se.cchat2.repository.FriendRepository;
import com.se.cchat2.repository.MemberRepository;
import com.se.cchat2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class ConversationController {

    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    @GetMapping("/loadConv/{cid}")
    public Conversation findByCid(@PathVariable String cid) throws ExecutionException, InterruptedException {
        return conversationRepository.getById(cid);
    }

    @GetMapping("/loadUserConv/{uid}")
    public List<Conversation> loadUserConv(@PathVariable("uid") String uid) throws ExecutionException, InterruptedException {
        List<Member> listMem = memberRepository.loadUserMem(uid);
        return conversationRepository.loadUserConv(listMem);
    }

    @GetMapping("/loadConvMem/{cid}")
    public List<User> loadConvMem(@PathVariable("cid") String cid) throws ExecutionException, InterruptedException {
        List<Member> listMem = memberRepository.loadConvMem(cid);
        List<User> listUser = new ArrayList<>();
        for(Member m : listMem){
            listUser.add(userRepository.findByUid(m.getUid()));
        }
        return listUser;
    }

    @PostMapping("/newConv")
    public String create(@RequestBody Conversation newConversation) throws ExecutionException, InterruptedException {
        return conversationRepository.create(newConversation);
    }

    @PostMapping("/newDefConv")
    public String createDef() throws ExecutionException, InterruptedException {
        return conversationRepository.createDefault();
    }

    @GetMapping("/checkExistedConv/{fuid}/{uuid}")
    public String checkExistedConv(@PathVariable String fuid, @PathVariable String uuid) throws ExecutionException, InterruptedException {
        List<Conversation> drl = conversationRepository.getAllDirects();
        List<Member> em = new ArrayList<>();
        for(Conversation c : drl){
            Member m = memberRepository.getByCidUid(c.getCid(), fuid);
            Member m2 = memberRepository.getByCidUid(c.getCid(), uuid);
            if(m.getMid()!=null && m2.getMid()!=null){
                em.add(m);
                em.add(m2);
            }
        }
        if(em.isEmpty()){
            return "Not existed";
        }
        else {
            return em.get(0).getCid();
        }
    }

    @GetMapping("/loadFriendsToAddConv/{uid}/{cid}")
    public List<User> loadFriendsToAddConv(@PathVariable String uid, @PathVariable String cid) throws ExecutionException, InterruptedException {
        List<String> fl = friendRepository.findUserFriends(uid);
        List<User> lu = new ArrayList<>();
        for(String s : fl) {
            Member m = memberRepository.getByCidUid(cid, s);
            if(m.getMid()!=null) {
//                fl.remove(s);
            }
            else {
                lu.add(userRepository.findByUid(s));
            }
        }
        return lu;
    }

}
