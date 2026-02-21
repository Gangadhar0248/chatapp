package com.chatapp.controller;

import com.chatapp.dto.MessageRequestDTO;
import com.chatapp.dto.MessageResponseDTO;
import com.chatapp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public MessageResponseDTO sendMessage(@RequestBody MessageRequestDTO request) {
        return messageService.save(request.getContent());
    }

    @GetMapping
    public List<MessageResponseDTO> getAllMessages() {
        return messageService.getAll();
    }
}