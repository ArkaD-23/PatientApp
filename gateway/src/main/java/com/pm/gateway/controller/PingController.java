package com.pm.gateway.controller;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PingController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/ping")
    public String ping() {

        final String appointmentServiceUrl = "https://appointment-service-l1wg.onrender.com";

        final String authServiceUrl = "https://auth-service-y5m5.onrender.com";

        final String userServiceUrl = "https://user-service-kbh0.onrender.com";

        final String chatServiceUrl = "https://chat-service-qtsr.onrender.com";

        try {
            ResponseEntity<String> appointmentResponse = restTemplate.exchange(
                    appointmentServiceUrl + "/ping",
                    HttpMethod.GET,
                    null,
                    String.class
            );

            ResponseEntity<String> authResponse = restTemplate.exchange(
                    authServiceUrl + "/ping",
                    HttpMethod.GET,
                    null,
                    String.class
            );

            ResponseEntity<String> userResponse = restTemplate.exchange(
                    userServiceUrl + "/ping",
                    HttpMethod.GET,
                    null,
                    String.class
            );

            ResponseEntity<String> chatResponse = restTemplate.exchange(
                    chatServiceUrl + "/ping",
                    HttpMethod.GET,
                    null,
                    String.class
            );

        } catch (Exception e) {
            return "Something went wrong !";
        }

        return "All service are alive!";
    }
}

