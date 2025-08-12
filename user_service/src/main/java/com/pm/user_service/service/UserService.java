package com.pm.user_service.service;

import com.pm.user_service.dto.BooleanDto;
import com.pm.user_service.dto.ProfileDto;
import com.pm.user_service.dto.ProfileResponseDto;
import com.pm.user_service.exception.InvalidTokenException;
import com.pm.user_service.exception.UserNotFoundException;
import com.pm.user_service.model.User;
import com.pm.user_service.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    UserRepository userRepository;
    JwtUtil jwtUtil;
    PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean createUser(ProfileDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            return false;
        }

        User user = new User();
        user.setFullname(dto.getFullname());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());

        userRepository.save(user);
        return true;
    }

    public boolean validateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }

    public ProfileResponseDto userUpdate(@Valid ProfileDto profileDto, String token) throws InvalidTokenException, UserNotFoundException {
        if (!jwtUtil.validateToken(token)) {
            throw new InvalidTokenException("Invalid token");
        }

        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        if (profileDto.getFullname() != null && !profileDto.getFullname().isEmpty()) {
            user.setFullname(profileDto.getFullname());
        }

        if (profileDto.getPassword() != null && !profileDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(profileDto.getPassword()));
        }

        userRepository.save(user);

        return new ProfileResponseDto(user);
    }

    public ProfileResponseDto getUser(String username, String token) throws InvalidTokenException, UserNotFoundException {
        if (!jwtUtil.validateToken(token)) {
            throw new InvalidTokenException("Invalid token");
        }
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        return new ProfileResponseDto(user);
    }

    public BooleanDto deleteUser(UUID id, String token) throws InvalidTokenException, UserNotFoundException {
        if (!jwtUtil.validateToken(token)) {
            throw new InvalidTokenException("Invalid token");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        userRepository.delete(user);
        return new BooleanDto(true);
    }
}
