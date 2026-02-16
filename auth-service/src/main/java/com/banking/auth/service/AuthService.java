package com.banking.auth.service;

import com.banking.auth.dto.request.LoginRequest;
import com.banking.auth.dto.request.RegisterRequest;
import com.banking.auth.dto.response.AuthResponse;
import com.banking.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    // TODO: JWT-based register implementation
    public AuthResponse register(RegisterRequest request) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // TODO: JWT-based login implementation
    public AuthResponse login(LoginRequest request) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}