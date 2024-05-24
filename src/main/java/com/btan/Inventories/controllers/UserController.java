package com.btan.Inventories.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.btan.Inventories.Util.LoginDTO;
import com.btan.Inventories.model.User;
import com.btan.Inventories.repo.UserRepository;
import com.btan.Inventories.service.UserService;
import com.btan.Inventories.settings.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users/")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;



    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginDTO loginDto,
            HttpServletResponse response) {
        Map<String, String> responseBody = new HashMap<>();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

            final UserDetails userDetails = userService.loadUserByUsername(loginDto.getEmail());

            Optional<User> user = userRepository.findByEmail(loginDto.getEmail());
            User theUser = user.orElse(null);
            final String token = jwtService.createToken(userDetails);

            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(2 * 24 * 60 * 60);

            response.addCookie(cookie);

            responseBody.put("message", "Login successful");
            if (theUser != null) {
                responseBody.put("userId", theUser.getId().toString());
            }
            return ResponseEntity.ok(responseBody);
        } catch (BadCredentialsException e) {
            responseBody.put("Error", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        } catch (UsernameNotFoundException e) {
            responseBody.put("Error", "User not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
            responseBody.put("Error", "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    @PostMapping(value = "/register", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            response = userService.registerUser(user);
            if (response.containsKey("Error")) {
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("Error", "Internal Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/profile/{userId}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getUserProfile(@PathVariable UUID userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            response = userService.getUserProfile(userId);
            if (response.containsKey("Error")) {
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("Error", "Internal Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/profile/{userId}", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> updateUserProfile(@PathVariable UUID userId, @Valid @RequestBody User updatedUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            response = userService.updateUserProfile(userId, updatedUser);
            if (response.containsKey("Error")) {
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("Error", "Internal Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{userId}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> removeUser(@PathVariable UUID userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            response = userService.removeUser(userId);
            if (response.containsKey("Error")) {
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("Error", "Internal Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/all", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getAllUsers() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> users = userService.getAllUsers();
            response.put("users", users);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            response.put("Error", "Internal Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
