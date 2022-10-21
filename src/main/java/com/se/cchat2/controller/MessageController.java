package com.se.cchat2.controller;

import com.se.cchat2.entity.Message;
import com.se.cchat2.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @MessageMapping("/chat/{cid}")
    public String send(@DestinationVariable String cid, @RequestBody Message newMess){
        return messageRepository.send(cid, newMess);
    }

}
