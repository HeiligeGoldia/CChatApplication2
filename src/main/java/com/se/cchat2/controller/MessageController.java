package com.se.cchat2.controller;

import com.se.cchat2.entity.Member;
import com.se.cchat2.repository.MemberRepository;
import com.se.cchat2.repository.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import com.se.cchat2.entity.Message;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MemberRepository memberRepository;

    @PostMapping("/send")
    public String send(@RequestBody Message newMessage) throws ExecutionException, InterruptedException {
        Member m = memberRepository.getByCidUid(newMessage.getCid(), newMessage.getUid());
        if(m.getMid() == null){
            return "Can not send message";
        }

        String lastId = messageRepository.getLastMessId();
        int newId = Integer.parseInt(lastId) + 1;
        newMessage.setMsid(String.valueOf(newId));
        String ld = LocalDate.now().toString();
        String d = ld.replace("-","/");
        String lt = LocalTime.now().toString();
        String t = lt.substring(0, 5);
        newMessage.setSentTime(d+" - "+t);
        messageRepository.sendMessage(newMessage);
        return newMessage.getContent();
    }

    @GetMapping("/receive/{cid}")
    public List<Message> receive(@PathVariable("cid") String cid) throws ExecutionException, InterruptedException {
        return messageRepository.getMessages(cid);
    }

    @GetMapping("/getLastMessId")
    public String getLastId() throws ExecutionException, InterruptedException {
        return messageRepository.getLastMessId();
    }

    @DeleteMapping("/deleteMessage/{msid}")
    public String deleteMessage(@PathVariable("msid") String msid) throws ExecutionException, InterruptedException {
        return messageRepository.deleteMessage(msid);
    }

}
