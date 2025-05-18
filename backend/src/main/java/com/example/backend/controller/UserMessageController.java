package com.example.backend.controller;

import com.example.backend.entity.UserMessage;
import com.example.backend.service.IUserMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userMessage")
@RequiredArgsConstructor
public class UserMessageController {
    private final IUserMessageService userMessageService;

    @GetMapping("/list")
    public List<UserMessage> getMessagesByUserId(@RequestParam("userId") Integer userId ) {
        return userMessageService.getMessagesByUserId(userId);
    }

    @PostMapping
    public UserMessage saveMessage(@RequestBody UserMessage userMessage) {
        return userMessageService.saveMessage(userMessage);
    }
}    