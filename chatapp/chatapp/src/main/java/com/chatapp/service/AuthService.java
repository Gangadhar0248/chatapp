
package com.chatapp.service;

import com.chatapp.entity.User;
import com.chatapp.repository.UserRepository;
import com.chatapp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    public User signup(User user) {

        user.setPassword(encoder.encode(user.getPassword()));

        return userRepository.save(user);
    }



    public String login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(email);
    }

}
