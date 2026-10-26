package com.teamlift.scheduler.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class GeminiAiService {

    @Value("${gemini.api.key:}")
    private String apiKey;

    @Value("${gemini.model:gemini-2.5-flash}")
    private String model;

    private final RestClient restClient = RestClient.create();

    public String generateConfirmation(String name, String topic) {

        if (apiKey == null || apiKey.isBlank()) {
            return "Your booking is confirmed, " + name
                    + "! We look forward to discussing " + topic + ".";
        }

        try {
            String prompt = "Write a friendly booking confirmation in English "
                    + "for " + name + " who booked a session about " + topic
                    + ". Use at most two short sentences. Do not use emojis.";

            Map<?, ?> response = restClient.post()
                    .uri("https://generativelanguage.googleapis.com/v1beta/models/"
                            + model + ":generateContent?key=" + apiKey)
                    .body(Map.of(
                            "contents", new Object[]{
                                    Map.of("parts", new Object[]{
                                            Map.of("text", prompt)
                                    })
                            }
                    ))
                    .retrieve()
                    .body(Map.class);

            if (response != null) {
                var candidates = (java.util.List<?>) response.get("candidates");

                if (candidates != null && !candidates.isEmpty()) {
                    var candidate = (Map<?, ?>) candidates.get(0);
                    var content = (Map<?, ?>) candidate.get("content");
                    var parts = (java.util.List<?>) content.get("parts");
                    var firstPart = (Map<?, ?>) parts.get(0);

                    Object text = firstPart.get("text");

                    if (text != null) {
                        return text.toString();
                    }
                }
            }
        } catch (Exception exception) {
            System.err.println("Gemini request failed: "
                    + exception.getMessage());
        }

        return "Your booking is confirmed, " + name
                + "! We look forward to discussing " + topic + ".";
    }
}