package com.messanger.app.messanger.services;

 
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.messanger.app.messanger.dto.ConversationDTO;
import com.messanger.app.messanger.dto.MessageDTO;
import com.messanger.app.messanger.dto.UserDTO;
import com.messanger.app.messanger.entity.AppUser;
import com.messanger.app.messanger.entity.Message;
import com.messanger.app.messanger.repository.MessageRepository;
import com.messanger.app.messanger.repository.UserRepository;

@Service
public class MessageService {

    @Autowired private MessageRepository messageRepo;
    @Autowired private UserRepository userRepo;

    public Message sendMessage(String fromUsername, String toUsername, String content) {
        AppUser sender = userRepo.findByUsername(fromUsername)
            .orElseThrow(() -> new NoSuchElementException("Sender not found"));
        AppUser receiver = userRepo.findByUsername(toUsername)
            .orElseThrow(() -> new NoSuchElementException("Receiver not found"));

        Message m = new Message(sender, receiver, content);
        return messageRepo.save(m);
    }

 public List<MessageDTO> getChatHistorySafe(String me, String other) {
    List<Message> messages = messageRepo.findChatHistory(me, other);
    return messages.stream().map(this::convertToDTO).collect(Collectors.toList());
}


private MessageDTO convertToDTO(Message message) {
    UserDTO sender = toUserDTO(message.getSender());
    UserDTO receiver = toUserDTO(message.getReceiver());

    LocalDateTime localTimestamp = message.getTimestamp()
        .toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();

    return new MessageDTO(sender, receiver, message.getContent(), localTimestamp);
}


private UserDTO toUserDTO(AppUser user) {
    return new UserDTO(
        user.getUsername(),
        user.getFirstName(),
        user.getLastName(),
        user.getBio()
    );
}

 

    public List<ConversationDTO> getConversations(String me) {
    AppUser meUser = userRepo.findByUsername(me)
        .orElseThrow(() -> new NoSuchElementException("User not found"));
    Long myId = meUser.getId();

    List<Message> latest = messageRepo.findLatestPerConversation(myId);

    return latest.stream()
        .map(m -> {
            AppUser partner = m.getSender().equals(meUser)
                ? m.getReceiver()
                : m.getSender();

         LocalDateTime localTime = m.getTimestamp()
            .toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();

            return new ConversationDTO(
                partner.getUsername(),
                partner.getFirstName(),
                partner.getLastName(),
                partner.getBio(),
                m.getContent(),
                localTime
            );
        })
        .collect(Collectors.toList());
}

}

