package com.cetys.josea.challenge.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class LLMService {
    @Value("${openai.api.key}")
    private String apiKey;
    private final RestTemplate restTemplate;

    public LLMService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String explainProduct(String productJson) {
        String url = "https://api.openai.com/v1/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        String prompt = "Explica de forma amigable para un usuario este producto: " + productJson;

        String requestBody = "{"
                + "\"model\": \"gpt-3.5-turbo\","
                + "\"prompt\": \"" + prompt + "\","
                + "\"max_tokens\": 200"
                + "}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        return restTemplate.postForObject(url, entity, String.class);
    }
}
