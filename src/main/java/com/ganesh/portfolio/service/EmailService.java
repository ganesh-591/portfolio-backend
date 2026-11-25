
package com.ganesh.portfolio.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ganesh.portfolio.entity.Contact;

import tools.jackson.databind.ObjectMapper;

@Service
public class EmailService {

    private static final Logger log =
            LoggerFactory.getLogger(EmailService.class);

    private static final String RESEND_URL =
            "https://api.resend.com/emails";

    private final ObjectMapper objectMapper;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Value("${RESEND_API_KEY:}")
    private String resendApiKey;

    @Value("${MAIL_USERNAME}")
    private String recipientEmail;

    public EmailService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void sendContactNotification(Contact contact) {

        if (resendApiKey == null || resendApiKey.isBlank()) {
            log.warn("Resend API key is missing. Email notification skipped.");
            return;
        }

        try {
            Map<String, Object> emailData = Map.of(
                    "from", "Portfolio Contact <onboarding@resend.dev>",
                    "to", List.of(recipientEmail),
                    "subject", "New Portfolio Contact: " + contact.getSubject(),
                    "text",
                    "You received a new message from your portfolio!\n\n"
                    + "Name: " + contact.getName() + "\n"
                    + "Email: " + contact.getEmail() + "\n"
                    + "Subject: " + contact.getSubject() + "\n\n"
                    + "Message:\n" + contact.getMessage()
            );

            String requestBody =
                    objectMapper.writeValueAsString(emailData);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(RESEND_URL))
                    .timeout(Duration.ofSeconds(20))
                    .header("Authorization", "Bearer " + resendApiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() >= 200
                    && response.statusCode() < 300) {
                log.info("Contact notification email sent successfully.");
            } else {
                log.error("Resend email failed. Status: {}, Response: {}",
                        response.statusCode(), response.body());
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Resend email request was interrupted.", e);

        } catch (Exception e) {
            log.error("Failed to send contact notification through Resend.", e);
        }
    }
}
