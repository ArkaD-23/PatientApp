package com.pm.user_service.controller;

import com.pm.user_service.dto.BooleanDto;
import com.pm.user_service.dto.ProfileDto;
import com.pm.user_service.dto.ProfileResponseDto;
import com.pm.user_service.dto.ValidationDto;
import com.pm.user_service.exception.InvalidTokenException;
import com.pm.user_service.exception.UserNotFoundException;
import com.pm.user_service.model.User;
import com.pm.user_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<BooleanDto> createUser(@RequestBody ProfileDto profileDto) {
        try {
            Boolean userCreated = userService.createUser(profileDto);
            System.out.println(userCreated);
            return ResponseEntity.ok(new BooleanDto(userCreated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<BooleanDto> validateUser(@RequestBody ValidationDto validationDto) {
        try {
            Boolean isValid = userService.validateUser(validationDto.getEmail(), validationDto.getPassword());
            System.out.println(isValid);
            return ResponseEntity.ok(new BooleanDto(isValid));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{email}/{token}")
    public ResponseEntity<ProfileResponseDto> getUser(
            @PathVariable String email,
            @PathVariable String token) {
        try {
            ProfileResponseDto result = userService.getUser(email, token);
            System.out.println(result);
            return ResponseEntity.ok(result);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidTokenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponseDto> getUserById(@PathVariable String id) {
        try {
            ProfileResponseDto result = userService.getUserById(id);
            return ResponseEntity.ok(result);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BooleanDto> deleteUser(
            @PathVariable String id,
            @RequestHeader("Authorization") String token) {
        try {
            BooleanDto result = userService.deleteUser(id, token);
            return ResponseEntity.ok(result);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidTokenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<ProfileResponseDto>> getAllDoctors() {
        try {
            List<User> doctors = userService.getDoctors("DOCTOR");

            List<ProfileResponseDto> response = doctors.stream()
                    .map(user -> new ProfileResponseDto(
                            user.getId(),
                            user.getFullname(),
                            user.getEmail(),
                            user.getRole(),
                            user.getUsername()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
