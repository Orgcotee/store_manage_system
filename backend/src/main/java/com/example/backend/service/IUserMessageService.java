package com.example.backend.service;

import com.example.backend.entity.UserMessage;

import java.util.List;

public interface IUserMessageService {
    List<UserMessage> getMessagesByUserId(Integer userId);
    UserMessage saveMessage(UserMessage userMessage);
}    