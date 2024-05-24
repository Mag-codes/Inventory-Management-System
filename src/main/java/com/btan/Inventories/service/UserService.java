package com.btan.Inventories.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.btan.Inventories.model.Role;
import com.btan.Inventories.model.User;
import com.btan.Inventories.repo.UserRepository;
import com.btan.Inventories.settings.EmailConfigurer;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailConfigurer emailConfigurer;

    @Autowired
    private RoleService roleService;

    public Map<String, Object> registerUser(@Valid User user) throws MessagingException {
        Map<String, Object> response = new HashMap<>();
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            response.put("Error", "Email or username has been taken");
            return response;
        } else {

            // hashing the password
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);

            // setting user role
            Optional<Role> optionalRole = roleService.getRoleByName("STAFF");
            if (optionalRole.isPresent()) {
                Role role = optionalRole.get();
                user.setRole(role);
            }

            User theUser = userRepository.save(user);
            emailConfigurer.sendConfirmaitonEmail(user.getEmail(), "Account created successfully",
                    "Account Confirmation", "", "");
            response.put("message", "User successfully registered");
            response.put("data", theUser);
            return response;
        }
    }

    public Map<String, Object> getUserProfile(UUID userId) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            response.put("data", optionalUser.get());
        } else {
            response.put("Error", "User not found");
        }
        return response;
    }

    public Map<String, Object> updateUserProfile(UUID userId, @Valid User updatedUser) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();

            if (updatedUser.getFullName() != null) {
                existingUser.setFullName(updatedUser.getFullName());
            }
            if (updatedUser.getEmail() != null) {
                existingUser.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getPassword() != null) {
                existingUser.setPassword(updatedUser.getPassword());
            }

            userRepository.save(existingUser);
            response.put("message", "User profile updated successfully");
            response.put("data", existingUser);
        } else {
            response.put("Error", "User not found");
        }
        return response;
    }

    public Map<String, Object> removeUser(UUID userId) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
            response.put("message", "User successfully removed");
        } else {
            response.put("Error", "User not found");
        }
        return response;
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isPresent()) {
            User theUser = user.get();
            return theUser;
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
