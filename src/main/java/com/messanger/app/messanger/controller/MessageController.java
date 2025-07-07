package com.messanger.app.messanger.controller;
 
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.messanger.app.messanger.dto.ConversationDTO;
import com.messanger.app.messanger.dto.MessageDTO;
import com.messanger.app.messanger.dto.SendMessageRequest;
import com.messanger.app.messanger.entity.Message;
import com.messanger.app.messanger.services.JwtService;
import com.messanger.app.messanger.services.MessageService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired private JwtService jwtService;
    @Autowired private  MessageService msgService;

     

    // 1. Send a message
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody SendMessageRequest req) {

        String me = jwtService.extractUsername(authHeader.substring(7));
        Message sent = msgService.sendMessage(me, req.getToUsername(), req.getContent());
        return ResponseEntity.ok(Map.of(
            "status", "sent",
            "timestamp", sent.getTimestamp()));
    }
@GetMapping("/chats/{other}")
public ResponseEntity<List<MessageDTO>> getChat(
        @RequestHeader("Authorization") String authHeader,
        @PathVariable String other) {
    String me = jwtService.extractUsername(authHeader.substring(7));
    List<MessageDTO> history = msgService.getChatHistorySafe(me, other);
    return ResponseEntity.ok(history);
}

    

    // 3. List all conversations (with last message)
    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationDTO>> getConversations(
            @RequestHeader("Authorization") String authHeader) {

        String me = jwtService.extractUsername(authHeader.substring(7));
        List<ConversationDTO> convos = msgService.getConversations(me);
        return ResponseEntity.ok(convos);
    }
}

