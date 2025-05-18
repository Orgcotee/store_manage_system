package com.example.backend.service.impl;



import com.example.backend.entity.UserMessage;
import com.example.backend.service.IUserMessageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMessageServiceImpl implements IUserMessageService {

    @Override
    public List<UserMessage> getMessagesByUserId(Integer userId) {
        return null;
    }

    @Override
    public UserMessage saveMessage(UserMessage userMessage) {
        return userMessage;
    }
}    