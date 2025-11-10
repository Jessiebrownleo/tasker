package com.example.tasker.feature.auth;

import com.example.tasker.config.JwtService;
import com.example.tasker.domain.Role;
import com.example.tasker.domain.User;
import com.example.tasker.feature.auth.dto.LoginRequest;
import com.example.tasker.feature.auth.dto.RegisterRequest;
import com.example.tasker.feature.auth.dto.TokenResponse;
import com.example.tasker.feature.user.UserRepository;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public TokenResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Email is " +
                    "already registered!!");
        }
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        return issueToken(user.getEmail(),user.getRole().name());
    }

    //Login

    public TokenResponse login(LoginRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        User user= userRepository.findByEmail(request.getEmail()).orElseThrow(
                ()->new ResponseStatusException(
                        HttpStatus.NOT_FOUND,"User not found"
                )
        );
        return issueToken(user.getEmail(),user.getRole().name());
    }



    //Issue Token
    private TokenResponse issueToken(String email,String role){
        TokenResponse res = new TokenResponse();
        String access = jwtService.generateAccessToken(email, Map.of("role",role));
        String refresh = jwtService.generateRefreshToken(email);
        res.setAccessToken(access);
        res.setRefreshToken(refresh);
        return res;
    }

}
