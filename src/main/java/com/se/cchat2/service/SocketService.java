//package com.se.cchat2.service;
//
//import com.corundumstudio.socketio.SocketIOClient;
//import com.se.cchat2.controller.UserController;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.concurrent.ExecutionException;
//
//@Service
//@Slf4j
//public class SocketService {
//
//    @Autowired
//    private UserController con;
//
//    public void sendMessage(String room, String eventName, SocketIOClient senderClient, String uid) throws ExecutionException, InterruptedException {
//        for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
//            client.sendEvent(eventName, con.getUserById(uid));
//        }
//    }
//
//}