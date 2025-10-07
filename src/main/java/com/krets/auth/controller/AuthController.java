package com.krets.auth.controller;



import com.krets.auth.dto.JwtResponse;
import com.krets.auth.dto.LoginRequest;
import com.krets.auth.dto.MessageResponse;
import com.krets.auth.dto.RegisterRequest;
import com.krets.auth.model.User;
import com.krets.auth.repository.UserRepository;
import com.krets.auth.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
           if (userRepository.existsByUsername(registerRequest.getUsername())) {
        // Return a JSON response for the error
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(registerRequest.getEmail())) {
        // Return a JSON response for the error
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(registerRequest.getUsername(),
                         registerRequest.getEmail(),
                         encoder.encode(registerRequest.getPassword()));

    userRepository.save(user);

    // Return a JSON response for the success case
    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}