package com.accuresoftech.abc.chatbot;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.accuresoftech.abc.chatbot.ChatRequest;
import com.accuresoftech.abc.chatbot.ChatResponse;
import com.accuresoftech.abc.chatbot.ChatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/public/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {

        ChatResponse response = chatService.handleMessage(request.getMessage());

        return ResponseEntity.ok(response);
    }

}