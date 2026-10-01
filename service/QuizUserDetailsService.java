package com.example.Quizapp.service;

import com.example.Quizapp.model.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class QuizUserDetailsService implements UserDetailsService {

    private final ConcurrentHashMap<String, User> userMap = new ConcurrentHashMap<>();
    private final PasswordEncoder passwordEncoder;

    public QuizUserDetailsService(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        registerUser("admin", "admin123", "admin@quiz.com", "ROLE_ADMIN");
        registerUser("user", "user123", "user@quiz.com", "ROLE_USER");
    }

    public void registerUser(String username, String rawPassword, String email, String role) {
        String encodedPassword = rawPassword.startsWith("$2a$") ? rawPassword : passwordEncoder.encode(rawPassword);
        userMap.put(username, new User(username, email, encodedPassword, role));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMap.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole())
                .build();
    }
}
