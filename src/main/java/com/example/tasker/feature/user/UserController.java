package com.example.tasker.feature.user;

import com.example.tasker.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import com.example.tasker.feature.user.dto.UpdateProfileRequest;

@RestController
@RequestMapping("api/v1/users/")
@RequiredArgsConstructor
public class UserController {
        private final UserRepository userRepository;
        private final UserService userService;

        @PreAuthorize("isAuthenticated()")
        @GetMapping("/me")
        public ResponseEntity<?> me(@AuthenticationPrincipal UserDetails principal) {
                User user = userRepository.findByEmail(principal.getUsername()).orElseThrow(
                                () -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "User not found"));
                return ResponseEntity.ok(
                                Map.of(
                                                "id", user.getId(),
                                                "fullName", user.getFullName(),
                                                "email", user.getEmail(),
                                                "role", user.getRole().name()));
        }

        @PatchMapping("/me")
        public ResponseEntity<?> updateProfile(@AuthenticationPrincipal UserDetails me,
                        @RequestBody UpdateProfileRequest request) {
                User user = userService.updateProfile(me.getUsername(), request);
                return ResponseEntity.ok(
                                Map.of(
                                                "id", user.getId(),
                                                "fullName", user.getFullName(),
                                                "email", user.getEmail(),
                                                "role", user.getRole().name()));
        }
}
