package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PostMapping("/{userId}/profile")
    public UserProfile createUserProfile(@PathVariable Long userId, @RequestBody UserProfile userProfile) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        userProfile.setUser(user);
        return userProfileRepository.save(userProfile);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
