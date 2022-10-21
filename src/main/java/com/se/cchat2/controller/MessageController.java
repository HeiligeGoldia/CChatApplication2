package com.se.cchat2.controller;

import com.se.cchat2.repository.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.se.cchat2.entity.Message;

@RestController
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;



}
