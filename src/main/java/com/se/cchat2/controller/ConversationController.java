package com.se.cchat2.controller;

import com.se.cchat2.entity.Conversation;
import com.se.cchat2.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class ConversationController {

    @Autowired
    private ConversationRepository repository;

    @GetMapping("/getConv/{cid}")
    public Conversation findByCid(@PathVariable String cid) throws ExecutionException, InterruptedException {
        return repository.getById(cid);
    }

}
