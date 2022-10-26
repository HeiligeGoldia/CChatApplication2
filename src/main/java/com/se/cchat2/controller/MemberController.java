package com.se.cchat2.controller;

import com.se.cchat2.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

}
