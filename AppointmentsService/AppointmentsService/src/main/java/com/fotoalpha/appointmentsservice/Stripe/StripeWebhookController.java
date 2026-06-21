package com.fotoalpha.appointmentsservice.Stripe;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fotoalpha.appointmentsservice.Service.appService;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@Slf4j
@RestController
@RequestMapping("/stripe-api")
@RequiredArgsConstructor
public class StripeWebhookController {

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;
    private final appService appService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeEvent(
            @RequestHeader("Stripe-Signature") String signature,
            @RequestBody String payload
    ) {
        log.info("Webhook active");
        Event event;

        try {
            event = Webhook.constructEvent(payload, signature, webhookSecret);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        String type = event.getType();
        log.info("Stripe event type: {}", type);

        if (!type.equals("checkout.session.completed")) {
            return ResponseEntity.ok("ignored: " + type);
        }

        try {

            JsonNode root = objectMapper.readTree(payload);
            JsonNode session = root.path("data").path("object");

            String appId = session.path("metadata").path("appId").asText(null);
            String userId = session.path("metadata").path("userId").asText(null);

            log.info("appId: {}, userId: {}", appId, userId);

            if (appId == null) {
                log.warn("Missing appId in metadata");
                return ResponseEntity.ok("missing metadata");
            }else{

                appService.cancelAppointment(userId, appId, true, true);
                log.info("Appointment cancelled with id: {}", appId);
                return ResponseEntity.ok("Cancellation was successful");
            }


        } catch (Exception e) {
            log.error("Webhook processing failed", e);
            return ResponseEntity.internalServerError().body("processing error");
        }

    }
}