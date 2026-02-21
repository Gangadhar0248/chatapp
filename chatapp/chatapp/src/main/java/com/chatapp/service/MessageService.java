
package com.chatapp.service;

import com.chatapp.dto.MessageResponseDTO;
import com.chatapp.dto.UserResponseDTO;
import com.chatapp.entity.Message;
import com.chatapp.entity.User;
import com.chatapp.repository.MessageRepository;
import com.chatapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    // SAVE METHOD
    public MessageResponseDTO save(String content) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        // FIX IS HERE: We use .orElseThrow() to unwrap the Optional<User>
        User sender = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        Message message = new Message();
        message.setContent(content);
        message.setSender(sender);
        message.setTimestamp(LocalDateTime.now());

        Message savedMessage = messageRepository.save(message);

        return convertToDto(savedMessage);
    }

    // GET ALL METHOD
    public List<MessageResponseDTO> getAll() {
        return messageRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    // HELPER METHOD
    private MessageResponseDTO convertToDto(Message message) {
        UserResponseDTO userDTO = new UserResponseDTO();
        userDTO.setId(message.getSender().getId());
        userDTO.setUsername(message.getSender().getUsername());
        userDTO.setEmail(message.getSender().getEmail());

        MessageResponseDTO dto = new MessageResponseDTO();
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setTimestamp(message.getTimestamp().toString());
        dto.setSender(userDTO);

        return dto;
    }
}
