package com.example.backend.handler;

import com.example.backend.entity.UserMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    private static final Map<String, WebSocketSession> sessions = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String token = (String) session.getAttributes().get("token");
        if (token != null) {
            sessions.put(token, session);
        }
        System.out.println("Connection established: " + token);
    }

    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        // 这里可以根据业务需求处理客户端发送的消息
        System.out.println("Received message from client: " + payload);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, @NotNull org.springframework.web.socket.CloseStatus status) throws Exception {
        String token = (String) session.getAttributes().get("token");
        if (token != null) {
            sessions.remove(token);
        }
        System.out.println("Connection closed: " + token);
    }

    public void sendMessageToUserByToken(String token, UserMessage userMessage) {
        WebSocketSession session = sessions.get(token);
        if (session != null && session.isOpen()) {
            try {
                String jsonMessage = objectMapper.writeValueAsString(userMessage);
                session.sendMessage(new TextMessage(jsonMessage));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}