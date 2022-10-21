package com.se.cchat2.repository;

import com.se.cchat2.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MessageRepository {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public String send(String cid,Message newMess){
        simpMessagingTemplate.convertAndSend("conversation/messages/"+cid, newMess);
        return newMess.getContent();
    }

}
