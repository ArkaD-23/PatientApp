package com.pm.gateway.controller;

import com.pm.gateway.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${user.service.url}")
    private String userServiceUrl;

    @GetMapping("/{email}/{token}")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable String email,
                                                 @PathVariable String token) {
        try {

            ResponseEntity<ProfileDto> response = restTemplate.exchange(
                    userServiceUrl + "/" + email + "/" + token,
                    HttpMethod.GET,
                    null,
                    ProfileDto.class
            );

            System.out.println(response.getBody());

            if (response.getBody() != null) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<ProfileDto>> getDoctors() {
        try {
            ResponseEntity<ProfileDto[]> response = restTemplate.exchange(
                    userServiceUrl + "/doctors",
                    HttpMethod.GET,
                    null,
                    ProfileDto[].class
            );

            if (response.getBody() != null) {
                return ResponseEntity.ok(Arrays.asList(response.getBody()));
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<BooleanDto> deleteUser(@RequestBody DeleteUserDto dto) {
        try {
            String url = userServiceUrl + "/" + dto.getId();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", dto.getToken());

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<BooleanDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    entity,
                    BooleanDto.class
            );

            return response;

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
