package com.accuresoftech.abc.chatbot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
public class AIService {

    @Value("${ai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public String askAI(String message) {

        try {

            String url = "https://openrouter.ai/api/v1/chat/completions";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            // Recommended by OpenRouter
            headers.set("HTTP-Referer", "http://localhost:8080");
            headers.set("X-Title", "ABC CRM Chatbot");

            Map<String, Object> body = new HashMap<>();

            body.put("model", "openai/gpt-3.5-turbo");

            List<Map<String, String>> messages = new ArrayList<>();

            messages.add(Map.of(
                    "role", "system",
                    "content", "You are an assistant helping users understand ABC CRM."
            ));

            messages.add(Map.of(
                    "role", "user",
                    "content", message
            ));

            body.put("messages", messages);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            JsonNode json = mapper.readTree(response.getBody());

            return json
                    .get("choices")
                    .get(0)
                    .get("message")
                    .get("content")
                    .asText();

        } catch (Exception e) {

            log.error("AI API error:", e);

            return "AI assistant is temporarily unavailable. Please contact support.";
        }
    }
}