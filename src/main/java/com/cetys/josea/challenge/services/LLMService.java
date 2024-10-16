package com.cetys.josea.challenge.services;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class LLMService {

    private final ChatClient chatClient;

    public LLMService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String getFriendlyExplanation(String productJson) {
        String prompt = "Explica de forma amigable para un usuario este producto: " + productJson;

        // Llamar a la API de OpenAI utilizando el ChatClient
        return chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
    }
}

