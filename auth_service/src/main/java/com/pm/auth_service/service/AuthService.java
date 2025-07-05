package com.pm.auth_service.service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.pm.auth_service.dto.BooleanDto;
import com.pm.auth_service.dto.LoginDto;
import com.pm.auth_service.dto.RegisterDto;
import com.pm.auth_service.exception.UserAlreadyExistsException;
import com.pm.auth_service.exception.UserAlreadyLoggedInException;
import com.pm.auth_service.model.User;
import com.pm.auth_service.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextHolderStrategy securityContextHolderStrategy =
            SecurityContextHolder.getContextHolderStrategy();
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public BooleanDto registerUser(Authentication authentication, @Valid @RequestBody RegisterDto registerDto) throws UserAlreadyExistsException {

        checkIfUserLoggedIn(authentication);

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new UserAlreadyExistsException("There already exists an account with the email address: " + registerDto.getEmail());
        }

        User user = new User();
        String prefix = registerDto.getEmail().split("@")[0].toLowerCase();
        if (!userRepository.existsByUsername(prefix)) {
            user.setUsername(prefix);
        } else {
            user.setUsername(generateRandomUsername(prefix));
        }
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setEmail(registerDto.getEmail());
        user.setFullname(registerDto.getFullname());
        user.setRole(registerDto.getRole());
        userRepository.save(user);

        return new BooleanDto(true);
    }

    public BooleanDto userLogin(Authentication authentication, @Valid @RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) throws UserAlreadyLoggedInException {

        checkIfUserLoggedIn(authentication);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        Authentication auth;

        try {
            auth = authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {

            authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
            auth = authenticationManager.authenticate(authenticationToken);
        }

        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(auth);
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        return new BooleanDto(true);
    }

    private void checkIfUserLoggedIn(Authentication authentication) throws UserAlreadyLoggedInException {

        if(authentication != null && authentication.isAuthenticated()) {
            throw new UserAlreadyLoggedInException("User already logged in");
        }
    }

    private String generateRandomUsername(String prefix) {

        String nanoId = NanoIdUtils.randomNanoId();

        return prefix + "_" + nanoId;
    }
}
