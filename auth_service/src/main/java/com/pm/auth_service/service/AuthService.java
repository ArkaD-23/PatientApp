package com.pm.auth_service.service;

import com.pm.auth_service.dto.BooleanDto;
import com.pm.auth_service.dto.LoginDto;
import com.pm.auth_service.dto.RegisterDto;
import com.pm.auth_service.dto.LoginResponseDto;
import com.pm.auth_service.exception.UserAlreadyExistsException;
import com.pm.auth_service.model.User;
import com.pm.auth_service.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public BooleanDto registerUser(@Valid RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            return new BooleanDto(false);
        }

        User user = new User();
        String prefix = registerDto.getEmail().split("@")[0].toLowerCase();
        user.setUsername(prefix);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setEmail(registerDto.getEmail());
        user.setFullname(registerDto.getFullname());
        user.setRole(registerDto.getRole());
        userRepository.save(user);

        return new BooleanDto(true);
    }

    public LoginResponseDto userLogin(@Valid LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        try {
            authenticationManager.authenticate(authToken);
            User user = userRepository.findByEmail(loginDto.getEmail());
            String jwt = jwtUtil.generateToken(user.getEmail(), user.getRole());
            return new LoginResponseDto(true, jwt);
        } catch (AuthenticationException e) {
            return new LoginResponseDto(false, "");
        }
    }
}
