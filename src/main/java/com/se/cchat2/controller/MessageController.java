package com.se.cchat2.controller;

import com.se.cchat2.repository.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import com.se.cchat2.entity.Message;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @PostMapping("/send")
    public String send(@RequestBody Message newMessage) throws ExecutionException, InterruptedException {
        messageRepository.sendMessage(newMessage);
        return newMessage.getContent();
    }

    @GetMapping("/receive/{cid}")
    public List<Message> receive(@PathVariable("cid") String cid) throws ExecutionException, InterruptedException {
        return messageRepository.getMessages(cid);
    }

}
