package com.se.cchat2.controller;

import com.se.cchat2.entity.Member;
import com.se.cchat2.entity.User;
import com.se.cchat2.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    @PostMapping("/newMem")
    public String create(@RequestBody Member newMem) throws ExecutionException, InterruptedException {
        return memberRepository.create(newMem.getCid(), newMem.getUid(), newMem.getRole());
    }

    @PostMapping("/newMems/{cid}")
    public String create(@PathVariable String cid, @RequestBody List<String> newMems) throws ExecutionException, InterruptedException {
        for(String u : newMems){
            memberRepository.create(cid, u, "Member");
        }
        return "Done";
    }

}
