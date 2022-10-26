package com.se.cchat2.controller;

import com.se.cchat2.entity.Conversation;
import com.se.cchat2.entity.Member;
import com.se.cchat2.repository.ConversationRepository;
import com.se.cchat2.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class ConversationController {

    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/loadConv/{cid}")
    public Conversation findByCid(@PathVariable String cid) throws ExecutionException, InterruptedException {
        return conversationRepository.getById(cid);
    }

    @GetMapping("/loadUserConv/{uid}")
    public List<Conversation> loadUserConv(@PathVariable("uid") String uid) throws ExecutionException, InterruptedException {
        List<Member> listMem = memberRepository.loadUserMem(uid);
        return conversationRepository.loadUserConv(listMem);
    }

}
