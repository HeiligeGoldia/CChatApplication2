package com.se.cchat2.controller;

import com.se.cchat2.entity.Conversation;
import com.se.cchat2.entity.Member;
import com.se.cchat2.entity.User;
import com.se.cchat2.repository.*;
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
    private MessageRepository messageRepository;

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
        List<Member> listMem = memberRepository.loadConvRoleOwner(cid);
        List<Member> mems = memberRepository.loadConvRoleMember(cid);
        listMem.addAll(mems);
        List<User> listUser = new ArrayList<>();
        for(Member m : listMem){
            listUser.add(userRepository.findByUid(m.getUid()));
        }
        return listUser;
    }

    @GetMapping("/loadConvRoleMem/{cid}")
    public List<User> loadConvRoleMem(@PathVariable("cid") String cid) throws ExecutionException, InterruptedException {
        List<Member> listMem = memberRepository.loadConvRoleMember(cid);
        List<User> listUser = new ArrayList<>();
        for(Member m : listMem){
            listUser.add(userRepository.findByUid(m.getUid()));
        }
        return listUser;
    }

    @GetMapping("/loadConvRoleOwner/{cid}")
    public List<User> loadConvRoleOwner(@PathVariable("cid") String cid) throws ExecutionException, InterruptedException {
        List<Member> listMem = memberRepository.loadConvRoleOwner(cid);
        List<User> listUser = new ArrayList<>();
        for(Member m : listMem){
            listUser.add(userRepository.findByUid(m.getUid()));
        }
        return listUser;
    }

    @PostMapping("/newConv/{uid}")
    public String create(@PathVariable String uid, @RequestBody Conversation newConversation) throws ExecutionException, InterruptedException {
        String cv = conversationRepository.create(newConversation);
        memberRepository.create(cv,uid,"Owner");
        return cv;
    }

    @GetMapping("/startDirectChat/{fuid}/{uuid}")
    public String startDirectChat(@PathVariable String fuid, @PathVariable String uuid) throws ExecutionException, InterruptedException {
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
            String defcid = conversationRepository.createDefault();
            memberRepository.create(defcid, uuid, "D_Member");
            memberRepository.create(defcid, fuid, "D_Member");
            return defcid;
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

    @DeleteMapping("/deleteGroup/{cid}")
    public String deleteGroup(@PathVariable("cid") String cid) throws ExecutionException, InterruptedException {
        List<Member> listMem = memberRepository.loadConvMem(cid);
        for (Member m : listMem){
            memberRepository.deleteMember(m);
        }

        return conversationRepository.deleteConv(cid);
    }

    @PutMapping("/updateConvName/{cid}/{newName}")
    public String updateConvName(@PathVariable("cid") String cid, @PathVariable("newName") String newName) throws ExecutionException, InterruptedException {
        Conversation c = conversationRepository.getById(cid);
        c.setName(newName);
        return conversationRepository.updateConv(c);
    }

}
